package VIEW;

import DAO.InsumoDao;
import DAO.MovimentacaoDao;
import MODEL.Insumo;
import MODEL.Movimentacao;
import UTIL.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimentacoesPanel extends JPanel {

    private MovimentacaoDao movDao = new MovimentacaoDao();
    private InsumoDao insumoDao = new InsumoDao();
    private DefaultTableModel model;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MovimentacoesPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        card.setBackground(new Color(32, 32, 32));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JTextField txtIdPeca = criarCampoTexto(4);
        JTextField txtQtd = criarCampoTexto(5);

        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"ENTRADA", "SAIDA"});
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTipo.setBackground(new Color(45, 45, 45));
        cbTipo.setForeground(Color.WHITE);

        JButton btnLancar = new JButton("Registrar Movimentação");
        btnLancar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLancar.setBackground(new Color(243, 108, 33));
        btnLancar.setForeground(Color.WHITE);
        btnLancar.setFocusPainted(false);

        card.add(criarLabel("ID Peça:")); card.add(txtIdPeca);
        card.add(criarLabel("Qtd:")); card.add(txtQtd);
        card.add(criarLabel("Tipo:")); card.add(cbTipo);
        card.add(btnLancar);

        model = new DefaultTableModel(new String[]{"ID Mov.", "ID Peça", "Tipo", "Quantidade", "Data/Hora", "Usuário ID"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
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

        btnLancar.addActionListener(e -> {
            try {
                int idPeca = Integer.parseInt(txtIdPeca.getText().trim());
                int qtd = Integer.parseInt(txtQtd.getText().trim());
                String tipo = (String) cbTipo.getSelectedItem();

                Insumo ins = insumoDao.buscarPorId(idPeca);
                if (ins == null) {
                    JOptionPane.showMessageDialog(this, "Peça não encontrada no estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ("SAIDA".equals(tipo) && (ins.getQuantquantidadeAtual() - qtd < 0)) {
                    JOptionPane.showMessageDialog(this, "Operação Recusada! Estoque insuficiente.\nEstoque Atual: " + ins.getQuantquantidadeAtual(), "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idUser = SessaoUsuario.getUsuarioLogado() != null ? SessaoUsuario.getUsuarioLogado().getId() : 1;
                movDao.registrarMovimentacao(new Movimentacao(0, idPeca, idUser, tipo, qtd, LocalDateTime.now()));

                carregar();
                txtIdPeca.setText("");
                txtQtd.setText("");
                JOptionPane.showMessageDialog(this, "Movimentação registrada com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(35, 35, 35));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

        carregar();

        add(card, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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

    public void carregar() {
        model.setRowCount(0);
        try {
            for (Movimentacao m : movDao.listar()) {
                String dataFormatada = m.getData_movimentacao() != null ? m.getData_movimentacao().format(dtf) : "";
                model.addRow(new Object[]{
                        m.getId(),
                        m.getIdInsumo(),
                        m.getTipo(),
                        m.getQuantidade(),
                        dataFormatada,
                        m.getIdUsuarios()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}