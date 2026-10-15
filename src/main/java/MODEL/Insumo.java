package MODEL;

public class Insumo {
    private Integer id;
    private String nome;
    private Integer unidadeMedida;
    private Integer quantquantidadeAtual;
    private Integer estoqueMinimo;


    public Insumo() {
    }

    public Insumo(Integer id, String nome, Integer unidadeMedida, Integer quantquantidadeAtual, Integer estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
        this.quantquantidadeAtual = quantquantidadeAtual;
        this.estoqueMinimo = estoqueMinimo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(Integer unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Integer getQuantquantidadeAtual() {
        return quantquantidadeAtual;
    }

    public void setQuantquantidadeAtual(Integer quantquantidadeAtual) {
        this.quantquantidadeAtual = quantquantidadeAtual;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }
}