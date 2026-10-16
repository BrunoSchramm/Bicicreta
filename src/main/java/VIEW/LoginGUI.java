package VIEW;

import DAO.UsuarioDao;
import MODEL.Usuario;
import UTIL.SessaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoginGUI extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JLabel lblMensagem;

    public LoginGUI() {
        setTitle("Fiasco de Bike - Login");
        setSize(420, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel Principal com fundo Escuro (#181818)
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 24, 24));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // --- 1. LOGO DA OFICINA ---
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon icon = carregarLogo();
        if (icon != null) {
            lblLogo.setIcon(icon);
        } else {
            // Texto alternativo caso a imagem logo.png não seja encontrada
            lblLogo.setText("FIASCO DE BIKE");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblLogo.setForeground(new Color(243, 108, 33)); // Laranja
        }

        JLabel lblSubtitulo = new JLabel("SISTEMA DE GESTÃO DE OFICINA");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSubtitulo.setForeground(new Color(170, 170, 170));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 2. FORMULÁRIO DE LOGIN ---
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridLayout(4, 1, 5, 6));
        formPanel.setMaximumSize(new Dimension(330, 170));

        JLabel lblEmail = new JLabel("E-mail do Operador:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEmail.setForeground(Color.WHITE);

        txtEmail = new JTextField("akon.admin@bicicleta.com"); // E-mail padrão
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBackground(new Color(40, 40, 40));
        txtEmail.setForeground(Color.WHITE);
        txtEmail.setCaretColor(Color.WHITE);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSenha.setForeground(Color.WHITE);

        txtSenha = new JPasswordField("tututu"); // Senha padrão
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBackground(new Color(40, 40, 40));
        txtSenha.setForeground(Color.WHITE);
        txtSenha.setCaretColor(Color.WHITE);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblSenha);
        formPanel.add(txtSenha);

        // --- 3. BOTÃO DE ENTRADA ---
        JButton btnLogin = new JButton("ENTRAR");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(new Color(243, 108, 33)); // Laranja Lançamento Fiasco
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(330, 42));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        lblMensagem = new JLabel(" ");
        lblMensagem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMensagem.setForeground(new Color(255, 90, 90));
        lblMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ações do botão e tecla Enter
        btnLogin.addActionListener(e -> efetuarLogin());
        txtSenha.addActionListener(e -> efetuarLogin());

        // Adicionando elementos com espaçamento
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(lblSubtitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(lblMensagem);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(btnLogin);

        add(mainPanel);
    }

    private ImageIcon carregarLogo() {
        try {
            java.net.URL imgURL = getClass().getResource("/logo.png");
            if (imgURL == null) {
                imgURL = getClass().getResource("logo.png");
            }

            Image img = null;
            if (imgURL != null) {
                img = new ImageIcon(imgURL).getImage();
            } else if (new File("logo.png").exists()) {
                img = new ImageIcon("logo.png").getImage();
            }

            if (img != null) {
                Image scaled = img.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Retorna null se não encontrar o arquivo de imagem
    }

    private void efetuarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha o e-mail e a senha!");
            return;
        }

        try {
            UsuarioDao dao = new UsuarioDao();
            // Verifica a autenticação que retorna boolean (true/false)
            boolean autenticado = dao.autenticar(email, senha);

            if (autenticado) {
                // Cria o usuário logado para manter na sessão do sistema
                Usuario user = new Usuario();
                user.setId(1);
                user.setNome("Akon");
                user.setEmail(email);
                user.setPerfil("ADMIN");

                SessaoUsuario.setUsuarioLogado(user);
                dispose();
                new MainFrame().setVisible(true);
            } else {
                lblMensagem.setText("E-mail ou senha incorretos!");
            }
        } catch (Exception ex) {
            lblMensagem.setText("Erro ao conectar ao banco de dados!");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}