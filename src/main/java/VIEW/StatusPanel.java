package VIEW;

import DAO.EncomendaDao;
import DAO.InsumoDao;
import MODEL.Entrega;
import MODEL.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StatusPanel extends JPanel {

    private EncomendaDao encomendaDao = new EncomendaDao();
    private InsumoDao insumoDao = new InsumoDao();

    private DefaultTableModel modelEntregas;
    private DefaultTableModel modelEstoque;

    public StatusPanel() {
        setLayout(new GridLayout(2, 1, 15, 15));
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- PAINEL STATUS GERAL DE SERVIÇOS ---
        JPanel panelEntregas = new JPanel(new BorderLayout(5, 5));
        panelEntregas.setOpaque(false);

        JLabel lblEntregas = new JLabel("Status Geral de Serviços e Entregas");
        lblEntregas.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEntregas.setForeground(new Color(243, 108, 33));
        panelEntregas.add(lblEntregas, BorderLayout.NORTH);

        modelEntregas = new DefaultTableModel(new String[]{"ID", "Cliente ID", "Data Entrega", "Estado Atual", "Valor Total"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tableEntregas = new JTable(modelEntregas);
        estilizarTabela(tableEntregas);

        JScrollPane scrollEntregas = new JScrollPane(tableEntregas);
        scrollEntregas.getViewport().setBackground(new Color(35, 35, 35));
        scrollEntregas.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        panelEntregas.add(scrollEntregas, BorderLayout.CENTER);

        // --- PAINEL ALERTAS DE ESTOQUE CRÍTICO ---
        JPanel panelEstoque = new JPanel(new BorderLayout(5, 5));
        panelEstoque.setOpaque(false);

        JLabel lblEstoque = new JLabel("Alertas de Estoque de Peças Crítico");
        lblEstoque.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstoque.setForeground(new Color(255, 90, 90));
        panelEstoque.add(lblEstoque, BorderLayout.NORTH);

        modelEstoque = new DefaultTableModel(new String[]{"ID Peça", "Nome Peça", "Qtd Atual", "Estoque Mínimo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tableEstoque = new JTable(modelEstoque);
        estilizarTabela(tableEstoque);

        JScrollPane scrollEstoque = new JScrollPane(tableEstoque);
        scrollEstoque.getViewport().setBackground(new Color(35, 35, 35));
        scrollEstoque.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        panelEstoque.add(scrollEstoque, BorderLayout.CENTER);

        add(panelEntregas);
        add(panelEstoque);

        carregarDados();
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

    public void carregarDados() {
        modelEntregas.setRowCount(0);
        modelEstoque.setRowCount(0);
        try {
            for (Entrega e : encomendaDao.listar()) {
                modelEntregas.addRow(new Object[]{
                        e.getId(),
                        e.getIdClientes(),
                        e.getDataEntrega(),
                        e.getEstadoAtual() != null ? e.getEstadoAtual().getDescricao() : "",
                        "R$ " + String.format("%.2f", e.getValorTotal())
                });
            }

            for (Insumo i : insumoDao.listarCriticos()) {
                modelEstoque.addRow(new Object[]{
                        i.getId(),
                        i.getNome(),
                        i.getQuantquantidadeAtual(),
                        i.getEstoqueMinimo()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}