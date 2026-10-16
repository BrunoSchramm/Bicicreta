package DAO;

import MODEL.Usuario;
import UTIL.ConexaoBanco;
import UTIL.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    public boolean autenticar(String email, String senha) throws SQLException {
        Usuario user = buscarPorEmail(email);
        if (user != null) {
            return PasswordUtils.verificarSenha(senha, user.getSenha());
        }
        return false;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE LOWER(email) = LOWER(?)";
        Connection conn = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuarios"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("perfil")
                    );
                }
            }
        }
        return null;
    }
}