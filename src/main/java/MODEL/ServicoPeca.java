package MODEL;

public class ServicoPeca {
    private Integer id;
    private Integer idServico;
    private Integer idPeca;
    private Double quantidade;
    private Double valorUnitario;

    public ServicoPeca() {}

    public ServicoPeca(Integer id, Integer idServico, Integer idPeca, Double quantidade, Double valorUnitario) {
        this.id = id;
        this.idServico = idServico;
        this.idPeca = idPeca;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdServico() { return idServico; }
    public void setIdServico(Integer idServico) { this.idServico = idServico; }

    public Integer getIdPeca() { return idPeca; }
    public void setIdPeca(Integer idPeca) { this.idPeca = idPeca; }

    public Double getQuantidade() { return quantidade; }
    public void setQuantidade(Double quantidade) { this.quantidade = quantidade; }

    public Double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(Double valorUnitario) { this.valorUnitario = valorUnitario; }
}