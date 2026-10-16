package DAO;

import MODEL.ServicoPeca;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicoPecaDao {

    public void adicionarPecaAoServico(ServicoPeca sp, int idUsuario) throws SQLException {
        Connection conn = ConexaoBanco.getConexao();

        String sqlInsertItem = "INSERT INTO servico_pecas (id_servico, id_peca, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        String sqlBaixaEstoque = "UPDATE pecas SET quantidade_atual = quantidade_atual - ? WHERE id_pecas = ?";
        String sqlMovimentacao = "INSERT INTO movimentacoes (id_pecas, id_usuarios, tipo, quantidade, data_movimentacao) VALUES (?, ?, 'SAIDA', ?, ?)";
        String sqlAtualizaTotal = "UPDATE servicos SET valor_total = valor_total + (? * ?) WHERE id = ?";

        try (PreparedStatement stmtItem = conn.prepareStatement(sqlInsertItem);
             PreparedStatement stmtEstoque = conn.prepareStatement(sqlBaixaEstoque);
             PreparedStatement stmtMov = conn.prepareStatement(sqlMovimentacao);
             PreparedStatement stmtTotal = conn.prepareStatement(sqlAtualizaTotal)) {

            // 1. Lança a peça no serviço
            stmtItem.setInt(1, sp.getIdServico());
            stmtItem.setInt(2, sp.getIdPeca());
            stmtItem.setDouble(3, sp.getQuantidade());
            stmtItem.setDouble(4, sp.getValorUnitario());
            stmtItem.executeUpdate();

            // 2. Dar baixa no estoque
            stmtEstoque.setDouble(1, sp.getQuantidade());
            stmtEstoque.setInt(2, sp.getIdPeca());
            stmtEstoque.executeUpdate();

            // 3. Registrar movimentação de saída
            stmtMov.setInt(1, sp.getIdPeca());
            stmtMov.setInt(2, idUsuario);
            stmtMov.setDouble(3, sp.getQuantidade());
            stmtMov.setString(4, LocalDateTime.now().toString());
            stmtMov.executeUpdate();

            // 4. Somar o valor da peça no valor total do serviço
            stmtTotal.setDouble(1, sp.getQuantidade());
            stmtTotal.setDouble(2, sp.getValorUnitario());
            stmtTotal.setInt(3, sp.getIdServico());
            stmtTotal.executeUpdate();
        }
    }

    public List<ServicoPeca> listarPorServico(int idServico) throws SQLException {
        List<ServicoPeca> lista = new ArrayList<>();
        String sql = "SELECT * FROM servico_pecas WHERE id_servico = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idServico);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ServicoPeca(
                            rs.getInt("id"),
                            rs.getInt("id_servico"),
                            rs.getInt("id_peca"),
                            rs.getDouble("quantidade"),
                            rs.getDouble("valor_unitario")
                    ));
                }
            }
        }
        return lista;
    }
}