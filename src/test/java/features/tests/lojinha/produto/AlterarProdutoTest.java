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

@DisplayName("Alterar produto do usuário")
public class AlterarProdutoTest {
    private static String token;
    private static String nomeProduto;
    private static String corProduto;
    private static String componenteNome;
    private static int componenteQuantidade;
    private static Double valorProduto;
    private static Integer produtoId;

    @Before
    public void setUp(){
        BaseConfig.configApis();
        corProduto = "Vermelha";
        nomeProduto = "Smartphone Motorola";
        componenteNome = "Carregador";
        componenteQuantidade = 1;
        valorProduto = 2100.00;
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
            .body(ProdutoDataFactory.criarProduto(nomeProduto, valorProduto, componenteNome, componenteQuantidade, corProduto))
        .when()
            .post(BaseProdutoPath.getPath())
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                    .path("data.produtoId");
    }
    @Test
    @DisplayName("Validar alteração de dados do produto")
    public void testValidarAlteracaoDeDadosDoProduto(){
        nomeProduto = "PlayStation 5";
        corProduto = "Branca";
        componenteNome = "Controle";
        componenteQuantidade = 2;
        valorProduto = 4500.00;
        alterarProduto().assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("data.produtoId", equalTo(produtoId))
            .body("data.produtoCores", containsString(corProduto))
            .body("data.componentes[0].componenteNome", equalTo(componenteNome))
            .body("data.componentes[0].componenteQuantidade", equalTo(componenteQuantidade));
    }
    @Test
    @DisplayName("Validar que valor do produto não pode ser alterado para menor que 0.00")
    public void testValidarQueValorDoProdutoNaoPodeSerAlteradoParaMenorQue0(){
        valorProduto = 0.00;
        alterarProduto().assertThat()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("data", empty())
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"));
    }
    @Test
    @DisplayName("Validar que valor do produto não pode ser alterado para maior que 7000.00")
    public void testValidarQueValorDoProdutoNaoPodeSerAlteradoParaMaiorQue7000(){
        valorProduto = 7000.01;
        alterarProduto().assertThat()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("data", empty())
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"));
    }
    @Test
    @DisplayName("Validar que o valor é obrigatório ao alterar o produto")
    public void testValidarQueOValorEObrigatorioAoAlterarOProduto(){
        valorProduto = null;
        alterarProduto().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("data", empty())
                .body("error", equalTo("produtoNome, produtoValor e produtoCores são campos obrigatórios"));
    }
    private ValidatableResponse alterarProduto(){
        return given()
            .contentType(ContentType.JSON)
            .header("token", token)
            .body(ProdutoDataFactory.criarProduto(nomeProduto, valorProduto, componenteNome, componenteQuantidade, corProduto))
        .when()
            .put(BaseProdutoPath.getPath() + "/" + produtoId)
        .then();
    }
    @After
    public void tearDown(){
        removerProdutoCriado(produtoId, token);
    }
}
