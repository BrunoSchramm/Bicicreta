package VIEW;

import DAO.ProdutoDao;
import MODEL.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProdutosPanel extends JPanel {
    private ProdutoDao produtoDao = new ProdutoDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    public ProdutosPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{"ID", "Nome Bicicleta", "Preço", "Descrição"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtNome = new JTextField(12);
        JTextField txtPreco = new JTextField(6);
        JTextField txtDesc = new JTextField(14);

        JButton btnAcao = new JButton("Adicionar Bicicleta");
        btnAcao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAcao.setBackground(new Color(40, 110, 90));
        btnAcao.setForeground(Color.WHITE);
        btnAcao.setPreferredSize(new Dimension(165, 36));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setPreferredSize(new Dimension(90, 36));

        formPanel.add(new JLabel("Nome:")); formPanel.add(txtNome);
        formPanel.add(new JLabel("Preço:")); formPanel.add(txtPreco);
        formPanel.add(new JLabel("Descrição:")); formPanel.add(txtDesc);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable reset = () -> {
            table.clearSelection();
            selectedId = -1;
            txtNome.setText(""); txtPreco.setText(""); txtDesc.setText("");
            btnAcao.setText("Adicionar Bicicleta");
            btnAcao.setBackground(new Color(40, 110, 90));
        };

        btnLimpar.addActionListener(e -> reset.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = (int) model.getValueAt(row, 0);
                txtNome.setText(model.getValueAt(row, 1).toString());
                txtPreco.setText(model.getValueAt(row, 2).toString().replace("R$ ", "").replace(",", "."));
                txtDesc.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                btnAcao.setText("Salvar Alterações");
                btnAcao.setBackground(new Color(40, 120, 180));
            }
        });

        btnAcao.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                double preco = Double.parseDouble(txtPreco.getText().trim());
                String desc = txtDesc.getText().trim();

                if (selectedId == -1) {
                    produtoDao.inserir(new Produto(0, nome, preco, desc));
                    JOptionPane.showMessageDialog(this, "Bicicleta registrada com sucesso!");
                } else {
                    produtoDao.atualizar(new Produto(selectedId, nome, preco, desc));
                    JOptionPane.showMessageDialog(this, "Bicicleta atualizada com sucesso!");
                }
                carregar();
                reset.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExcluir = new JButton("Excluir Bicicleta Selecionada");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.setPreferredSize(new Dimension(230, 38));

        btnExcluir.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um item para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Excluir bicicleta ID " + selectedId + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    produtoDao.excluir(selectedId);
                    JOptionPane.showMessageDialog(this, "Bicicleta excluída com sucesso!");
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
            for (Produto p : produtoDao.listar()) {
                model.addRow(new Object[]{p.getId(), p.getNome(), "R$ " + String.format("%.2f", p.getPreco()), p.getDescricao()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}