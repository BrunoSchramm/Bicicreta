package VIEW;

import DAO.InsumoDao;
import MODEL.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EstoquePanel extends JPanel {

    private InsumoDao insumoDao = new InsumoDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    private JTextField txtNome;
    private JTextField txtUnidade;
    private JTextField txtQtd;
    private JTextField txtMin;
    private JButton btnAcao;

    public EstoquePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{"ID Peça", "Nome Peça", "Unidade", "Qtd Atual", "Estoque Mínimo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        estilitarTabela(table);

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(new Color(32, 32, 32));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        txtNome = criarCampoTexto(10);
        txtUnidade = criarCampoTexto(4);
        txtQtd = criarCampoTexto(4);
        txtMin = criarCampoTexto(4);

        btnAcao = new JButton("Adicionar Peça");
        btnAcao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAcao.setBackground(new Color(243, 108, 33));
        btnAcao.setForeground(Color.WHITE);
        btnAcao.setFocusPainted(false);
        btnAcao.setPreferredSize(new Dimension(150, 34));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpar.setBackground(new Color(60, 60, 60));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setPreferredSize(new Dimension(90, 34));

        formPanel.add(criarLabel("Nome:")); formPanel.add(txtNome);
        formPanel.add(criarLabel("Unid:")); formPanel.add(txtUnidade);
        formPanel.add(criarLabel("Qtd:")); formPanel.add(txtQtd);
        formPanel.add(criarLabel("Min:")); formPanel.add(txtMin);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable resetForm = () -> {
            txtNome.setText("");
            txtUnidade.setText("");
            txtQtd.setText("");
            txtMin.setText("");
            table.clearSelection();
            selectedId = -1;
            btnAcao.setText("Adicionar Peça");
            btnAcao.setBackground(new Color(243, 108, 33));
        };

        btnLimpar.addActionListener(e -> resetForm.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) model.getValueAt(row, 0);
                    txtNome.setText(model.getValueAt(row, 1).toString());
                    txtUnidade.setText(model.getValueAt(row, 2).toString());
                    txtQtd.setText(model.getValueAt(row, 3).toString());
                    txtMin.setText(model.getValueAt(row, 4).toString());

                    btnAcao.setText("Salvar Alterações");
                    btnAcao.setBackground(new Color(40, 120, 180));
                } else {
                    resetForm.run();
                }
            }
        });

        btnAcao.addActionListener(e -> {
            try {
                if (txtNome.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha o nome da peça!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int qtd = Integer.parseInt(txtQtd.getText().trim());
                int min = Integer.parseInt(txtMin.getText().trim());

                Insumo i = new Insumo(selectedId == -1 ? 0 : selectedId, txtNome.getText().trim(), txtUnidade.getText().trim(), qtd, min);

                if (selectedId == -1) {
                    insumoDao.inserir(i);
                    JOptionPane.showMessageDialog(this, "Peça cadastrada com sucesso!");
                } else {
                    insumoDao.atualizar(i);
                    JOptionPane.showMessageDialog(this, "Peça atualizada com sucesso!");
                }

                carregar();
                resetForm.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnExcluir = new JButton("Excluir Peça Selecionada");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(210, 36));

        btnExcluir.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma peça para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "Excluir peça ID " + selectedId + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    insumoDao.excluir(selectedId);
                    JOptionPane.showMessageDialog(this, "Peça excluída com sucesso!");
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

    private void estilitarTabela(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setBackground(new Color(35, 35, 35));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(55, 55, 55));
        table.setSelectionBackground(new Color(243, 108, 33));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(45, 45, 45));
        table.getTableHeader().setForeground(new Color(243, 108, 33));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    public void carregar() {
        model.setRowCount(0);
        try {
            for (Insumo i : insumoDao.listar()) {
                model.addRow(new Object[]{i.getId(), i.getNome(), i.getUnidadeMedida(), i.getQuantquantidadeAtual(), i.getEstoqueMinimo()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}