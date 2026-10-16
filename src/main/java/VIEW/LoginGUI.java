package VIEW;

import DAO.UsuarioDao;
import MODEL.Usuario;
import UTIL.SessaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginGUI extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioDao usuarioDao = new UsuarioDao();

    public LoginGUI() {
        setTitle("Oficina Bicicleta - Autenticação");
        setSize(500, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("BICICLETA S.A.", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 110, 90));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("E-mail:"), gbc);

        txtEmail = new JTextField("akon.admin@bicicleta.com", 18);
        txtEmail.setPreferredSize(new Dimension(220, 35));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Palavra-passe:"), gbc);

        txtSenha = new JPasswordField("tututu", 18);
        txtSenha.setPreferredSize(new Dimension(220, 35));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtSenha, gbc);

        JButton btnLogin = new JButton("Entrar no Sistema");
        btnLogin.setBackground(new Color(40, 110, 90));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setPreferredSize(new Dimension(200, 42));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> efetuarLogin());
        add(panel);
    }

    private void efetuarLogin() {
        try {
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());
            if (usuarioDao.autenticar(email, senha)) {
                Usuario u = usuarioDao.buscarPorEmail(email);
                SessaoUsuario.setUsuarioLogado(u);
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Banco: " + ex.getMessage());
        }
    }
}