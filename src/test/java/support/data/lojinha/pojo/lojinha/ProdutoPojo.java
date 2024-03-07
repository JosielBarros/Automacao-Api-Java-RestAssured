package support.data.lojinha.pojo.lojinha;

import java.util.List;

public class ProdutoPojo {
    private String produtoNome;
    private Double produtoValor;
    private List<String> produtoCores;
    private String produtoUrlMock;
    private List<ComponentesPojo> componentes;

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public void setProdutoValor(Double produtoValor) {
        this.produtoValor = produtoValor;
    }

    public void setProdutoCores(List< String > produtoCores) {
        this.produtoCores = produtoCores;
    }

    public void setProdutoUrlMock(String produtoUrlMock) {
        this.produtoUrlMock = produtoUrlMock;
    }

    public void setComponentes(List< ComponentesPojo > componentes) {
        this.componentes = componentes;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Double getProdutoValor() {
        return produtoValor;
    }

    public List< String > getProdutoCores() {
        return produtoCores;
    }

    public String getProdutoUrlMock() {
        return produtoUrlMock;
    }

    public List< ComponentesPojo > getComponentes() {
        return componentes;
    }
}
