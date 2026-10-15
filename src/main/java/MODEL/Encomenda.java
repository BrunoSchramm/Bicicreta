package MODEL;

import java.time.LocalDateTime;
public class Encomenda {
    private Integer id;
    private Integer idClientes;
    private Integer idUsuarios;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private EstadoAtual estadoAtual;
    private Double valorTotal;




    public enum EstadoAtual {
        EM_ANDAMENTO("Em andamento"),
        PAUSADA("Pausada"),
        ESPERANDO_ENTREGA("Esperando entrega");


        private final String descricao;


        EstadoAtual(String descricao) {
            this.descricao = descricao;
        }


        public String getDescricao() {
            return descricao;
        }
    }

    public Encomenda() {
    }

    public Encomenda(Integer id, Integer idClientes, Integer idUsuarios, LocalDateTime dataPedido, LocalDateTime dataEntrega, EstadoAtual estadoAtual, Double valorTotal) {
        this.id = id;
        this.idClientes = idClientes;
        this.idUsuarios = idUsuarios;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.estadoAtual = estadoAtual;
        this.valorTotal = valorTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(Integer idClientes) {
        this.idClientes = idClientes;
    }

    public Integer getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(Integer idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public EstadoAtual getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoAtual estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
