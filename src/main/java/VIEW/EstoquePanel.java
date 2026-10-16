package VIEW;

import DAO.InsumoDao;
import MODEL.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        JTextField txtUnidade = new JTextField(4);
        JTextField txtQtd = new JTextField(4);
        JTextField txtMin = new JTextField(4);

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

        Runnable resetForm = () -> {
            txtNome.setText("");
            txtUnidade.setText("");
            txtQtd.setText("");
            txtMin.setText("");
            table.clearSelection();
        };

        btnLimpar.addActionListener(e -> resetForm.run());

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (table.rowAtPoint(e.getPoint()) == -1) table.clearSelection();
            }
        });

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
                    selectedId = -1;
                    btnAcao.setText("Adicionar Peça");
                    btnAcao.setBackground(new Color(40, 110, 90));
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
                    resetForm.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                }
            }
        });
        bottomPanel.add(btnExcluir);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { table.clearSelection(); }
        });

        carregar();
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void carregar() {
        model.setRowCount(0);
        try {
            for (Insumo i : insumoDao.listar()) {
                model.addRow(new Object[]{i.getId(), i.getNome(), i.getUnidadeMedida(), i.getQuantquantidadeAtual(), i.getEstoqueMinimo()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}