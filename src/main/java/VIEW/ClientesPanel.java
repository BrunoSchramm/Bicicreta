package VIEW;

import DAO.ClienteDao;
import MODEL.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientesPanel extends JPanel {
    private ClienteDao clienteDao = new ClienteDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    public ClientesPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{"ID", "Nome", "Telefone", "E-mail", "Endereço"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtNome = new JTextField(10);
        JTextField txtTel = new JTextField(8);
        JTextField txtEmail = new JTextField(10);
        JTextField txtEnd = new JTextField(12);

        JButton btnAcao = new JButton("Adicionar Cliente");
        btnAcao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAcao.setBackground(new Color(40, 110, 90));
        btnAcao.setForeground(Color.WHITE);
        btnAcao.setPreferredSize(new Dimension(165, 36));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setPreferredSize(new Dimension(90, 36));

        formPanel.add(new JLabel("Nome:")); formPanel.add(txtNome);
        formPanel.add(new JLabel("Tel:")); formPanel.add(txtTel);
        formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);
        formPanel.add(new JLabel("End:")); formPanel.add(txtEnd);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable reset = () -> {
            table.clearSelection();
            selectedId = -1;
            txtNome.setText(""); txtTel.setText(""); txtEmail.setText(""); txtEnd.setText("");
            btnAcao.setText("Adicionar Cliente");
            btnAcao.setBackground(new Color(40, 110, 90));
        };

        btnLimpar.addActionListener(e -> reset.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = (int) model.getValueAt(row, 0);
                txtNome.setText(model.getValueAt(row, 1).toString());
                txtTel.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
                txtEmail.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                txtEnd.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                btnAcao.setText("Salvar Alterações");
                btnAcao.setBackground(new Color(40, 120, 180));
            }
        });

        btnAcao.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                String tel = txtTel.getText().trim();
                String email = txtEmail.getText().trim();
                String end = txtEnd.getText().trim();

                if (nome.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha pelo menos o Nome e o E-mail!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (selectedId == -1) {
                    if (clienteDao.existeDuplicado(email, nome, tel)) {
                        JOptionPane.showMessageDialog(this, "Cliente já registrado com este E-mail ou Nome/Telefone!", "Duplicado", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    clienteDao.inserir(new Cliente(0, nome, tel, email, end));
                    JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                } else {
                    if (clienteDao.existeEmailEmOutroCliente(email, selectedId)) {
                        JOptionPane.showMessageDialog(this, "Este E-mail já pertence a outro cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    clienteDao.atualizar(new Cliente(selectedId, nome, tel, email, end));
                    JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
                }
                carregar();
                reset.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExcluir = new JButton("Excluir Cliente Selecionado");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.setPreferredSize(new Dimension(230, 38));

        btnExcluir.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente ID " + selectedId + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    clienteDao.excluir(selectedId);
                    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                    carregar();
                    reset.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage());
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
            for (Cliente c : clienteDao.listar()) {
                model.addRow(new Object[]{c.getId(), c.getNome(), c.getTelefone(), c.getEmail(), c.getEndereco()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}