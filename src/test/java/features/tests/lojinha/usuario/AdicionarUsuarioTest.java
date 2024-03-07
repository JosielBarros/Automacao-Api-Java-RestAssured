package features.tests.lojinha.usuario;

import com.github.javafaker.Faker;
import features.clients.lojinha.BaseConfig;
import features.clients.lojinha.usuario.BaseUsuarioPath;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.data.lojinha.data_factory.UsuarioDataFactory;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AdicionarUsuarioTest {
    Faker faker = new Faker();
    private static final String NOME_USUARIO = "Jhon";
    // O ideal seria exluir o usuário ao final do teste e cadastrar novamente. Mas a API não tem o endpoint de exclusão.
    @BeforeAll
    public static void setUp(){
        BaseConfig.configApis();
    }
    @Test
    @DisplayName("Validar cadastro de usuário")
    public void validarCadastroUsuario(){
        String usuarioLogin = faker.name().firstName() + "@";
        cadastrarUsuario(usuarioLogin).assertThat()
            .statusCode(HttpStatus.SC_CREATED)
            .body("data.usuarioNome", equalTo(NOME_USUARIO))
            .body("data.usuarioLogin", equalTo(usuarioLogin))
            .body("message", equalTo("Usuário adicionado com sucesso"));
    }
    @Test
    @DisplayName("Validar tentativa de cadastro para usuário já cadastrado")
    public void validarTentativaCadastroUsuarioDuplicado(){
        String usuarioLogin = "jhon@gmail";
        cadastrarUsuario(usuarioLogin).assertThat()
            .statusCode(HttpStatus.SC_CONFLICT)
            .body("error", equalTo("O usuário " + usuarioLogin + " já existe."));
    }
    private ValidatableResponse cadastrarUsuario(String usuarioLogin){
        return   given()
            .contentType(ContentType.JSON)
            .body(UsuarioDataFactory.criarNovoUsuario(NOME_USUARIO, usuarioLogin, "123"))
        .when()
            .post(BaseUsuarioPath.getPath())
        .then();
    }
}