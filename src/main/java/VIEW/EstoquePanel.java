package VIEW;

import DAO.InsumoDao;
import MODEL.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EstoquePanel extends JPanel {
    private InsumoDao insumoDao = new InsumoDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    public EstoquePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{"ID Peça", "Nome Peça", "Unidade", "Qtd Atual", "Estoque Mínimo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtNome = new JTextField(10);
        JTextField txtUnidade = new JTextField("UN", 4);
        JTextField txtQtd = new JTextField("10", 4);
        JTextField txtMin = new JTextField("3", 4);

        JButton btnAcao = new JButton("Adicionar Peça");
        btnAcao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAcao.setBackground(new Color(40, 110, 90));
        btnAcao.setForeground(Color.WHITE);
        btnAcao.setPreferredSize(new Dimension(160, 36));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setPreferredSize(new Dimension(90, 36));

        formPanel.add(new JLabel("Nome:")); formPanel.add(txtNome);
        formPanel.add(new JLabel("Unid:")); formPanel.add(txtUnidade);
        formPanel.add(new JLabel("Qtd:")); formPanel.add(txtQtd);
        formPanel.add(new JLabel("Mín:")); formPanel.add(txtMin);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable reset = () -> {
            table.clearSelection();
            selectedId = -1;
            txtNome.setText(""); txtUnidade.setText("UN"); txtQtd.setText("10"); txtMin.setText("3");
            btnAcao.setText("Adicionar Peça");
            btnAcao.setBackground(new Color(40, 110, 90));
        };

        btnLimpar.addActionListener(e -> reset.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = (int) model.getValueAt(row, 0);
                txtNome.setText(model.getValueAt(row, 1).toString());
                txtUnidade.setText(model.getValueAt(row, 2).toString());
                txtQtd.setText(model.getValueAt(row, 3).toString());
                txtMin.setText(model.getValueAt(row, 4).toString());
                btnAcao.setText("Salvar Alterações");
                btnAcao.setBackground(new Color(40, 120, 180));
            }
        });

        btnAcao.addActionListener(e -> {
            try {
                int qtd = Integer.parseInt(txtQtd.getText().trim());
                int min = Integer.parseInt(txtMin.getText().trim());

                if (qtd < 0 || min < 0) {
                    JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Insumo i = new Insumo(selectedId == -1 ? 0 : selectedId, txtNome.getText().trim(), txtUnidade.getText().trim(), qtd, min);
                if (selectedId == -1) {
                    insumoDao.inserir(i);
                    JOptionPane.showMessageDialog(this, "Peça cadastrada com sucesso!");
                } else {
                    insumoDao.atualizar(i);
                    JOptionPane.showMessageDialog(this, "Peça atualizada com sucesso!");
                }
                carregar();
                reset.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExcluir = new JButton("Excluir Peça Selecionada");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.setPreferredSize(new Dimension(230, 38));

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
                    reset.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                }
            }
        });
        bottomPanel.add(btnExcluir);

        carregar();
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void carregar() {
        model.setRowCount(0);
        try {
            for (Insumo i : insumoDao.listar()) {
                model.addRow(new Object[]{i.getId(), i.getNome(), i.getUnidadeMedida(), i.getQuantquantidadeAtual(), i.getEstoqueMinimo()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}