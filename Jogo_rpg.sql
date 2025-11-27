-- ==========================================
--  RECRIAR BANCO DO RPG DO ZERO
-- ==========================================

DROP DATABASE IF EXISTS rpg_game2;
CREATE DATABASE rpg_game2;
USE rpg_game2;

-- ==========================================
--  APAGAR TABELAS ANTIGAS (se existirem)
--  (na ordem correta para evitar FK error)
-- ==========================================

DROP TABLE IF EXISTS inventario;
DROP TABLE IF EXISTS missao_jogador;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS missao;
DROP TABLE IF EXISTS jogador;

-- ==========================================
--  TABELA DO JOGADOR
-- ==========================================

CREATE TABLE jogador (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    vida INT DEFAULT 100,
    ataque INT DEFAULT 10,
    experiencia INT DEFAULT 0,
    capacidade_inventario INT DEFAULT 5,
    mapa_atual INT DEFAULT 1,
    missao_atual INT DEFAULT 1,
    horario VARCHAR(20)
);

-- ==========================================
--  TABELA DE ITENS
-- ==========================================

CREATE TABLE item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    tipo VARCHAR(50)
);

-- ==========================================
--  TABELA DE INVENTÁRIO
-- ==========================================

CREATE TABLE inventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    jogador_id INT,
    item_id INT,
    FOREIGN KEY (jogador_id) REFERENCES jogador(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);

-- ==========================================
--  TABELA DE MISSÕES
-- ==========================================

CREATE TABLE missao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(255) NOT NULL
);


-- popula as 5 missões
INSERT INTO missao (id, descricao) VALUES
(1, 'Sair do mapa 1'),
(2, 'Sair do mapa 2'),
(3, 'Sair do mapa 3'),
(4, 'Sair do mapa 4'),
(5, 'Sair do mapa 5')
ON DUPLICATE KEY UPDATE descricao = VALUES(descricao);

-- ==========================================
--  TABELA MISSÃO DO JOGADOR (progresso)
-- ==========================================

CREATE TABLE missao_jogador (
    id INT PRIMARY KEY AUTO_INCREMENT,
    jogador_id INT NOT NULL,
    missao_id INT NOT NULL,
    concluida BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (jogador_id) REFERENCES jogador(id) ON DELETE CASCADE,
    FOREIGN KEY (missao_id) REFERENCES missao(id) ON DELETE CASCADE
);

-- ==========================================
--  Criar item padrão "Poção de Cura"
-- ==========================================

INSERT INTO item (nome, tipo)
VALUES ('Poção de Cura', 'cura')
ON DUPLICATE KEY UPDATE tipo='cura';

-- ==========================================
--  MOSTRAR TABELAS
-- ==========================================

