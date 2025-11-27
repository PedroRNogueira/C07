
# 📘 README — Jogo RPG Console + Banco de Dados (CRUD + JOINs)

Bem-vindo ao projeto **Jogo RPG Console + Sistema de Banco de Dados**!  
Este documento explica **como jogar**, **como usar o menu do banco de dados**, **como funciona cada componente**, além da **estrutura completa do projeto**.

---

# 📌 1. Sobre o Projeto

Este é um jogo RPG simples em Java que integra:

✔ Mapa com movimentação  
✔ Sistema de batalhas  
✔ Inventário persistente  
✔ Missões progressivas (1 a 5)  
✔ Sistema de SAVE automático  
✔ Banco de Dados com CRUD completo  
✔ JOINs mostram inventário + missões do jogador  
✔ Menu do jogo + Menu Administrativo feito com SELECT, UPDATE, INSERT e DELETE  

O objetivo é **explorar mapas, enfrentar inimigos e concluir as 5 missões**.

---

# 🎮 2. Como Jogar

Quando você inicia o jogo (opção 1 no menu principal), ocorre:

### 1️⃣ Criar Jogador
Você informa um nome, e o sistema cria:
- Vida inicial: 100
- Ataque inicial: 10
- Missão inicial: 1
- Mapa inicial: 1

### 2️⃣ Entrar no mapa
O jogador aparece no centro do mapa, representado por:

```
X  (você)
.  (chão)
```

### 3️⃣ Controles

| Tecla | Ação |
|------|------|
| **W** | mover para cima |
| **S** | mover para baixo |
| **A** | mover para esquerda |
| **D** | mover para direita |
| **I** | abrir inventário |
| **Q** | sair do jogo e voltar ao menu |

---

# 🗺️ 3. Mapas e Missões

O jogo possui **5 mapas** (Mapa 1 até Mapa 5).  
Cada mapa corresponde a **uma missão**:

| Mapa | Missão |
|------|--------|
| 1 | Sair do mapa 1 |
| 2 | Sair do mapa 2 |
| 3 | Sair do mapa 3 |
| 4 | Sair do mapa 4 |
| 5 | Sair do mapa 5 (final) |

### ✔ Ao sair do mapa:
- Missão atual é concluída no banco
- Avança para a próxima missão
- Avança para o próximo mapa
- SAVE automático:
  - mapa_atual
  - missao_atual
  - vida
  - inventário
- Se concluir todas, apresenta mensagem de parabéns 🎉

---

# ⚔️ 4. Batalhas

Ao andar no mapa existe chance de:

| Chance | Evento |
|--------|--------|
| 40% | Encontrar poção |
| 40% | Batalha contra Goblin |
| 20% | Nada acontece |

Batalhas são **em turnos**:
- Jogador ataca
- Inimigo ataca
- Continua até alguém morrer

Morrer retorna ao menu principal.

---

# 🎒 5. Inventário

Cada jogador tem **limite de 5 itens**.

Itens são salvos no BD via tabela `inventario`.

Tipos de itens:

1. **Poção de Cura**
2. **Item desconhecido (adicionado pelo menu BD)**

### ✔ Ao usar um item:
- Se for poção → cura 30 de vida  
- Se for item desconhecido → mensagem:  
  `"Eu não sei o que isso faz..."`  
  E o item é descartado.

---

# 🗄️ 6. Menu do Banco de Dados (CRUD Completo)

A opção **3 - Menu do Banco de Dados** abre um painel administrativo com:

---

## 🔹 **1 — CRUD JOGADOR**
- Inserir jogador (gera missões automaticamente)
- Atualizar jogador
- Deletar jogador
- Listar jogadores

Jogador possui:
- id  
- nome  
- vida  
- ataque  
- mapa_atual  
- missao_atual  

---

## 🔹 **2 — CRUD ITENS**
- Inserir item  
- Listar itens  
- Atualizar item  
- Deletar item  
  (também apaga vínculos com inventário)

---

## 🔹 **3 — CRUD INVENTÁRIO**
Permite:

- Adicionar item ao jogador
- Listar itens do jogador
- Atualizar item (trocar itemA → itemB)
- Remover item do inventário

---

## 🔹 **4 — CRUD MISSÕES**
Permite:

- Inserir missão  
- Listar missões  
- Atualizar missão  
- Deletar missão  

---

## 🔹 **5 — JOINS Completos**
### Joins mostram:

### ✔ (17) Jogadores + MISSÃO ATUAL
Usa subconsulta para encontrar **próxima missão não concluída**.

### ✔ (18) Inventário + Missões do Jogador
Mostra:

- Itens (JOIN inventario + itens)
- Missões com status (JOIN missao + missao_jogador)

### ✔ (19–21) CRUD Completo via JOIN
Permite:
- Criar jogador com ID escolhido
- Atualizar jogador completo
- Deletar jogador completo

---

# 🧱 7. Estrutura do Banco de Dados

Tabelas:

- jogador  
- itens  
- inventario  
- missao  
- missao_jogador  

Inclui:

- Foreign Keys  
- ON DELETE CASCADE  
- Controle completo de missões  

---

# 🧩 8. Estrutura do Projeto

```
src/
 ├─ app/
 │   ├─ Main.java
 │   ├─ MenuRPG.java
 │   ├─ Game.java
 │   ├─ MainHelper.java
 │   └─ Relogio.java
 ├─ dao/
 │   ├─ JogadorDAO.java
 │   ├─ ItemDAO.java
 │   ├─ InventarioDAO.java
 │   ├─ MissaoDAO.java
 │   └─ MissaoJogadorDAO.java
 ├─ Mundo/
 │   └─ Mapas.java
 ├─ Missoes/
 │   ├─ Missao.java
 │   ├─ GerenciadorMissoes.java
 ├─ Entidades/
 │   ├─ Jogador.java
 │   ├─ Inimigo.java
 │   └─ Personagens.java
 ├─ Itens/
 │   ├─ Cura.java
 │   ├─ ItemDesconhecido.java
 │   └─ Inventario.java
```

---

# ▶️ 9. Como Rodar o Jogo

Requisitos:
- Java 17+  
- MySQL 8+  
- IntelliJ, Eclipse ou NetBeans  

### 💾 Criar Banco:
Execute o SQL:

```
DROP DATABASE IF EXISTS rpg_game2;
CREATE DATABASE rpg_game2;
USE rpg_game2;
-- (restante do script incluído no projeto)
```

Configure a conexão em `DB.java`.

### ▶️ Rodar:
Abra o arquivo:

```
app/Main.java
```

E clique **Run**.

---

# 💬 10. Suporte

Se você tiver:
- erros no banco  
- dúvidas no JOIN  
- bugs no mapa ou missão  

basta perguntar que continuo o suporte.

Bom jogo! 🎮🔥

