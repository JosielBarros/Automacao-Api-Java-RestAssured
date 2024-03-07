package features.tests.lojinha.usuario;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.data.lojinha.data_factory.UsuarioDataFactory;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GerarTokenTest {

    @BeforeAll
    public static void setUp(){
        BaseConfig.configApis();
    }
    @Test
    @DisplayName("Validar tentativa de gerar token com usuario invalido")
    public void validarTentativaGerarTokenUsuarioInvalido(){
        gerartoken("usuario_invalido", "senha_invalida").assertThat()
            .statusCode(401);
    }
    @Test
    @DisplayName("Validar geração de token")
    public void validarGeracaoToken(){
        gerartoken("jhon", "123").assertThat()
            .statusCode(200)
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
