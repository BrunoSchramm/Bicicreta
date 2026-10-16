package DAO;

import MODEL.Cliente;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id_clientes"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("endereco")
                ));
            }
        }
        return lista;
    }

    public boolean existeDuplicado(String email, String nome, String telefone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE LOWER(email) = LOWER(?) OR (LOWER(nome) = LOWER(?) AND telefone = ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existeEmailEmOutroCliente(String email, int idCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE LOWER(email) = LOWER(?) AND id_clientes != ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void inserir(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (nome, telefone, email, endereco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getTelefone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getEndereco());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET nome=?, telefone=?, email=?, endereco=? WHERE id_clientes=?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getTelefone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getEndereco());
            stmt.setInt(5, c.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_clientes = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }
}