Jogo_RPG — Resumo e instruções

Descrição
---------
Pequeno jogo de RPG em console (Java). O ponto de entrada é `src/Main.java`. O fluxo básico:
- `Main` cria um `Scanner`, um `Jogador` e um `Mapas` e chama `mapa.explorar(jogador)`.
- `Mapas.explorar()` mostra um mapa, recebe comandos W/A/S/D para mover e gera eventos aleatórios:
  - Batalha contra um `Inimigo` (mais provável)
  - Encontrar um `Itens` (poção de cura)
- `Batalha` gerencia turnos: jogador e inimigo atacam alternadamente até um morrer. Jogador ganha XP/ouro e possivelmente loot.

Arquivos principais
------------------
- `src/Main.java` — entrada do programa.
- `src/Mundo/Mapas.java` — lógica de exploração, movimentação e eventos.
- `src/Lutas/Batalha.java` — loop de combate entre `Jogador` e `Inimigo`.
- `src/Entidades/Personagens.java` — classe abstrata base (nome, vida, ataque).
- `src/Entidades/Jogador.java` — herda `Personagens`, controla XP/nível/ouro.
- `src/Entidades/Inimigo.java` — herda `Personagens`, lógica de ataque do inimigo.
- `src/Itens/Itens.java` — modelo simples de item (nome, valor).

Notas sobre o comportamento e probabilidades
-------------------------------------------
- Em `Mapas.explorar()` o evento aleatório é decidido com `rand.nextInt(100)` (0..99):
  - `evento <= 80` (0..80) → batalha (~81% de chance)
  - `evento > 80` (81..99) → encontra item (~19% de chance)
  - Observação: o código original tem um `else` que, com as condições atuais, é inalcançável. Se desejar um caso "nada encontrado", ajuste as condições (por exemplo, `if (evento < 80)`, `else if (evento < 95)`, `else`).
- Em batalhas:
  - Jogador ataca: `getAtaque() + getRand().nextInt(6)` (ataque base + 0..5)
  - Inimigo ataca: `getAtaque() + getRand().nextInt(5)` (ataque base + 0..4)
  - Ao vencer, jogador ganha XP `rand.nextInt(10) + 10` (10..19) e ouro `rand.nextInt(10) + 5` (5..14).
  - Loot: gerado com ~40% de chance dentro de `gerarLoot()` (se gerado, escolhe um item aleatório).
- Cura: o projeto usa `jogador.receberDano(-15)` para aumentar vida em 15. Isso funciona porque `receberDano` aplica `vida = Math.max(0, vida - dano)`.

Como compilar (PowerShell)
-------------------------
Observação: você pediu para não instalar nada agora. Os comandos abaixo assumem que `javac`/`java` estão no PATH ou que `JAVA_HOME` aponte para o JDK desejado.

Compilar (simples):
```powershell
javac -d out src\Main.java src\Entidades\*.java src\Lutas\*.java src\Mundo\*.java src\Itens\*.java
```
Executar:
```powershell
java -cp out Main
```


Alterações
---------------------------------
- Inserção de comentários explicativos (em português) em todas as classes, indicando conceitos de POO (herança, encapsulamento, polimorfismo, composição) e explicando trechos como `scanner.nextLine().trim().toUpperCase()`, `rand.nextInt()`, `Math.max()` e `receberDano(-15)`.
