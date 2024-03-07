package features.tests.lojinha.usuario;

import com.github.javafaker.Faker;
import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.usuario.BaseUsuarioPath;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.data.lojinha.data_factory.UsuarioDataFactory;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AdicionarUsuarioTest {
    Faker faker = new Faker();
    @BeforeEach
    public void setUp(){
        BaseConfig.configApis();
    }

    @Test
    @DisplayName("Validar cadastro de usuário")
    public void validarCadastroUsuario(){
        String nome = faker.name().firstName();
        String login = faker.name().firstName() + "@";
        given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarNovoUsuario(nome, login, "123"))
        .when()
            .post(BaseUsuarioPath.getPath())
        .then()
            .assertThat()
                .statusCode(201)
                .body("data.usuarioNome", equalTo(nome))
                .body("data.usuarioLogin", equalTo(login))
                .body("message", equalTo("Usuário adicionado com sucesso"));
    }
    @Test
    @DisplayName("Validar tentativa de cadastro com usuário duplicado")
    public void validarTentativaCadastroUsuarioDuplicado(){
        given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarNovoUsuario("jhon", "jhon", "123"))
        .when()
            .post(BaseUsuarioPath.getPath())
        .then()
            .assertThat()
                .statusCode(409)
                .body("error", equalTo("O usuário jhon já existe."));
    }
}
