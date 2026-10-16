package DAO;

import MODEL.Bicicleta;
import UTIL.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BicicletaDao {

    public List<Bicicleta> listar() throws SQLException {
        List<Bicicleta> lista = new ArrayList<>();
        String sql = "SELECT * FROM bicicletas";
        try (Statement stmt = ConexaoBanco.getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Bicicleta(
                        rs.getInt("id_bicicletas"),
                        rs.getInt("id_clientes"),
                        rs.getString("marca_modelo"),
                        rs.getString("cor"),
                        rs.getString("numero_serie")
                ));
            }
        }
        return lista;
    }

    public List<Bicicleta> listarPorCliente(int idCliente) throws SQLException {
        List<Bicicleta> lista = new ArrayList<>();
        String sql = "SELECT * FROM bicicletas WHERE id_clientes = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Bicicleta(
                            rs.getInt("id_bicicletas"),
                            rs.getInt("id_clientes"),
                            rs.getString("marca_modelo"),
                            rs.getString("cor"),
                            rs.getString("numero_serie")
                    ));
                }
            }
        }
        return lista;
    }

    public void inserir(Bicicleta b) throws SQLException {
        String sql = "INSERT INTO bicicletas (id_clientes, marca_modelo, cor, numero_serie) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, b.getIdCliente());
            stmt.setString(2, b.getMarcaModelo());
            stmt.setString(3, b.getCor());
            stmt.setString(4, b.getNumeroSerie());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Bicicleta b) throws SQLException {
        String sql = "UPDATE bicicletas SET marca_modelo=?, cor=?, numero_serie=? WHERE id_bicicletas=?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setString(1, b.getMarcaModelo());
            stmt.setString(2, b.getCor());
            stmt.setString(3, b.getNumeroSerie());
            stmt.setInt(4, b.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idBicicleta) throws SQLException {
        String sql = "DELETE FROM bicicletas WHERE id_bicicletas = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, idBicicleta);
            stmt.executeUpdate();
        }
    }
}