package VIEW;

import DAO.ClienteDao;
import MODEL.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientesPanel extends JPanel {

    private ClienteDao clienteDao = new ClienteDao();
    private DefaultTableModel model;
    private int selectedId = -1;

    private JTextField txtNome;
    private JTextField txtTel;
    private JTextField txtEmail;
    private JTextField txtEnd;
    private JButton btnAcao;

    public ClientesPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- TABELA ---
        model = new DefaultTableModel(new String[]{"ID", "Nome", "Telefone", "E-mail", "Endereço"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        estilizarTabela(table);

        // --- FORMULÁRIO ---
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(new Color(32, 32, 32));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        txtNome = criarCampoTexto(10);
        txtTel = criarCampoTexto(8);
        txtEmail = criarCampoTexto(10);
        txtEnd = criarCampoTexto(12);

        btnAcao = new JButton("Adicionar Cliente");
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
        formPanel.add(criarLabel("Tel:")); formPanel.add(txtTel);
        formPanel.add(criarLabel("Email:")); formPanel.add(txtEmail);
        formPanel.add(criarLabel("End:")); formPanel.add(txtEnd);
        formPanel.add(btnAcao);
        formPanel.add(btnLimpar);

        Runnable resetForm = () -> {
            txtNome.setText("");
            txtTel.setText("");
            txtEmail.setText("");
            txtEnd.setText("");
            table.clearSelection();
            selectedId = -1;
            btnAcao.setText("Adicionar Cliente");
            btnAcao.setBackground(new Color(243, 108, 33));
        };

        btnLimpar.addActionListener(e -> resetForm.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) model.getValueAt(row, 0);
                    txtNome.setText(model.getValueAt(row, 1).toString());
                    txtTel.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
                    txtEmail.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                    txtEnd.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");

                    btnAcao.setText("Salvar Alterações");
                    btnAcao.setBackground(new Color(40, 120, 180));
                } else {
                    resetForm.run();
                }
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
                        JOptionPane.showMessageDialog(this, "Cliente já registrado com este E-mail ou Nome/Telefone!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                resetForm.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // --- PAINEL INFERIOR ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnExcluir = new JButton("Excluir Cliente Selecionado");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(220, 36));

        btnExcluir.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "Excluir cliente ID " + selectedId + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    clienteDao.excluir(selectedId);
                    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
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

    private void estilizarTabela(JTable table) {
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
            for (Cliente c : clienteDao.listar()) {
                model.addRow(new Object[]{c.getId(), c.getNome(), c.getTelefone(), c.getEmail(), c.getEndereco()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}