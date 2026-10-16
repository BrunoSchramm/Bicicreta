package DAO;

import MODEL.Produto;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {

    public List<Produto> listar() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM bicicreta";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Produto(
                        rs.getInt("id_bicicreta"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("descricao")
                ));
            }
        }
        return lista;
    }

    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO bicicreta (nome, preco, descricao) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setString(3, p.getDescricao());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE bicicreta SET nome=?, preco=?, descricao=? WHERE id_bicicreta=?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setString(3, p.getDescricao());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idBicicreta) throws SQLException {
        String sql = "DELETE FROM bicicreta WHERE id_bicicreta = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idBicicreta);
            stmt.executeUpdate();
        }
    }
}