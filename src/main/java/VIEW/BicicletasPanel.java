package VIEW;

import DAO.BicicletaDao;
import MODEL.Bicicleta;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BicicletasPanel extends JPanel {

    private BicicletaDao bicicletaDao = new BicicletaDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    private JTextField txtIdCli;
    private JTextField txtModelo;
    private JTextField txtCor;
    private JTextField txtSerie;
    private JButton btnAcao;

    public BicicletasPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- 1. TABELA DE BICICLETAS ---
        model = new DefaultTableModel(new String[]{"ID", "ID Cliente", "Marca / Modelo", "Cor", "Nº de Série"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setBackground(new Color(35, 35, 35));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(55, 55, 55));
        table.setSelectionBackground(new Color(243, 108, 33));
        table.setSelectionForeground(Color.WHITE);

        // Estilo do Cabeçalho da Tabela
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(45, 45, 45));
        table.getTableHeader().setForeground(new Color(243, 108, 33));

        // Centralizar texto nas células
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // --- 2. FORMULÁRIO DE CADASTRO ---
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(new Color(32, 32, 32));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        txtIdCli = criarCampoTexto(4);
        txtModelo = criarCampoTexto(12);
        txtCor = criarCampoTexto(8);
        txtSerie = criarCampoTexto(10);

        btnAcao = new JButton("Adicionar Bicicleta");
        btnAcao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAcao.setBackground(new Color(243, 108, 33));
        btnAcao.setForeground(Color.WHITE);
        btnAcao.setFocusPainted(false);
        btnAcao.setPreferredSize(new Dimension(165, 34));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpar.setBackground(new Color(60, 60, 60));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setPreferredSize(new Dimension(90, 34));

        formPanel.add(criarLabel("ID Cli:")); formPanel.add(txtIdCli);
        formPanel.add(criarLabel("Modelo:")); formPanel.add(txtModelo);
        formPanel.add(criarLabel("Cor:")); formPanel.add(txtCor);
        formPanel.add(criarLabel("Nº Série:")); formPanel.add(txtSerie);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable resetForm = () -> {
            txtIdCli.setText("");
            txtModelo.setText("");
            txtCor.setText("");
            txtSerie.setText("");
            table.clearSelection();
            selectedId = -1;
            btnAcao.setText("Adicionar Bicicleta");
            btnAcao.setBackground(new Color(243, 108, 33));
        };

        btnLimpar.addActionListener(e -> resetForm.run());

        // Evento de Seleção na Tabela
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) model.getValueAt(row, 0);
                    txtIdCli.setText(model.getValueAt(row, 1).toString());
                    txtModelo.setText(model.getValueAt(row, 2).toString());
                    txtCor.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                    txtSerie.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");

                    btnAcao.setText("Salvar Alterações");
                    btnAcao.setBackground(new Color(40, 120, 180));
                } else {
                    resetForm.run();
                }
            }
        });

        btnAcao.addActionListener(e -> {
            try {
                if (txtIdCli.getText().trim().isEmpty() || txtModelo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha o ID do Cliente e o Modelo da Bicicleta!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idCli = Integer.parseInt(txtIdCli.getText().trim());
                String modelo = txtModelo.getText().trim();
                String cor = txtCor.getText().trim();
                String serie = txtSerie.getText().trim();

                Bicicleta b = new Bicicleta(selectedId == -1 ? 0 : selectedId, idCli, modelo, cor, serie);

                if (selectedId == -1) {
                    bicicletaDao.inserir(b);
                    JOptionPane.showMessageDialog(this, "Bicicleta cadastrada com sucesso!");
                } else {
                    bicicletaDao.atualizar(b);
                    JOptionPane.showMessageDialog(this, "Bicicleta atualizada com sucesso!");
                }

                carregar();
                resetForm.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // --- 3. PAINEL INFERIOR (EXCLUSÃO) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnExcluir = new JButton("Excluir Bicicleta Selecionada");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(230, 36));

        btnExcluir.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma bicicleta na tabela para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "Excluir bicicleta ID " + selectedId + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    bicicletaDao.excluir(selectedId);
                    JOptionPane.showMessageDialog(this, "Bicicleta excluída com sucesso!");
                    carregar();
                    resetForm.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                }
            }
        });

        bottomPanel.add(btnExcluir);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(35, 35, 35));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

        scrollPane.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.clearSelection();
            }
        });

        carregar();

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JTextField criarCampoTexto(int colunas) {
        JTextField txt = new JTextField(colunas);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(new Color(45, 45, 45));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return txt;
    }

    public void carregar() {
        model.setRowCount(0);
        try {
            for (Bicicleta b : bicicletaDao.listar()) {
                model.addRow(new Object[]{b.getId(), b.getIdCliente(), b.getMarcaModelo(), b.getCor(), b.getNumeroSerie()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}