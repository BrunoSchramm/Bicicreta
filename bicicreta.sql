CREATE USER 'bicicreta'@'localhost' IDENTIFIED BY 'qwerty';
GRANT ALL PRIVILEGES ON *.* TO 'bicicreta'@'localhost' WITH GRANT OPTION;

create database bicicreta;
use bicicreta;

-- 1. TABELA DE USUÁRIOS
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuarios INTEGER PRIMARY KEY auto_increment,
    nome varchar(75) NOT NULL, -- Por causa do "Aliaune Damala Bouga Time Puru Nacka Lu Lu Lu Badara Akon Thiam"
    email varchar(75) NOT NULL UNIQUE,
    senha varchar(30) NOT NULL,
    perfil varchar(30) NOT NULL
);

-- 2. TABELA DE CLIENTES
CREATE TABLE IF NOT EXISTS clientes (
    id_clientes INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome varchar(75) NOT NULL,
    telefone TEXT,
    email varchar(75) UNIQUE,
    endereco TEXT
);

-- 3. TABELA DE PRODUTOS
CREATE TABLE IF NOT EXISTS bicicreta (
    id_bicicreta INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome varchar(40) NOT NULL,
    preco REAL NOT NULL,
    descricao TEXT
);

-- 4. TABELA DE INSUMOS (MATÉRIA-PRIMA)
CREATE TABLE IF NOT EXISTS pecas (
    id_pecas INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome varchar(40) NOT NULL,
    unidade_medida INTEGER NOT NULL,
    quantidade_atual INTEGER DEFAULT 0,
    estoque_minimo INTEGER DEFAULT 0
);

-- 5. TABELA DE ENCOMENDAS
CREATE TABLE IF NOT EXISTS servicos (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    id_clientes INTEGER NOT NULL,
    id_usuarios INTEGER NOT NULL,
    data_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_entrega DATE,
    estado_atual enum('Em andamento','Pausada','Esperando entrega'),
    valor_total decimal,
    FOREIGN KEY (id_clientes) REFERENCES clientes(id_clientes),
    FOREIGN KEY (id_usuarios) REFERENCES usuarios(id_usuarios)
);

-- 6. TABELA DE MOVIMENTAÇÕES DE ESTOQUE
CREATE TABLE IF NOT EXISTS movimentacoes (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    id_pecas INTEGER NOT NULL,
    id_usuarios INTEGER NOT NULL,
    tipo varchar(25) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_movimentacao DATE NOT NULL,
    FOREIGN KEY (id_pecas) REFERENCES pecas(id_pecas),
    FOREIGN KEY (id_usuarios) REFERENCES usuarios(id_usuarios)
);

drop table servicos; -- 1
drop table movimentacoes; -- 2
drop table bicicreta; -- 3
drop table clientes; -- 4
drop table pecas; -- 5
drop table servicos; -- 6
drop table usuarios; -- 7