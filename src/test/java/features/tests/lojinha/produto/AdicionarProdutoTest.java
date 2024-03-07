package features.tests.lojinha.produto;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.produto.BaseProdutoPath;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.data.lojinha.data_factory.ProdutoDataFactory;
import support.data.lojinha.data_factory.UsuarioDataFactory;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class AdicionarProdutoTest {
    private static String token;
    private final String NOME_PRODUTO = "MacBook";
    private double valorProduto = 3000.00;
    private final String NOME_COMPONENTE = "Mouse";
    private final String COR_PRODUTO = "Vermelha";

    @BeforeAll
    public static void setUp(){
        BaseConfig.configApis();
        token = given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarUsuarioAutenticacao("jhon", "123"))
        .when()
            .post(BaseLoginPath.getPath())
        .then()
            .statusCode(200)
            .extract()
                .path("data.token");
    }

    @Test
    @DisplayName("Validar cadastro de produto")
    public void validarCadastroProduto(){
        adicionarUmProduto().assertThat()
            .statusCode(201)
            .body("message", equalTo("Produto adicionado com sucesso"))
            .body("data.produtoNome", equalTo(NOME_PRODUTO))
            .body("data.componentes[0].componenteQuantidade", equalTo(1))
            .body("data.componentes[0].componenteNome", equalTo(NOME_COMPONENTE))
            .body("data.produtoCores", hasItem(COR_PRODUTO));
    }

    @Test
    @DisplayName("Validar tentativa de cadastro do produto com valor do produto menor que 0.00")
    public void validarTentativaCadastroProdutoComValorProdutoMenorQue0(){
        valorProduto = 0.00;
        adicionarUmProduto().assertThat()
            .statusCode(422)
            .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"));
    }

    @Test
    @DisplayName("Validar tentativa de cadastro do produto com valor do produto maior que 7000.00")
    public void validarTentativaCadastroProdutoValorProdutoMaiorQue7000(){
        valorProduto = 7000.01;
        adicionarUmProduto().assertThat()
            .statusCode(422)
            .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"));
    }
    private ValidatableResponse adicionarUmProduto(){
        return given()
            .contentType(ContentType.JSON)
            .header("token", token)
            .body(ProdutoDataFactory.criarProduto(NOME_PRODUTO, valorProduto, NOME_COMPONENTE, 1, COR_PRODUTO))
        .when()
            .post(BaseProdutoPath.getPath())
        .then();
    }
}
