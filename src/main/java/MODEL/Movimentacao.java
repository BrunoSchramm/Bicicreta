package MODEL;

import java.time.LocalDateTime;

public class Movimentacao {

   private Integer id;
    private Integer idInsumo;
    private Integer idUsuarios;
    private String tipo;
    private Integer quantidade;
    private LocalDateTime data_movimentacao;

    public Movimentacao() {
    }

    public Movimentacao(Integer id, Integer idInsumo, Integer idUsuarios, String tipo, Integer quantidade, LocalDateTime data_movimentacao) {
        this.id = id;
        this.idInsumo = idInsumo;
        this.idUsuarios = idUsuarios;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data_movimentacao = data_movimentacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public Integer getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(Integer idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getData_movimentacao() {
        return data_movimentacao;
    }

    public void setData_movimentacao(LocalDateTime data_movimentacao) {
        this.data_movimentacao = data_movimentacao;
    }
}
