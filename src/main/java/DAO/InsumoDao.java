package DAO;

import MODEL.Insumo;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoDao {

    public List<Insumo> listar() throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM pecas";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Insumo(
                        rs.getInt("id_pecas"),
                        rs.getString("nome"),
                        rs.getString("unidade_medida"),
                        (int) rs.getDouble("quantidade_atual"),
                        (int) rs.getDouble("estoque_minimo")
                ));
            }
        }
        return lista;
    }

    public List<Insumo> listarCriticos() throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM pecas WHERE quantidade_atual <= estoque_minimo";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Insumo(
                        rs.getInt("id_pecas"),
                        rs.getString("nome"),
                        rs.getString("unidade_medida"),
                        (int) rs.getDouble("quantidade_atual"),
                        (int) rs.getDouble("estoque_minimo")
                ));
            }
        }
        return lista;
    }

    public Insumo buscarPorId(int idPeca) throws SQLException {
        String sql = "SELECT * FROM pecas WHERE id_pecas = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idPeca);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Insumo(
                            rs.getInt("id_pecas"),
                            rs.getString("nome"),
                            rs.getString("unidade_medida"),
                            (int) rs.getDouble("quantidade_atual"),
                            (int) rs.getDouble("estoque_minimo")
                    );
                }
            }
        }
        return null;
    }

    public void inserir(Insumo i) throws SQLException {
        String sql = "INSERT INTO pecas (nome, unidade_medida, quantidade_atual, estoque_minimo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, i.getNome());
            stmt.setString(2, i.getUnidadeMedida());
            stmt.setDouble(3, i.getQuantquantidadeAtual());
            stmt.setDouble(4, i.getEstoqueMinimo());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Insumo i) throws SQLException {
        String sql = "UPDATE pecas SET nome=?, unidade_medida=?, quantidade_atual=?, estoque_minimo=? WHERE id_pecas=?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, i.getNome());
            stmt.setString(2, i.getUnidadeMedida());
            stmt.setDouble(3, i.getQuantquantidadeAtual());
            stmt.setDouble(4, i.getEstoqueMinimo());
            stmt.setInt(5, i.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idPeca) throws SQLException {
        String sql = "DELETE FROM pecas WHERE id_pecas = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idPeca);
            stmt.executeUpdate();
        }
    }
}