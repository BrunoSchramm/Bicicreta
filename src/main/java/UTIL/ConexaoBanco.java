package UTIL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBanco {
    private static final String URL = "jdbc:sqlite:bicicreta.db";
    private static Connection conexao = null;

    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver do SQLite não encontrado no classpath!", e);
            }
            conexao = DriverManager.getConnection(URL);
            inicializarTabelas(conexao);
        }
        return conexao;
    }

    private static void inicializarTabelas(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id_usuarios INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "email TEXT NOT NULL UNIQUE, " +
                    "senha TEXT NOT NULL, " +
                    "perfil TEXT NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                    "id_clientes INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "telefone TEXT, " +
                    "email TEXT UNIQUE, " +
                    "endereco TEXT);");

            stmt.execute("CREATE TABLE IF NOT EXISTS bicicletas (" +
                    "id_bicicletas INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_clientes INTEGER NOT NULL, " +
                    "marca_modelo TEXT NOT NULL, " +
                    "cor TEXT, " +
                    "numero_serie TEXT, " +
                    "FOREIGN KEY (id_clientes) REFERENCES clientes(id_clientes));");

            // Tabela para a aba de Produtos
            stmt.execute("CREATE TABLE IF NOT EXISTS bicicreta (" +
                    "id_bicicreta INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "preco REAL NOT NULL, " +
                    "descricao TEXT);");

            stmt.execute("CREATE TABLE IF NOT EXISTS pecas (" +
                    "id_pecas INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "unidade_medida TEXT NOT NULL, " +
                    "quantidade_atual REAL DEFAULT 0, " +
                    "estoque_minimo REAL DEFAULT 0);");

            stmt.execute("CREATE TABLE IF NOT EXISTS servicos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_clientes INTEGER NOT NULL, " +
                    "id_bicicletas INTEGER NOT NULL, " +
                    "id_usuarios INTEGER NOT NULL, " +
                    "defeito_relatado TEXT, " +
                    "descricao_servico TEXT, " +
                    "data_pedido TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "data_entrega TEXT, " +
                    "estado_atual TEXT CHECK(estado_atual IN ('Em andamento', 'Pausada', 'Esperando entrega', 'Entregue')), " +
                    "valor_total REAL DEFAULT 0.0, " +
                    "FOREIGN KEY (id_clientes) REFERENCES clientes(id_clientes), " +
                    "FOREIGN KEY (id_bicicletas) REFERENCES bicicletas(id_bicicletas), " +
                    "FOREIGN KEY (id_usuarios) REFERENCES usuarios(id_usuarios));");

            stmt.execute("CREATE TABLE IF NOT EXISTS servico_pecas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_servico INTEGER NOT NULL, " +
                    "id_peca INTEGER NOT NULL, " +
                    "quantidade REAL NOT NULL, " +
                    "valor_unitario REAL NOT NULL, " +
                    "FOREIGN KEY (id_servico) REFERENCES servicos(id), " +
                    "FOREIGN KEY (id_peca) REFERENCES pecas(id_pecas));");

            stmt.execute("CREATE TABLE IF NOT EXISTS movimentacoes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_pecas INTEGER NOT NULL, " +
                    "id_usuarios INTEGER NOT NULL, " +
                    "tipo TEXT NOT NULL, " +
                    "quantidade REAL NOT NULL, " +
                    "data_movimentacao TEXT NOT NULL, " +
                    "FOREIGN KEY (id_pecas) REFERENCES pecas(id_pecas), " +
                    "FOREIGN KEY (id_usuarios) REFERENCES usuarios(id_usuarios));");

            // Cargas Iniciais
            stmt.execute("INSERT OR IGNORE INTO usuarios (id_usuarios, nome, email, senha, perfil) " +
                    "VALUES (1, 'Akon', 'akon.admin@bicicleta.com', 'tututu', 'ADMIN');");

            stmt.execute("INSERT OR IGNORE INTO clientes (id_clientes, nome, telefone, email, endereco) " +
                    "VALUES (1, 'Ketlyn', '47999998888', 'euAmoMinhaMuie@email.com', 'Rua das Lésbicas 420');");

            stmt.execute("INSERT OR IGNORE INTO bicicletas (id_bicicletas, id_clientes, marca_modelo, cor, numero_serie) " +
                    "VALUES (1, 1, 'BMX', 'Preta', 'SN123456');");

            stmt.execute("INSERT OR IGNORE INTO pecas (id_pecas, nome, unidade_medida, quantidade_atual, estoque_minimo) " +
                    "VALUES (1, 'Câmara de Ar 29', 'UN', 50, 5);");

            stmt.execute("INSERT OR IGNORE INTO pecas (id_pecas, nome, unidade_medida, quantidade_atual, estoque_minimo) " +
                    "VALUES (2, 'Corrente 9v', 'UN', 10, 2);");

            stmt.execute("INSERT OR IGNORE INTO servicos (id, id_clientes, id_bicicletas, id_usuarios, defeito_relatado, descricao_servico, data_pedido, data_entrega, estado_atual, valor_total) " +
                    "VALUES (1, 1, 1, 1, 'Pneu furado e corrente pulando', 'Troca de câmara e regulagem de marcha', '2026-10-13 15:35:00', '2026-10-14 10:00:00', 'Entregue', 120.00);");
        }
    }
}