package app;

import dao.*;
import Entidades.Jogador;
import java.sql.*;
import java.util.Scanner;

public class MenuRPG {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        JogadorDAO jogadorDAO = new JogadorDAO();
        ItemDAO itemDAO = new ItemDAO();
        InventarioDAO inventarioDAO = new InventarioDAO();
        MissaoDAO missaoDAO = new MissaoDAO();
        MissaoJogadorDAO missaoJogadorDAO = new MissaoJogadorDAO();

        while (true) {

            System.out.println("\n===== MENU RPG (CRUD + JOINS) =====");

            System.out.println("\n--- JOGADOR ---");
            System.out.println("1 - Inserir jogador");
            System.out.println("2 - Atualizar jogador");
            System.out.println("3 - Deletar jogador");
            System.out.println("4 - Listar jogadores");

            System.out.println("\n--- ITENS ---");
            System.out.println("5 - Inserir item");
            System.out.println("6 - Listar itens");
            System.out.println("7 - Atualizar item");
            System.out.println("8 - Deletar item");

            System.out.println("\n--- INVENTÁRIO ---");
            System.out.println("9 - Adicionar item ao inventário");
            System.out.println("10 - Listar inventário de um jogador");
            System.out.println("11 - Atualizar item do inventário");
            System.out.println("12 - Remover item do inventário");

            System.out.println("\n--- MISSÕES ---");
            System.out.println("13 - Inserir missão");
            System.out.println("14 - Listar missões");
            System.out.println("15 - Atualizar missão");
            System.out.println("16 - Deletar missão");

            System.out.println("\n--- JOINS ---");
            System.out.println("17 - Listar jogadores e MISSAO ATUAL (computed)");
            System.out.println("18 - Mostrar detalhes (Inventário + Missões) de um jogador");
            System.out.println("19 - Adicionar jogador (JOIN)");
            System.out.println("20 - Atualizar jogador (JOIN)");
            System.out.println("21 - Deletar jogador (JOIN)");

            System.out.println("\n0 - Sair");
            System.out.print("Escolha: ");

            int op = lerInt();

            try {
                switch (op) {

                    // JOGADOR
                    case 1 -> inserirJogador(jogadorDAO, missaoJogadorDAO);
                    case 2 -> atualizarJogador(jogadorDAO);
                    case 3 -> deletarJogador(jogadorDAO);
                    case 4 -> listarJogadores(jogadorDAO);

                    // ITENS
                    case 5 -> inserirItem(itemDAO);
                    case 6 -> listarItens(itemDAO);
                    case 7 -> atualizarItem(itemDAO);
                    case 8 -> deletarItem(itemDAO);

                    // INVENTÁRIO
                    case 9 -> adicionarItemInventario(inventarioDAO);
                    case 10 -> listarInventario(inventarioDAO);
                    case 11 -> atualizarItemInventario(inventarioDAO);
                    case 12 -> deletarItemInventario(inventarioDAO);

                    // MISSÕES
                    case 13 -> inserirMissao(missaoDAO);
                    case 14 -> listarMissoes(missaoDAO);
                    case 15 -> atualizarMissao(missaoDAO);
                    case 16 -> deletarMissao(missaoDAO);

                    // JOINS
                    case 17 -> listarJogadoresComMissaoAtual();
                    case 18 -> mostrarDetalhesJogador(inventarioDAO, missaoJogadorDAO); // CORRIGIDO
                    case 19 -> adicionarJogadorJOIN(jogadorDAO, missaoJogadorDAO);
                    case 20 -> atualizarJogadorJOIN(jogadorDAO);
                    case 21 -> deletarJogadorJOIN(jogadorDAO);

                    case 0 -> {
                        System.out.println("Saindo...");
                        return;
                    }

                    default -> System.out.println("Opção inválida.");
                }

            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Número inválido. Tente novamente: ");
            }
        }
    }

    // =====================================================================================
    // CRUD JOGADOR
    // =====================================================================================

    private static void inserirJogador(JogadorDAO dao, MissaoJogadorDAO mj) throws Exception {
        System.out.print("Nome do jogador: ");
        String nome = sc.nextLine();

        Jogador j = new Jogador(nome, 100, 10);
        dao.inserir(j);

        mj.iniciarMissoesParaJogador(j.getId());

        System.out.println("Jogador criado com ID: " + j.getId());
    }

    private static void atualizarJogador(JogadorDAO dao) throws Exception {
        System.out.print("ID do jogador: ");
        int id = lerInt();

        Jogador j = dao.buscarPorId(id);
        if (j == null) {
            System.out.println("Jogador não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        j.setNome(sc.nextLine());

        System.out.print("Nova vida: ");
        j.setVida(lerInt());

        dao.atualizar(j);

        System.out.println("Jogador atualizado!");
    }

    private static void deletarJogador(JogadorDAO dao) throws Exception {
        System.out.print("ID do jogador para deletar: ");
        int id = lerInt();
        dao.deletar(id);
        System.out.println("Jogador deletado!");
    }

    private static void listarJogadores(JogadorDAO dao) throws Exception {
        var lista = dao.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        lista.forEach(j ->
                System.out.println(j.getId() + " - " + j.getNome() + " (XP: " + j.getExperiencia() + ")")
        );
    }

    // =====================================================================================
    // CRUD ITENS
    // =====================================================================================

    private static void inserirItem(ItemDAO dao) throws Exception {
        System.out.print("Nome do item: ");
        String nome = sc.nextLine();

        System.out.print("Tipo: ");
        String tipo = sc.nextLine();

        int id = dao.inserirRetornandoId(nome, tipo);

        System.out.println("Item criado com ID: " + id);
    }

    private static void listarItens(ItemDAO dao) throws Exception {
        var lista = dao.listar();
        lista.forEach(System.out::println);
    }

    private static void atualizarItem(ItemDAO dao) throws Exception {
        System.out.print("ID do item: ");
        int id = lerInt();

        System.out.print("Novo nome: ");
        String nome = sc.nextLine();

        System.out.print("Novo tipo: ");
        String tipo = sc.nextLine();

        dao.atualizar(id, nome, tipo);

        System.out.println("Item atualizado!");
    }

    private static void deletarItem(ItemDAO dao) throws Exception {
        System.out.print("ID do item para deletar: ");
        int id = lerInt();

        String sql = "DELETE FROM inventario WHERE item_id=?";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }

        dao.deletar(id);

        System.out.println("Item e vínculos deletados.");
    }

    // =====================================================================================
    // INVENTÁRIO
    // =====================================================================================

    private static void adicionarItemInventario(InventarioDAO dao) throws Exception {
        System.out.print("ID jogador: ");
        int jogadorId = lerInt();

        System.out.print("ID item: ");
        int itemId = lerInt();

        dao.adicionarItem(jogadorId, itemId);

        System.out.println("Item adicionado ao inventário!");
    }

    private static void listarInventario(InventarioDAO dao) throws Exception {
        System.out.print("ID do jogador: ");
        int jogadorId = lerInt();

        var lista = dao.listarDetalhado(jogadorId);

        if (lista.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }

        lista.forEach(i ->
                System.out.println("InvID: " + i.invId + " | ItemID: " + i.itemId + " | Nome: " + i.itemNome)
        );
    }

    private static void atualizarItemInventario(InventarioDAO dao) throws Exception {

        System.out.print("ID do jogador: ");
        int jogadorId = lerInt();

        System.out.print("ID do item antigo (que o jogador possui): ");
        int itemAntigo = lerInt();

        System.out.print("ID do novo item (item_id): ");
        int itemNovo = lerInt();

        dao.atualizarItemDoJogador(jogadorId, itemAntigo, itemNovo);

        System.out.println("Item do inventário atualizado!");
    }

    private static void deletarItemInventario(InventarioDAO dao) throws Exception {

        System.out.print("ID do jogador: ");
        int jogadorId = lerInt();

        System.out.print("ID do item para remover do inventário: ");
        int itemId = lerInt();

        dao.removerItemDoJogador(jogadorId, itemId);

        System.out.println("Item removido do inventário!");
    }

    // =====================================================================================
    // MISSÕES
    // =====================================================================================

    private static void inserirMissao(MissaoDAO dao) throws Exception {
        System.out.print("Descrição da missão: ");
        String desc = sc.nextLine();

        int id = dao.inserir(desc);

        System.out.println("Missão criada com ID: " + id);
    }

    private static void listarMissoes(MissaoDAO dao) throws Exception {
        var lista = dao.listarTodas();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma missão cadastrada.");
            return;
        }

        lista.forEach(m ->
                System.out.println(m.getId() + " - " + m.getDescricao())
        );
    }

    private static void atualizarMissao(MissaoDAO dao) throws Exception {
        System.out.print("ID da missão: ");
        int id = lerInt();

        System.out.print("Nova descrição: ");
        String desc = sc.nextLine();

        dao.atualizar(id, desc);

        System.out.println("Missão atualizada!");
    }

    private static void deletarMissao(MissaoDAO dao) throws Exception {
        System.out.print("ID da missão para deletar: ");
        int id = lerInt();

        dao.deletar(id);

        System.out.println("Missão deletada!");
    }

    // =====================================================================================
    // JOIN 1 — PRÓXIMA MISSÃO
    // =====================================================================================

    private static void listarJogadoresComMissaoAtual() {

        String sql =
                """
                SELECT j.id AS jogador_id, j.nome AS jogador_nome, j.mapa_atual,
                       pm.prox_missao, m.descricao AS prox_missao_desc
                FROM jogador j
                LEFT JOIN (
                    SELECT jogador_id, MIN(missao_id) AS prox_missao
                    FROM missao_jogador
                    WHERE concluida = FALSE
                    GROUP BY jogador_id
                ) pm ON pm.jogador_id = j.id
                LEFT JOIN missao m ON m.id = pm.prox_missao
                ORDER BY j.id;
                """;

        try (Connection c = DB.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | Nome | MapaAtual | MissaoAtual");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("jogador_id");
                String nome = rs.getString("jogador_nome");
                int mapa = rs.getInt("mapa_atual");

                Integer proxId = (Integer) rs.getObject("prox_missao");
                String desc = rs.getString("prox_missao_desc");

                if (proxId == null)
                    System.out.printf("%d | %s | %d | TODAS CONCLUÍDAS%n", id, nome, mapa);
                else
                    System.out.printf("%d | %s | %d | %d - %s%n", id, nome, mapa, proxId, desc);
            }

        } catch (Exception e) {
            System.out.println("Erro JOIN: " + e.getMessage());
        }
    }

    // =====================================================================================
    // JOIN 2 — INVENTÁRIO + MISSÕES (CORRIGIDO)
    // =====================================================================================

    private static void mostrarDetalhesJogador(InventarioDAO invDao, MissaoJogadorDAO mjDao) {
        System.out.print("ID jogador: ");
        int jogadorId = lerInt();

        try {

            System.out.println("\n--- Inventário ---");
            var itens = invDao.listarDetalhado(jogadorId);
            if (itens.isEmpty()) System.out.println("[Inventário vazio]");
            else itens.forEach(e -> System.out.println("InvID: " + e.invId + " | ItemID: " + e.itemId + " | Nome: " + e.itemNome));

            System.out.println("\n--- Missões ---");

            // CORRIGIDO: usar método correto para o MENU
            var missoes = mjDao.listarMissoesFormatadas(jogadorId);

            if (missoes.isEmpty()) {
                System.out.println("[Nenhuma missão]");
            } else {
                missoes.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // =====================================================================================
    // JOIN — CRUD COMPLETO DE JOGADOR
    // =====================================================================================

    private static void adicionarJogadorJOIN(JogadorDAO jogadorDAO, MissaoJogadorDAO missaoDAO) throws Exception {

        System.out.println("\n=== Criar Jogador (JOIN) ===");

        System.out.print("ID do jogador (digite o ID desejado): ");
        int id = lerInt();

        Jogador existente = jogadorDAO.buscarPorId(id);
        if (existente != null) {
            System.out.println("ERRO: Já existe jogador com esse ID!");
            return;
        }

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Vida: ");
        int vida = lerInt();

        System.out.print("Ataque: ");
        int ataque = lerInt();

        System.out.print("Mapa atual: ");
        int mapaAtual = lerInt();

        System.out.print("Missão atual: ");
        int missaoAtual = lerInt();

        Jogador j = new Jogador(nome, vida, ataque);
        j.setId(id);
        j.setMapaAtual(mapaAtual);
        j.setMissaoAtual(missaoAtual);

        jogadorDAO.inserirCompleto(j);

        missaoDAO.iniciarMissoesComBaseEm(j.getId(), missaoAtual);

        System.out.println("Jogador criado com sucesso!");
    }

    private static void atualizarJogadorJOIN(JogadorDAO jogadorDAO) throws Exception {

        System.out.print("ID do jogador para atualizar: ");
        int id = lerInt();

        Jogador j = jogadorDAO.buscarPorId(id);

        if (j == null) {
            System.out.println("Jogador não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        j.setNome(sc.nextLine());

        System.out.print("Novo mapa_atual: ");
        j.setMapaAtual(lerInt());

        System.out.print("Nova vida: ");
        j.setVida(lerInt());

        jogadorDAO.atualizar(j);

        System.out.println("Jogador atualizado com sucesso!");
    }

    private static void deletarJogadorJOIN(JogadorDAO jogadorDAO) throws Exception {

        System.out.print("ID do jogador para deletar: ");
        int id = lerInt();

        jogadorDAO.deletar(id);

        System.out.println("Jogador deletado com sucesso!");
    }

}
