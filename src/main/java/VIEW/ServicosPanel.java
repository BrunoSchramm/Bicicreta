package VIEW;

import DAO.EncomendaDao;
import MODEL.Entrega;
import UTIL.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServicosPanel extends JPanel {
    private EncomendaDao encomendaDao = new EncomendaDao();
    private DefaultTableModel model;

    public ServicosPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{"ID", "Cliente ID", "Data Pedido", "Data Entrega", "Estado Atual", "Valor Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Em andamento", "Pausada", "Esperando entrega", "Entregue"});
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cbStatus));

        model.addTableModelListener(e -> {
            if (e.getColumn() == 4 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                int id = (int) model.getValueAt(row, 0);
                String st = model.getValueAt(row, 4).toString();
                try { encomendaDao.atualizarStatus(id, st); } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtIdCli = new JTextField("1", 4);
        JTextField txtDataPed = new JTextField("2026-10-13", 8);
        JTextField txtDataEnt = new JTextField("2026-10-20", 8);
        JTextField txtValor = new JTextField("100.00", 6);
        JButton btnCriar = new JButton("Criar Serviço/Entrega");
        btnCriar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        form.add(new JLabel("ID Cliente:")); form.add(txtIdCli);
        form.add(new JLabel("Data Pedido:")); form.add(txtDataPed);
        form.add(new JLabel("Data Entrega:")); form.add(txtDataEnt);
        form.add(new JLabel("Valor:")); form.add(txtValor);
        form.add(btnCriar);

        btnCriar.addActionListener(e -> {
            try {
                int idCli = Integer.parseInt(txtIdCli.getText());
                double v = Double.parseDouble(txtValor.getText());
                int idU = SessaoUsuario.getUsuarioLogado() != null ? SessaoUsuario.getUsuarioLogado().getId() : 1;
                encomendaDao.inserir(new Entrega(0, idCli, idU, txtDataPed.getText(), txtDataEnt.getText(), Entrega.EstadoAtual.EM_ANDAMENTO, v));
                JOptionPane.showMessageDialog(this, "Serviço registrado com sucesso!");
                carregar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExcluir = new JButton("Excluir Serviço Selecionado");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.setPreferredSize(new Dimension(250, 38));

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

        carregar();
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void carregar() {
        model.setRowCount(0);
        try {
            for (Entrega e : encomendaDao.listar()) {
                model.addRow(new Object[]{
                        e.getId(),
                        e.getIdClientes(),
                        e.getDataPedido(),
                        e.getDataEntrega(),
                        e.getEstadoAtual() != null ? e.getEstadoAtual().getDescricao() : "",
                        "R$ " + String.format("%.2f", e.getValorTotal())
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}