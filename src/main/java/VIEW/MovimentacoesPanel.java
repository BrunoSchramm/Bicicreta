package VIEW;

import DAO.InsumoDao;
import DAO.MovimentacaoDao;
import MODEL.Insumo;
import MODEL.Movimentacao;
import UTIL.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class MovimentacoesPanel extends JPanel {
    private MovimentacaoDao movDao = new MovimentacaoDao();
    private InsumoDao insumoDao = new InsumoDao();
    private DefaultTableModel model;

    public MovimentacoesPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtIdPeca = new JTextField("1", 4);
        JTextField txtQtd = new JTextField("5", 5);
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"ENTRADA", "SAIDA"});
        JButton btnLancar = new JButton("Registrar Movimentação");

        card.add(new JLabel("ID Peça:")); card.add(txtIdPeca);
        card.add(new JLabel("Qtd:")); card.add(txtQtd);
        card.add(new JLabel("Tipo:")); card.add(cbTipo);
        card.add(btnLancar);

        model = new DefaultTableModel(new String[]{"ID Mov.", "ID Peça", "Tipo", "Quantidade", "Data/Hora", "Usuário ID"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

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
                    JOptionPane.showMessageDialog(this, "Operação Recusada! Estoque insuficiente para esta saída.\nEstoque Atual: " + ins.getQuantquantidadeAtual(), "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idUser = SessaoUsuario.getUsuarioLogado() != null ? SessaoUsuario.getUsuarioLogado().getId() : 1;
                movDao.registrarMovimentacao(new Movimentacao(0, idPeca, idUser, tipo, qtd, LocalDateTime.now()));
                carregar();
                JOptionPane.showMessageDialog(this, "Movimentação registrada com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        carregar();
        add(card, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void carregar() {
        model.setRowCount(0);
        try {
            for (Movimentacao m : movDao.listar()) {
                model.addRow(new Object[]{
                        m.getId(),
                        m.getIdInsumo(),
                        m.getTipo(),
                        m.getQuantidade(),
                        m.getData_movimentacao() != null ? m.getData_movimentacao().toString().replace("T", " ") : "",
                        m.getIdUsuarios()
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}