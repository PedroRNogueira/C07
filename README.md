Este projeto implementa um **banco de dados relacional** para um **jogo RPG**.

---

## Funcionalidades
- Cadastro de **jogadores**.
- Criação de **personagens** associados a jogadores.
- Sistema de **inventário** para cada personagem.
- Cadastro de **itens** com diferentes raridades.
- Cadastro de **missões** com recompensas.
- Controle de quais personagens possuem quais itens e missões.

---

## Estrutura do Banco de Dados

### **Entidades Principais**
| Entidade     | Descrição |
|-------------|-----------|
| **Jogador**     | Armazena os dados do jogador. |
| **Personagem**  | Representa os personagens criados pelos jogadores. |
| **Inventario**  | Inventário exclusivo para cada personagem. |
| **Item**        | Objetos que podem ser armazenados no inventário. |
| **Missao**      | Missões que os personagens podem aceitar. |

---

## Relacionamentos

| Entidade A    | Entidade B    | Tipo de Relação | Descrição |
|--------------|---------------|-----------------|-----------|
| **Jogador**  | **Personagem** | **1:N** | Um jogador pode ter **vários personagens**. |
| **Personagem** | **Inventario** | **1:1** | Cada personagem possui **apenas um inventário**. |
| **Inventario** | **Item** | **N:N** | Um inventário pode conter **vários itens** e um item pode estar em **vários inventários**. |
| **Personagem** | **Missao** | **N:N** | Um personagem pode fazer **várias missões** e cada missão pode ser feita por **vários personagens**. |

---
