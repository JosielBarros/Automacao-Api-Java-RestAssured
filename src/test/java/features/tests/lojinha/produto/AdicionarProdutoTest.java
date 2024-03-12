package features.tests.lojinha.produto;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.produto.BaseProdutoPath;
import features.clients.lojinha.usuario.BaseLoginPath;
import org.junit.Before;
import org.junit.Test;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import support.data.lojinha.data_factory.ProdutoDataFactory;
import support.data.lojinha.data_factory.UsuarioDataFactory;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

@DisplayName("Adicionar produto ao usuário")
public class AdicionarProdutoTest {
    private static String token;
    private static final String NOME_PRODUTO = "MacBook";
    private static final String NOME_COMPONENTE = "Mouse";
    private static final String COR_PRODUTO = "Vermelha";

    @Before
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
    @DisplayName("Validar cadastro de produto")
    public void testValidarCadastroProduto(){
        int produtoId = adicionarUmProduto(3000.00).assertThat()
            .statusCode(HttpStatus.SC_CREATED)
            .body("message", equalTo("Produto adicionado com sucesso"))
            .body("data.produtoNome", equalTo(NOME_PRODUTO))
            .body("data.componentes[0].componenteQuantidade", equalTo(1))
            .body("data.componentes[0].componenteNome", equalTo(NOME_COMPONENTE))
            .body("data.produtoCores", hasItem(COR_PRODUTO))
            .extract().path("data.produtoId");
        removerProdutoCriado(produtoId, token);
    }
    @Test
    @DisplayName("Validar tentativa de cadastro do produto com valor do produto menor que 0.00")
    public void testValidarTentativaCadastroProdutoComValorProdutoMenorQue0(){
        adicionarUmProduto(0.00).assertThat()
            .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
            .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"));
    }
    @Test
    @DisplayName("Validar tentativa de cadastro do produto com valor do produto maior que 7000.00")
    public void testValidarTentativaCadastroProdutoValorProdutoMaiorQue7000(){
        adicionarUmProduto(7000.01).assertThat()
            .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
            .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"));
    }
    @Test
    @DisplayName("Validar que o valor é obrigatório ao cadastrar o produto")
    public void testValidarQueOValorEObrigatorioAoCadastrarOProduto(){
        adicionarUmProduto(null).assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("data", empty())
            .body("error", equalTo("produtoNome, produtoValor e produtoCores são campos obrigatórios"));
    }
    private ValidatableResponse adicionarUmProduto(Double valorProduto){
        return given()
            .contentType(ContentType.JSON)
            .header("token", token)
            .body(ProdutoDataFactory.criarProduto(NOME_PRODUTO, valorProduto, NOME_COMPONENTE, 1, COR_PRODUTO))
        .when()
            .post(BaseProdutoPath.getPath())
        .then();
    }
    public static void removerProdutoCriado(Integer produtoId, String token){
        if (produtoId != null && token != null) {
            given()
                .header("token", token)
            .when()
                .delete(BaseProdutoPath.getPath() + "/" + produtoId)
            .then()
                .assertThat()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        }
    }
}
