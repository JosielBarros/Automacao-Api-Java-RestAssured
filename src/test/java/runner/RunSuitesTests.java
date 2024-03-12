package runner;

import features.tests.lojinha.produto.*;
import features.tests.lojinha.usuario.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdicionarProdutoTest.class,
        AlterarProdutoTest.class,
        BuscarProdutoIdTest.class,
        BuscarProdutoTest.class,
        RemoverProdutoTest.class,
        AdicionarUsuarioTest.class,
        GerarTokenTest.class
})
public class RunSuitesTests {
}
