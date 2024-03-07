package support.data.lojinha.data_factory;

import support.data.lojinha.pojo.lojinha.UsuarioPojo;

public class UsuarioDataFactory {
    static UsuarioPojo usuario = new UsuarioPojo();
    public static UsuarioPojo criarUsuarioAutenticacao(String login, String senha){
        usuario.setUsuarioLogin(login);
        usuario.setUsuarioSenha(senha);
        return usuario;
    }
    public static UsuarioPojo criarNovoUsuario(String nome, String login, String senha){
        criarUsuarioAutenticacao(login, senha);
        usuario.setUsuarioNome(nome);
        return usuario;
    }
}
