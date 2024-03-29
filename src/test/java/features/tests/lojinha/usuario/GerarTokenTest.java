package features.tests.lojinha.usuario;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import support.data.lojinha.data_factory.UsuarioDataFactory;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Gerar token de acesso do usuário")
public class GerarTokenTest {

    @BeforeClass
    public static void setUp(){
        BaseConfig.configApis();
    }
    @Test
    @DisplayName("Validar tentativa de gerar token com usuario invalido")
    public void testValidaTentativaGerarTokenUsuarioInvalido(){
        gerartoken("usuario_invalido", "senha_invalida").assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
    @Test
    @DisplayName("Validar geração de token")
    public void testValidaGeracaoToken(){
        gerartoken("jhon", "123").assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("message", equalTo("Sucesso ao realizar o login"));
    }
    private ValidatableResponse gerartoken(String login, String senha){
        return   given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarUsuarioAutenticacao(login, senha))
        .when()
            .post(BaseLoginPath.getPath())
        .then();
    }
}
