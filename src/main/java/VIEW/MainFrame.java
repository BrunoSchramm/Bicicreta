package VIEW;

import UTIL.SessaoUsuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Fiasco de Bike - Painel de Controle");
        setSize(1200, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ajustar cores globais para dar consistência escura no Swing
        UIManager.put("TabbedPane.selected", new Color(243, 108, 33));
        UIManager.put("TabbedPane.contentAreaColor", new Color(24, 24, 24));

        // --- 1. BARRA SUPERIOR (TOP BAR) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(32, 32, 32));
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        String nomeResp = (SessaoUsuario.getUsuarioLogado() != null) ? SessaoUsuario.getUsuarioLogado().getNome() : "Operador";

        JLabel lblTitulo = new JLabel("FIASCO DE BIKE | ");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(243, 108, 33)); // Laranja Fiasco

        JLabel lblUsuario = new JLabel("Operador: " + nomeResp);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsuario.setForeground(Color.WHITE);

        JPanel pnlHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlHeaderLeft.setOpaque(false);
        pnlHeaderLeft.add(lblTitulo);
        pnlHeaderLeft.add(lblUsuario);

        JButton btnDeslogar = new JButton("Deslogar");
        btnDeslogar.setBackground(new Color(180, 50, 50));
        btnDeslogar.setForeground(Color.WHITE);
        btnDeslogar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDeslogar.setFocusPainted(false);
        btnDeslogar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnDeslogar.addActionListener(e -> {
            SessaoUsuario.setUsuarioLogado(null);
            dispose();
            new LoginGUI().setVisible(true);
        });

        topBar.add(pnlHeaderLeft, BorderLayout.WEST);
        topBar.add(btnDeslogar, BorderLayout.EAST);

        // --- 2. ABAS NAVEGÁVEIS (JTABBEDPANE) ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(40, 40, 40));
        tabbedPane.setForeground(Color.WHITE);

        // Instanciar os painéis
        ClientesPanel clientesPanel = new ClientesPanel();
        BicicletasPanel bicicletasPanel = new BicicletasPanel();
        EstoquePanel estoquePanel = new EstoquePanel();
        MovimentacoesPanel movimentacoesPanel = new MovimentacoesPanel();
        ServicosPanel servicosPanel = new ServicosPanel();
        StatusPanel statusPanel = new StatusPanel();

        // Adicionar as abas
        tabbedPane.addTab("Clientes", clientesPanel);
        tabbedPane.addTab("Bicicletas", bicicletasPanel);
        tabbedPane.addTab("Estoque Peças", estoquePanel);
        tabbedPane.addTab("Movimentações", movimentacoesPanel);
        tabbedPane.addTab("Serviços", servicosPanel);
        tabbedPane.addTab("Status", statusPanel);

        // Recarregar os dados do painel visível ao mudar de aba
        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected == clientesPanel) clientesPanel.carregar();
            else if (selected == bicicletasPanel) bicicletasPanel.carregar();
            else if (selected == estoquePanel) estoquePanel.carregar();
            else if (selected == movimentacoesPanel) movimentacoesPanel.carregar();
            else if (selected == servicosPanel) servicosPanel.carregar();
            else if (selected == statusPanel) statusPanel.carregarDados();
        });

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
}