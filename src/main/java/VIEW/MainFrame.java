package VIEW;

import UTIL.SessaoUsuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Gestão de Bicicletas - Painel Principal");
        setSize(1200, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(240, 242, 245));
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        String nomeResp = (SessaoUsuario.getUsuarioLogado() != null) ? SessaoUsuario.getUsuarioLogado().getNome() : "Administrador";
        JLabel lblUsuario = new JLabel("Painel de Controlo - Operador: " + nomeResp);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnDeslogar = new JButton("Deslogar");
        btnDeslogar.setBackground(new Color(180, 50, 50));
        btnDeslogar.setForeground(Color.WHITE);
        btnDeslogar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDeslogar.addActionListener(e -> {
            SessaoUsuario.setUsuarioLogado(null);
            dispose();
            new LoginGUI().setVisible(true);
        });

        topBar.add(lblUsuario, BorderLayout.WEST);
        topBar.add(btnDeslogar, BorderLayout.EAST);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));

        tabbedPane.addTab("Clientes", new ClientesPanel());
        tabbedPane.addTab("Estoque", new EstoquePanel());
        tabbedPane.addTab("Movimentações", new MovimentacoesPanel());
        tabbedPane.addTab("Produtos", new ProdutosPanel());
        tabbedPane.addTab("Serviços", new ServicosPanel());
        tabbedPane.addTab("Status", new StatusPanel());

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
}