package features.tests.lojinha.produto;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.produto.BaseProdutoPath;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import support.data.lojinha.data_factory.ProdutoDataFactory;
import support.data.lojinha.data_factory.UsuarioDataFactory;

import static features.tests.lojinha.produto.AdicionarProdutoTest.removerProdutoCriado;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Buscar por produtos do usuário")
public class BuscarProdutoTest {
    private static String token;
    private static String nomeProduto;
    private static String corProduto;
    private static int produtoId;

    @Before
    public void setUp(){
        BaseConfig.configApis();
        corProduto = "Vermelha";
        nomeProduto = "Smartphone Motorola";
        token = given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarUsuarioAutenticacao("jhon", "123"))
        .when()
            .post(BaseLoginPath.getPath())
        .then()
            .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("data.token");

        produtoId = given()
            .contentType(ContentType.JSON)
            .header("token", token)
            .body(ProdutoDataFactory.criarProduto(nomeProduto, 4200.00, "Carregador", 1, corProduto))
        .when()
            .post(BaseProdutoPath.getPath())
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("data.produtoId");
    }
    @Test
    @DisplayName("Validar busca de produto para um usuário")
    public void validarBuscaDeProdutoParaUmUsuario(){
        corProduto = "";
        nomeProduto = "";
        buscarProdutosDeUmUsuario().assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("data[0].produtoId", greaterThan(0))
            .body("data[0].produtoNome", notNullValue())
            .body("data[0].produtoValor", greaterThan(0));
    }
    @Test
    @DisplayName("Validar busca de produto por nome")
    public void validarBuscaDeProdutoPorNome() {
        corProduto = "";
        buscarProdutosDeUmUsuario().assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("data.size()", greaterThan(0))
            .body("data.produtoNome", everyItem(equalTo(nomeProduto)));
    }
    @Test
    @DisplayName("Validar busca de produtos por cor")
    public void validarBuscaDeProdutoPorCor(){
        nomeProduto = "";
        buscarProdutosDeUmUsuario().assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("data.size()", greaterThan(0))
            .body("data.produtoCores", everyItem(hasItem(corProduto)));
    }
    @Test
    @DisplayName("Validar tentativa de busca por produto não cadastrado")
    public void validarTentativaDeBuscaPorProdutoNaoCadastrado(){
        nomeProduto = "Produto não cadastrado";
        corProduto = "";
        buscarProdutosDeUmUsuario().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data", empty());
    }
    private ValidatableResponse buscarProdutosDeUmUsuario(){
        return  given()
            .header("token", token)
            .params("produtoNome", nomeProduto, "produtoCores", corProduto)
        .when()
            .get(BaseProdutoPath.getPath())
        .then();
    }
    @After
    public void tearDown(){
        removerProdutoCriado(produtoId, token);
    }
}
