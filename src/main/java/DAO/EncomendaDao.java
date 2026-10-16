package DAO;

import MODEL.Entrega;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EncomendaDao {

    private Entrega.EstadoAtual converterEstado(String estadoStr) {
        if (estadoStr == null) return null;
        for (Entrega.EstadoAtual estado : Entrega.EstadoAtual.values()) {
            if (estado.getDescricao().equalsIgnoreCase(estadoStr) || estado.name().equalsIgnoreCase(estadoStr)) {
                return estado;
            }
        }
        return null;
    }

    public List<Entrega> listar() throws SQLException {
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT * FROM servicos";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_clientes"),
                        rs.getInt("id_usuarios"),
                        rs.getString("data_pedido"),
                        rs.getString("data_entrega"),
                        converterEstado(rs.getString("estado_atual")),
                        rs.getDouble("valor_total")
                ));
            }
        }
        return lista;
    }

    public void inserir(Entrega e) throws SQLException {
        String sql = "INSERT INTO servicos (id_clientes, id_usuarios, data_pedido, data_entrega, estado_atual, valor_total) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, e.getIdClientes());
            stmt.setInt(2, e.getIdUsuarios());
            stmt.setString(3, e.getDataPedido());
            stmt.setString(4, e.getDataEntrega());
            stmt.setString(5, e.getEstadoAtual() != null ? e.getEstadoAtual().getDescricao() : null);
            stmt.setDouble(6, e.getValorTotal());
            stmt.executeUpdate();
        }
    }

    public void atualizarStatus(int id, String status) throws SQLException {
        String sql = "UPDATE servicos SET estado_atual = ? WHERE id = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM servicos WHERE id = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}