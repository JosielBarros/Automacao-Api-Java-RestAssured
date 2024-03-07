package support.data.lojinha.data_factory;

import support.data.lojinha.pojo.lojinha.ComponentesPojo;
import support.data.lojinha.pojo.lojinha.ProdutoPojo;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDataFactory {
    public static ProdutoPojo criarProduto(String nome, double valor, String componenteNome, int componenteQuantidade, String cor){
        ProdutoPojo produto = new ProdutoPojo();
        produto.setProdutoNome(nome);
        produto.setProdutoValor(valor);
        List<String> cores = new ArrayList<>();
        cores.add(cor);
        cores.add("Preto");
        produto.setProdutoCores(cores);
        ComponentesPojo primeiroComponente = new ComponentesPojo();
        primeiroComponente.setComponenteNome(componenteNome);
        primeiroComponente.setComponenteQuantidade(componenteQuantidade);
        List<ComponentesPojo> componentes = new ArrayList<>();
        componentes.add(primeiroComponente);
        produto.setComponentes(componentes);
        return produto;
    }
}
