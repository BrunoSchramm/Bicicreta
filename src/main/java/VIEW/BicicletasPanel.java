package VIEW;

import DAO.BicicletaDao;
import MODEL.Bicicleta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BicicletasPanel extends JPanel {
    private BicicletaDao bicicletaDao = new BicicletaDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    public BicicletasPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{"ID", "ID Cliente", "Marca / Modelo", "Cor", "Nº de Série"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtIdCli = new JTextField(4);
        JTextField txtModelo = new JTextField(12);
        JTextField txtCor = new JTextField(8);
        JTextField txtSerie = new JTextField(10);

        JButton btnAcao = new JButton("Adicionar Bicicleta");
        btnAcao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAcao.setBackground(new Color(40, 110, 90));
        btnAcao.setForeground(Color.WHITE);
        btnAcao.setPreferredSize(new Dimension(165, 36));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setPreferredSize(new Dimension(90, 36));

        formPanel.add(new JLabel("ID Cli:")); formPanel.add(txtIdCli);
        formPanel.add(new JLabel("Modelo:")); formPanel.add(txtModelo);
        formPanel.add(new JLabel("Cor:")); formPanel.add(txtCor);
        formPanel.add(new JLabel("Nº Série:")); formPanel.add(txtSerie);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable resetForm = () -> {
            txtIdCli.setText("");
            txtModelo.setText("");
            txtCor.setText("");
            txtSerie.setText("");
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
                    txtIdCli.setText(model.getValueAt(row, 1).toString());
                    txtModelo.setText(model.getValueAt(row, 2).toString());
                    txtCor.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                    txtSerie.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                    btnAcao.setText("Salvar Alterações");
                    btnAcao.setBackground(new Color(40, 120, 180));
                } else {
                    selectedId = -1;
                    btnAcao.setText("Adicionar Bicicleta");
                    btnAcao.setBackground(new Color(40, 110, 90));
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExcluir = new JButton("Excluir Bicicleta Selecionada");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.setPreferredSize(new Dimension(230, 38));

        btnExcluir.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma bicicleta para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
            for (Bicicleta b : bicicletaDao.listar()) {
                model.addRow(new Object[]{b.getId(), b.getIdCliente(), b.getMarcaModelo(), b.getCor(), b.getNumeroSerie()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}