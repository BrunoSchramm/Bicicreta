package VIEW;

import DAO.EncomendaDao;
import MODEL.Entrega;
import UTIL.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServicosPanel extends JPanel {
    private EncomendaDao encomendaDao = new EncomendaDao();
    private DefaultTableModel model;

    public ServicosPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        model = new DefaultTableModel(new String[]{
                "ID", "Cliente ID", "Bicicleta ID", "Defeito", "Data Pedido", "Data Entrega", "Estado Atual", "Valor Total"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Em andamento", "Pausada", "Esperando entrega", "Entregue"});
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(cbStatus));

        model.addTableModelListener(e -> {
            if (e.getColumn() == 6 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                int id = (int) model.getValueAt(row, 0);
                String st = model.getValueAt(row, 6).toString();
                try { encomendaDao.atualizarStatus(id, st); } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JTextField txtIdCli = new JTextField(3);
        JTextField txtIdBici = new JTextField(3);
        JTextField txtDefeito = new JTextField(10);
        JTextField txtDesc = new JTextField(10);
        JTextField txtDataPed = new JTextField(7);
        JTextField txtDataEnt = new JTextField(7);
        JTextField txtValor = new JTextField(5);
        JButton btnCriar = new JButton("Criar Serviço/Entrega");
        btnCriar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        form.add(new JLabel("ID Cli:")); form.add(txtIdCli);
        form.add(new JLabel("ID Bici:")); form.add(txtIdBici);
        form.add(new JLabel("Defeito:")); form.add(txtDefeito);
        form.add(new JLabel("Descrição:")); form.add(txtDesc);
        form.add(new JLabel("Data Ped:")); form.add(txtDataPed);
        form.add(new JLabel("Data Ent:")); form.add(txtDataEnt);
        form.add(new JLabel("Valor:")); form.add(txtValor);
        form.add(btnCriar);

        btnCriar.addActionListener(e -> {
            try {
                int idCli = Integer.parseInt(txtIdCli.getText().trim());
                int idBici = Integer.parseInt(txtIdBici.getText().trim());
                double v = Double.parseDouble(txtValor.getText().trim());
                int idU = SessaoUsuario.getUsuarioLogado() != null ? SessaoUsuario.getUsuarioLogado().getId() : 1;

                encomendaDao.inserir(new Entrega(
                        0, idCli, idBici, idU,
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { table.clearSelection(); }
        });

        carregar();
        add(form, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}