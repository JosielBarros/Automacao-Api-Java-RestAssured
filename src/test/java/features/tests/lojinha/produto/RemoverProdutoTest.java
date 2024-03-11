package features.tests.lojinha.produto;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.produto.BaseProdutoPath;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import support.data.lojinha.data_factory.ProdutoDataFactory;
import support.data.lojinha.data_factory.UsuarioDataFactory;

import static io.restassured.RestAssured.given;

public class RemoverProdutoTest {
    private static String token;
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
    @DisplayName("Validar remoção de um produto")
    public void validarRemocaoDeUmProduto(){
        removerProduto(cadastrarProduto()).assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }
    @Test
    @DisplayName("Validar que produto não é encontrado ao tentar remover com id invalido")
    public void validarQueprodutoNaoEEncontradoAoTentarRemoverComIdInvalido(){
        removerProduto(0).assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
    private ValidatableResponse removerProduto(int produtoId){
        return given()
            .header("token", token)
        .when()
            .delete(BaseProdutoPath.getPath() + "/" + produtoId)
        .then();
    }
    private int cadastrarProduto(){
        return given()
            .contentType(ContentType.JSON)
            .header("token", token)
            .body(ProdutoDataFactory.criarProduto("Smartphone Motorola", 4200.00, "Carregador", 1, "Vermelha"))
        .when()
            .post(BaseProdutoPath.getPath())
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                    .path("data.produtoId");
    }
}
