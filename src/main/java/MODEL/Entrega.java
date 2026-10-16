package MODEL;

public class Entrega {
    private Integer id;
    private Integer idClientes;
    private Integer idBicicletas;
    private Integer idUsuarios;
    private String defeitoRelatado;
    private String descricaoServico;
    private String dataPedido;
    private String dataEntrega;
    private EstadoAtual estadoAtual;
    private Double valorTotal;

    public enum EstadoAtual {
        EM_ANDAMENTO("Em andamento"),
        PAUSADA("Pausada"),
        ESPERANDO_ENTREGA("Esperando entrega"),
        ENTREGUE("Entregue");

        private final String descricao;

        EstadoAtual(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() { return descricao; }
    }

    public Entrega() {}

    public Entrega(Integer id, Integer idClientes, Integer idBicicletas, Integer idUsuarios,
                   String defeitoRelatado, String descricaoServico,
                   String dataPedido, String dataEntrega, EstadoAtual estadoAtual, Double valorTotal) {
        this.id = id;
        this.idClientes = idClientes;
        this.idBicicletas = idBicicletas;
        this.idUsuarios = idUsuarios;
        this.defeitoRelatado = defeitoRelatado;
        this.descricaoServico = descricaoServico;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.estadoAtual = estadoAtual;
        this.valorTotal = valorTotal;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdClientes() { return idClientes; }
    public void setIdClientes(Integer idClientes) { this.idClientes = idClientes; }

    public Integer getIdBicicletas() { return idBicicletas; }
    public void setIdBicicletas(Integer idBicicletas) { this.idBicicletas = idBicicletas; }

    public Integer getIdUsuarios() { return idUsuarios; }
    public void setIdUsuarios(Integer idUsuarios) { this.idUsuarios = idUsuarios; }

    public String getDefeitoRelatado() { return defeitoRelatado; }
    public void setDefeitoRelatado(String defeitoRelatado) { this.defeitoRelatado = defeitoRelatado; }

    public String getDescricaoServico() { return descricaoServico; }
    public void setDescricaoServico(String descricaoServico) { this.descricaoServico = descricaoServico; }

    public String getDataPedido() { return dataPedido; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }

    public String getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(String dataEntrega) { this.dataEntrega = dataEntrega; }

    public EstadoAtual getEstadoAtual() { return estadoAtual; }
    public void setEstadoAtual(EstadoAtual estadoAtual) { this.estadoAtual = estadoAtual; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
}