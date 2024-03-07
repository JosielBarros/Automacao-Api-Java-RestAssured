package features.tests.lojinha.usuario;

import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.usuario.BaseLoginPath;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.data.lojinha.data_factory.UsuarioDataFactory;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GerarTokenTest {

    @BeforeEach
    public void setUp(){
        BaseConfig.configApis();
    }
    @Test
    @DisplayName("Validar tentativa de gerar token com usuario invalido")
    public void validarTentativaGerarTokenUsuarioInvalido(){
        given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarUsuarioAutenticacao("usuario_invalido", "senha_invalida"))
        .when()
            .post(BaseLoginPath.getPath())
        .then()
            .statusCode(401);
    }
    @Test
    @DisplayName("Validar geração de token")
    public void validarGeracaoToken(){
        given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarUsuarioAutenticacao("jhon", "123"))
        .when()
            .post(BaseLoginPath.getPath())
        .then()
            .assertThat()
                .statusCode(200)
                .body("message", equalTo("Sucesso ao realizar o login"));
    }
}
