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
                throw new SQLException("Driver do SQLite não encontrado no classpath! Verifique o pom.xml.", e);
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
                    "id_usuarios INTEGER NOT NULL, " +
                    "data_pedido TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "data_entrega TEXT, " +
                    "estado_atual TEXT CHECK(estado_atual IN ('Em andamento', 'Pausada', 'Esperando entrega', 'Entregue')), " +
                    "valor_total REAL, " +
                    "FOREIGN KEY (id_clientes) REFERENCES clientes(id_clientes), " +
                    "FOREIGN KEY (id_usuarios) REFERENCES usuarios(id_usuarios));");

            stmt.execute("CREATE TABLE IF NOT EXISTS movimentacoes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_pecas INTEGER NOT NULL, " +
                    "id_usuarios INTEGER NOT NULL, " +
                    "tipo TEXT NOT NULL, " +
                    "quantidade REAL NOT NULL, " +
                    "data_movimentacao TEXT NOT NULL, " +
                    "FOREIGN KEY (id_pecas) REFERENCES pecas(id_pecas), " +
                    "FOREIGN KEY (id_usuarios) REFERENCES usuarios(id_usuarios));");

            // Cargas iniciais
            stmt.execute("INSERT OR IGNORE INTO usuarios (id_usuarios, nome, email, senha, perfil) " +
                    "VALUES (1, 'Akon', 'akon.admin@bicicleta.com', 'tututu', 'ADMIN');");

            stmt.execute("INSERT OR IGNORE INTO clientes (id_clientes, nome, telefone, email, endereco) " +
                    "VALUES (1, 'Ketlyn', '479', 'euamominhamuie@email.com', 'Rua das Lésbicas, 420');");

            stmt.execute("INSERT OR IGNORE INTO pecas (id_pecas, nome, unidade_medida, quantidade_atual, estoque_minimo) " +
                    "VALUES (1, 'Roda', 'Polegadas', 155, 5);");

            stmt.execute("INSERT OR IGNORE INTO pecas (id_pecas, nome, unidade_medida, quantidade_atual, estoque_minimo) " +
                    "VALUES (2, 'Correia', 'Milimetros', 10, 3);");

            stmt.execute("INSERT OR IGNORE INTO servicos (id, id_clientes, id_usuarios, data_pedido, data_entrega, estado_atual, valor_total) " +
                    "VALUES (1, 1, 1, '2026-10-13 15:35:00', '2027-12-21 01:00:33', 'Entregue', 10.0000);");
        }
    }
}