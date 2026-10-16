package DAO;

import MODEL.Movimentacao;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDao {

    public List<Movimentacao> listar() throws SQLException {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes ORDER BY id DESC";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String strData = rs.getString("data_movimentacao");
                LocalDateTime dataLoc = (strData != null) ? LocalDateTime.parse(strData.replace(" ", "T")) : null;

                lista.add(new Movimentacao(
                        rs.getInt("id"),
                        rs.getInt("id_pecas"),
                        rs.getInt("id_usuarios"),
                        rs.getString("tipo"),
                        (int) rs.getDouble("quantidade"),
                        dataLoc
                ));
            }
        }
        return lista;
    }

    public void registrarMovimentacao(Movimentacao mov) throws SQLException {
        Connection conn = ConexaoBanco.getConexao();
        String sqlStock = "ENTRADA".equalsIgnoreCase(mov.getTipo()) ?
                "UPDATE pecas SET quantidade_atual = quantidade_atual + ? WHERE id_pecas = ?" :
                "UPDATE pecas SET quantidade_atual = quantidade_atual - ? WHERE id_pecas = ?";

        String sqlMov = "INSERT INTO movimentacoes (id_pecas, id_usuarios, tipo, quantidade, data_movimentacao) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmtStock = conn.prepareStatement(sqlStock);
             PreparedStatement stmtMov = conn.prepareStatement(sqlMov)) {

            stmtStock.setDouble(1, mov.getQuantidade());
            stmtStock.setInt(2, mov.getIdInsumo());
            stmtStock.executeUpdate();

            stmtMov.setInt(1, mov.getIdInsumo());
            stmtMov.setInt(2, mov.getIdUsuarios());
            stmtMov.setString(3, mov.getTipo());
            stmtMov.setDouble(4, mov.getQuantidade());
            stmtMov.setString(5, mov.getData_movimentacao() != null ? mov.getData_movimentacao().toString() : LocalDateTime.now().toString());
            stmtMov.executeUpdate();
        }
    }
}