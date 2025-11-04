SET SQL_SAFE_UPDATES = 0;

DROP DATABASE IF EXISTS jogo_rpg;
CREATE DATABASE jogo_rpg;
USE jogo_rpg;

-- remover tabelas antigas se existirem
DROP TABLE IF EXISTS batalha;
DROP TABLE IF EXISTS inventario;
DROP TABLE IF EXISTS mapa;
DROP TABLE IF EXISTS loot;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS inimigo;
DROP TABLE IF EXISTS personagem;

-- =========================
-- TABELAS
-- =========================

CREATE TABLE personagem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    vida INT NOT NULL,
    ataque INT NOT NULL,
    nivel INT NOT NULL DEFAULT 1,
    experiencia INT NOT NULL DEFAULT 0,
    experiencia_proximo_nivel INT NOT NULL DEFAULT 100
);

CREATE TABLE inimigo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    vida INT NOT NULL,
    ataque INT NOT NULL,
    experiencia_drop INT NOT NULL
);

CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo ENUM('cura','ataque','defesa','outro') NOT NULL,
    valor_ouro INT NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE loot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inimigo_id INT NOT NULL,
    item_id INT NOT NULL,
    chance DECIMAL(3,2) NOT NULL CHECK (chance >= 0 AND chance <= 1),
    FOREIGN KEY (inimigo_id) REFERENCES inimigo(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);

CREATE TABLE mapa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    linha INT NOT NULL,
    coluna INT NOT NULL,
    tipo ENUM('vazio','inimigo','item','saida') NOT NULL,
    inimigo_id INT,
    item_id INT,
    FOREIGN KEY (inimigo_id) REFERENCES inimigo(id),
    FOREIGN KEY (item_id) REFERENCES item(id)
);

CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personagem_id INT NOT NULL,
    item_id INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    FOREIGN KEY (personagem_id) REFERENCES personagem(id),
    FOREIGN KEY (item_id) REFERENCES item(id)
);

CREATE TABLE batalha (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personagem_id INT NOT NULL,
    inimigo_id INT NOT NULL,
    resultado ENUM('vitoria','derrota') NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (personagem_id) REFERENCES personagem(id),
    FOREIGN KEY (inimigo_id) REFERENCES inimigo(id)
);

-- =========================
-- INSERÇÕES (pelo menos 3 por tabela)
-- =========================

INSERT INTO personagem (nome, vida, ataque, nivel, experiencia)
VALUES
('Herói', 30, 5, 1, 0),
('Guerreiro', 40, 6, 2, 120),
('Mago', 25, 8, 3, 240);

INSERT INTO inimigo (nome, vida, ataque, experiencia_drop)
VALUES
('Goblin', 20, 4, 30),
('Lobo Selvagem', 25, 6, 45),
('Esqueleto', 35, 7, 60);

INSERT INTO item (nome, tipo, valor_ouro, descricao)
VALUES
('Poção de Cura', 'cura', 20, 'Recupera 20 pontos de vida'),
('Espada Curta', 'ataque', 50, 'Aumenta o ataque em +2'),
('Escudo Pequeno', 'defesa', 40, 'Reduz o dano recebido em 2 pontos'),
('Saco de Miçangas', 'outro', 5, 'Item trivial - será deletado no exemplo');

INSERT INTO loot (inimigo_id, item_id, chance)
VALUES
(1, 1, 0.40),
(2, 2, 0.25),
(3, 3, 0.30);

-- mapa 5x5 exemplo (linhas/colunas iniciando em 1)
INSERT INTO mapa (linha, coluna, tipo, inimigo_id, item_id)
VALUES
(1,1,'vazio',NULL,NULL),
(1,2,'item',NULL,1),
(1,3,'inimigo',1,NULL),
(1,4,'vazio',NULL,NULL),
(1,5,'saida',NULL,NULL),
(2,1,'inimigo',2,NULL),
(2,2,'vazio',NULL,NULL),
(2,3,'item',NULL,2),
(2,4,'vazio',NULL,NULL),
(2,5,'inimigo',3,NULL),
(3,1,'vazio',NULL,NULL),
(3,2,'vazio',NULL,NULL),
(3,3,'vazio',NULL,NULL),
(3,4,'item',NULL,3),
(3,5,'vazio',NULL,NULL),
(4,1,'vazio',NULL,NULL),
(4,2,'vazio',NULL,NULL),
(4,3,'inimigo',1,NULL),
(4,4,'vazio',NULL,NULL),
(4,5,'vazio',NULL,NULL),
(5,1,'vazio',NULL,NULL),
(5,2,'vazio',NULL,NULL),
(5,3,'vazio',NULL,NULL),
(5,4,'item',NULL,1),
(5,5,'saida',NULL,NULL);

INSERT INTO inventario (personagem_id, item_id, quantidade)
VALUES
(1,1,2),
(1,2,1),
(2,3,1);

-- =========================
-- UPDATES / DELETES (pelo menos 2)
-- =========================

-- exemplo: dar XP globalmente
UPDATE personagem SET experiencia = experiencia + 50;

-- exemplo: deletar itens do tipo 'outro' (apaga a instância 'Saco de Miçangas')
DELETE FROM item WHERE tipo = 'outro';

-- =========================
-- ALTER e DROP (exemplo)
-- =========================

ALTER TABLE personagem ADD COLUMN mana INT DEFAULT 50;

-- exemplo de DROP e recriação de loot (mostra uso de DROP)
DROP TABLE IF EXISTS loot;

CREATE TABLE loot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inimigo_id INT NOT NULL,
    item_id INT NOT NULL,
    chance DECIMAL(3,2) NOT NULL CHECK (chance >= 0 AND chance <= 1),
    FOREIGN KEY (inimigo_id) REFERENCES inimigo(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);

INSERT INTO loot (inimigo_id, item_id, chance)
VALUES
(1,1,0.40),
(2,2,0.25),
(3,3,0.30);

-- =========================
-- VIEW, FUNCTION, PROCEDURE, TRIGGER (3+ estruturas)
-- =========================

CREATE OR REPLACE VIEW vw_personagem_status AS
SELECT
    id,
    nome,
    nivel,
    experiencia,
    experiencia_proximo_nivel,
    ROUND((experiencia / experiencia_proximo_nivel) * 100, 1) AS progresso_percentual
FROM personagem;

-- FUNCTION: XP necessária para próximo nível
DELIMITER $$
CREATE FUNCTION xp_necessaria(nivel_atual INT)
RETURNS INT
DETERMINISTIC
BEGIN
    RETURN nivel_atual * 100;
END$$
DELIMITER ;

-- PROCEDURE: registra uma batalha (útil para integrar com Java)
DELIMITER $$
CREATE PROCEDURE registrar_batalha(
    IN p_personagem INT,
    IN p_inimigo INT,
    IN p_resultado ENUM('vitoria','derrota')
)
BEGIN
    INSERT INTO batalha (personagem_id, inimigo_id, resultado)
    VALUES (p_personagem, p_inimigo, p_resultado);
END$$
DELIMITER ;

-- TRIGGER: sobe de nível automaticamente quando experiencia >= experiencia_proximo_nivel
DELIMITER $$
CREATE TRIGGER trigger_subir_nivel
AFTER UPDATE ON personagem
FOR EACH ROW
BEGIN
    IF NEW.experiencia >= NEW.experiencia_proximo_nivel THEN
        UPDATE personagem
        SET
            nivel = NEW.nivel + 1,
            vida = NEW.vida + 10,
            ataque = NEW.ataque + 2,
            experiencia = 0,
            experiencia_proximo_nivel = NEW.experiencia_proximo_nivel + 100
        WHERE id = NEW.id;
    END IF;
END$$
DELIMITER ;

-- =========================
-- USUÁRIO E PERMISSÕES
-- =========================

DROP USER IF EXISTS 'jogador'@'localhost';
CREATE USER 'jogador'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT, INSERT, UPDATE, DELETE ON jogo_rpg.* TO 'jogador'@'localhost';
FLUSH PRIVILEGES;
