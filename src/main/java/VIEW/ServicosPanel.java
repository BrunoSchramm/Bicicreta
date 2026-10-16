package VIEW;

import DAO.EncomendaDao;
import MODEL.Entrega;
import UTIL.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServicosPanel extends JPanel {

    private EncomendaDao encomendaDao = new EncomendaDao();
    private DefaultTableModel model;

    public ServicosPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{
                "ID", "Cliente ID", "Bicicleta ID", "Defeito", "Data Pedido", "Data Entrega", "Estado Atual", "Valor Total"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 6; // Apenas a coluna "Estado Atual" pode ser editada
            }
        };

        JTable table = new JTable(model);
        estilizarTabela(table);

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Em andamento", "Pausada", "Esperando entrega", "Entregue"});
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStatus.setBackground(new Color(45, 45, 45));
        cbStatus.setForeground(Color.WHITE);

        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(cbStatus));

        model.addTableModelListener(e -> {
            if (e.getColumn() == 6 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                int id = (int) model.getValueAt(row, 0);
                String st = model.getValueAt(row, 6).toString();
                try {
                    encomendaDao.atualizarStatus(id, st);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // --- FORMULÁRIO ---
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.setBackground(new Color(32, 32, 32));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JTextField txtIdCli = criarCampoTexto(3);
        JTextField txtIdBici = criarCampoTexto(3);
        JTextField txtDefeito = criarCampoTexto(9);
        JTextField txtDesc = criarCampoTexto(9);
        JTextField txtDataPed = criarCampoTexto(7);
        JTextField txtDataEnt = criarCampoTexto(7);
        JTextField txtValor = criarCampoTexto(5);

        JButton btnCriar = new JButton("Criar Serviço");
        btnCriar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCriar.setBackground(new Color(243, 108, 33));
        btnCriar.setForeground(Color.WHITE);
        btnCriar.setFocusPainted(false);

        form.add(criarLabel("ID Cli:")); form.add(txtIdCli);
        form.add(criarLabel("ID Bici:")); form.add(txtIdBici);
        form.add(criarLabel("Defeito:")); form.add(txtDefeito);
        form.add(criarLabel("Descrição:")); form.add(txtDesc);
        form.add(criarLabel("Data Ped:")); form.add(txtDataPed);
        form.add(criarLabel("Data Ent:")); form.add(txtDataEnt);
        form.add(criarLabel("Valor:")); form.add(txtValor);
        form.add(btnCriar);

        btnCriar.addActionListener(e -> {
            try {
                int idCli = Integer.parseInt(txtIdCli.getText().trim());
                int idBici = Integer.parseInt(txtIdBici.getText().trim());
                double v = Double.parseDouble(txtValor.getText().trim());
                int idu = SessaoUsuario.getUsuarioLogado() != null ? SessaoUsuario.getUsuarioLogado().getId() : 1;

                encomendaDao.inserir(new Entrega(
                        0, idCli, idBici, idu,
                        txtDefeito.getText().trim(), txtDesc.getText().trim(),
                        txtDataPed.getText().trim(), txtDataEnt.getText().trim(),
                        Entrega.EstadoAtual.EM_ANDAMENTO, v
                ));

                JOptionPane.showMessageDialog(this, "Serviço registrado com sucesso!");
                carregar();

                txtIdCli.setText(""); txtIdBici.setText(""); txtDefeito.setText("");
                txtDesc.setText(""); txtDataPed.setText(""); txtDataEnt.setText(""); txtValor.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // --- PAINEL INFERIOR ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnExcluir = new JButton("Excluir Serviço Selecionado");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(230, 36));

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um serviço na tabela para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idServico = (int) model.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(this, "Excluir serviço ID " + idServico + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    encomendaDao.excluir(idServico);
                    JOptionPane.showMessageDialog(this, "Serviço excluído com sucesso!");
                    carregar();
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

        add(form, BorderLayout.NORTH);
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
            for (Entrega e : encomendaDao.listar()) {
                model.addRow(new Object[]{
                        e.getId(),
                        e.getIdClientes(),
                        e.getIdBicicletas(),
                        e.getDefeitoRelatado(),
                        e.getDataPedido(),
                        e.getDataEntrega(),
                        e.getEstadoAtual() != null ? e.getEstadoAtual().getDescricao() : "",
                        "R$ " + String.format("%.2f", e.getValorTotal())
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}