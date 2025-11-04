# 🧙‍♂️ Banco de Dados — Jogo RPG (Segunda Entrega)

## ⚠️ Importante — Usuário de Exemplo

Neste projeto, foi criado um **usuário SQL fictício** apenas **para demonstração acadêmica** do controle de permissões em um banco de dados.  
O usuário `'jogador'@'localhost'` é um **exemplo didático** exigido pelos critérios da entrega, com permissões básicas (`SELECT, INSERT, UPDATE, DELETE`).  

> 💡 Como o projeto foi desenvolvido individualmente, **todas as operações reais** foram realizadas utilizando a **conta administrativa padrão do MySQL**.  
> A presença do usuário `jogador` serve **somente para comprovar o conhecimento sobre gerenciamento de acessos** no contexto da disciplina.

Exemplo de criação do usuário:
```sql
CREATE USER IF NOT EXISTS 'jogador'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT, INSERT, UPDATE, DELETE ON jogo_rpg.* TO 'jogador'@'localhost';
FLUSH PRIVILEGES;
```

---

## 🧩 Descrição Geral

Este projeto implementa o **banco de dados do jogo RPG por turnos**, conectando-se ao código Java desenvolvido anteriormente.  
Foi desenvolvido para a **segunda entrega prática da disciplina**, contendo:

- Estrutura completa de tabelas e relacionamentos  
- Inserções com dados representativos (poucos nulos)  
- Atualizações e exclusões controladas  
- Uma função, uma view e uma trigger  
- Um usuário de exemplo com permissões limitadas  

---

## 🧱 Estrutura do Banco de Dados

| Tabela | Descrição |
|---------|------------|
| **personagem** | Armazena informações dos personagens jogáveis (nome, vida, ataque, mana, nível e experiência). |
| **inimigo** | Contém inimigos do jogo, com atributos e experiência concedida ao serem derrotados. |
| **item** | Define todos os itens do jogo (nome, tipo, efeito e valor). |
| **loot** | Relaciona inimigos aos itens que podem dropar, com uma chance percentual. |
| **inventario** | Representa os itens pertencentes a cada personagem. |
| **mapa** | Estrutura as posições do mundo do jogo (coordenadas, inimigos, itens, saídas, etc.). |

---

## 🔗 Relacionamentos

| Entidade A | Entidade B | Tipo | Descrição |
|-------------|-------------|------|------------|
| **personagem** | **inventario** | 1:N | Cada personagem pode possuir vários itens. |
| **item** | **inventario** | 1:N | Um mesmo item pode estar em vários inventários. |
| **inimigo** | **loot** | 1:N | Um inimigo pode dropar vários itens. |
| **item** | **loot** | 1:N | Um item pode ser dropado por diversos inimigos. |
| **mapa** | **inimigo/item** | N:1 | Cada posição pode conter um inimigo, item ou estar vazia. |

---

## 🧠 Estruturas Implementadas

| Tipo | Nome | Finalidade |
|------|------|------------|
| **VIEW** | `vw_personagem_status` | Exibe o progresso percentual de XP até o próximo nível. |
| **FUNCTION** | `xp_necessaria(nivel_atual)` | Calcula a experiência necessária para subir de nível. |
| **TRIGGER** | `trigger_subir_nivel` | Atualiza automaticamente os atributos ao alcançar XP suficiente. |

---

## 🔄 Atualizações e Alterações

As seguintes operações foram aplicadas como parte das **alterações controladas** da entrega:

| Tipo | Operação | Descrição |
|------|-----------|------------|
| **UPDATE** | `UPDATE personagem SET experiencia = experiencia + 50;` | Simula ganho de XP após batalha. |
| **DELETE** | `DELETE FROM item WHERE tipo = 'outro';` | Remove itens obsoletos sem função ativa. |
| **ALTER** | `ALTER TABLE personagem ADD COLUMN mana INT DEFAULT 100;` | Adiciona o atributo “mana” à tabela principal. |
| **DROP** | `DROP TABLE IF EXISTS loot;` | Remoção da estrutura antiga para recriação com integridade referencial. |

Essas ações representam o **processo de manutenção natural** de um banco de dados em evolução, refletindo ajustes de gameplay e balanceamento.

---

## 🗺️ Estrutura do Mapa — Exemplo Prático

| Linha | Coluna | Tipo | Conteúdo |
|--------|---------|------|-----------|
| 1 | 1 | item | Poção de Cura |
| 1 | 2 | inimigo | Goblin |
| 1 | 3 | item | Espada Curta |
| 1 | 4 | inimigo | Lobo Selvagem |
| 1 | 5 | saída | Fim do mapa |
| 2 | 1 | inimigo | Esqueleto |
| 2 | 2 | item | Escudo Pequeno |
| 2 | 3 | vazio | - |
| 2 | 4 | inimigo | Orc |
| 2 | 5 | item | Poção de Mana |

---

## ✅ Requisitos da Entrega — Verificação

| Critério | Cumprimento |
|-----------|-------------|
| Criação de tabelas com relacionamentos | ✅ |
| Inserções representativas (mínimo de 3 por tabela) | ✅ |
| 2 atualizações ou exclusões | ✅ |
| 1 ALTER e 1 DROP | ✅ |
| Criação de 1 usuário SQL com privilégios | ✅ *(usuário exemplo)* |
| 3 estruturas (Function, View, Trigger) | ✅ |

---

## 🧾 Observações Finais

- O banco pode ser recriado sem erros, utilizando comandos com `IF EXISTS` e `IF NOT EXISTS`.  
- Todos os relacionamentos seguem **integridade referencial** com chaves estrangeiras e cascatas adequadas.  
- O sistema foi projetado para ser compatível com o código Java do jogo RPG em turnos.  
- O usuário `'jogador'@'localhost'` **não é utilizado na prática**, mas **foi incluído unicamente para atender à exigência do controle de acessos** na entrega.  

---

## 📂 Arquivos do Projeto

| Arquivo | Descrição |
|----------|------------|
| `banco_rpg.sql` | Script completo com tabelas, inserções, updates, triggers, view e function. |
| `banco_rpg.mwb` | Modelo visual criado no MySQL Workbench. |
| `README.md` | Documentação explicativa e resumo da entrega. |

---

**Autor:** Pedro Ribeiro Nogueira  
**Curso:** Engenharia de Software — INATEL  
**Período:** 5º  
**Entrega:** Segunda Etapa — Banco de Dados (Jogo RPG)
