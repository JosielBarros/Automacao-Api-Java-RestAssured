package features.tests.lojinha.produto;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.produto.BaseProdutoPath;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.data.lojinha.data_factory.ProdutoDataFactory;
import support.data.lojinha.data_factory.UsuarioDataFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BuscarProdutoIdTest {
    private static String token;
    private static String nomeProduto;
    private static String corProduto;
    private static int produtoId;
    @BeforeEach
    public void setUp(){
        BaseConfig.configApis();
        token = given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarUsuarioAutenticacao("jhon", "123"))
        .when()
            .post(BaseLoginPath.getPath())
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
                .path("data.token");
    }
    @Test
    @DisplayName("Validar busca de produto por id")
    public void validarBuscaDeProdutoPorId(){
        cadastrarProduto();
        buscarProdutoPorId().assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("data.produtoId", equalTo(produtoId))
            .body("data.produtoNome", equalTo(nomeProduto))
            .body("data.produtoCores", hasItem(corProduto))
            .body("data.componentes[0].componenteNome", equalTo("Carregador"));
    }
    @Test
    @DisplayName("Validar tentativa de busca de produto por id invalido")
    public void validarTentativaDeBuscaDeProdutoComIdInvalido(){
        produtoId = 0;
        buscarProdutoPorId().assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
    private ValidatableResponse buscarProdutoPorId(){
        return  given()
            .header("token", token)
        .when()
            .get(BaseProdutoPath.getPath() + "/" + produtoId)
        .then();
    }
    private void cadastrarProduto(){
        corProduto = "Vermelha";
        nomeProduto = "Smartphone Motorola";
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
}