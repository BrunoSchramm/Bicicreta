package VIEW;

import DAO.EncomendaDao;
import DAO.InsumoDao;
import MODEL.Entrega;
import MODEL.Insumo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StatusPanel extends JPanel {
    private EncomendaDao encomendaDao = new EncomendaDao();
    private InsumoDao insumoDao = new InsumoDao();

    private DefaultTableModel modelEntregas;
    private DefaultTableModel modelEstoque;

    public StatusPanel() {
        setLayout(new GridLayout(2, 1, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tabela Status Geral de Serviços - NÃO EDITÁVEL
        JPanel panelEntregas = new JPanel(new BorderLayout());
        JLabel lblEntregas = new JLabel("Status Geral de Serviços e Entregas");
        lblEntregas.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelEntregas.add(lblEntregas, BorderLayout.NORTH);

        modelEntregas = new DefaultTableModel(new String[]{"ID", "Cliente ID", "Data Entrega", "Estado Atual", "Valor"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableEntregas = new JTable(modelEntregas);
        panelEntregas.add(new JScrollPane(tableEntregas), BorderLayout.CENTER);

        // Tabela Alertas de Estoque Crítico - NÃO EDITÁVEL
        JPanel panelEstoque = new JPanel(new BorderLayout());
        JLabel lblEstoque = new JLabel("Alertas de Estoque de Peças Crítico");
        lblEstoque.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstoque.setForeground(new Color(180, 50, 50));
        panelEstoque.add(lblEstoque, BorderLayout.NORTH);

        modelEstoque = new DefaultTableModel(new String[]{"ID Peça", "Nome Peça", "Qtd Atual", "Estoque Mínimo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableEstoque = new JTable(modelEstoque);
        panelEstoque.add(new JScrollPane(tableEstoque), BorderLayout.CENTER);

        add(panelEntregas);
        add(panelEstoque);

        carregarDados();
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
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}