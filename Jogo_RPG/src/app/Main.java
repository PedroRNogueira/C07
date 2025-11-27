package app;

import java.util.Scanner;

import Entidades.Jogador;
import Mundo.Mapas;
import Itens.Inventario;
import dao.JogadorDAO;
import dao.MissaoJogadorDAO;
import Missoes.GerenciadorMissoes;
import Missoes.Missao;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static Jogador jogadorAtual = null;

    public static void main(String[] args) {

        JogadorDAO jogadorDAO = new JogadorDAO();
        Relogio.iniciar();

        while (true) {

            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("Horário: " + Relogio.getHorario());
            System.out.println("1 - Iniciar novo jogo");
            System.out.println("2 - Carregar jogador");
            System.out.println("3 - Menu do Banco de Dados (CRUD / JOINs)");
            System.out.println("4 - Sair");
            System.out.print("> ");

            String op = sc.nextLine().trim();

            try {

                switch (op) {

                    case "1":
                        iniciarNovo();
                        break;

                    case "2":
                        carregarJogador(jogadorDAO);
                        break;

                    case "3":
                        MenuRPG.main(null);
                        break;

                    case "4":
                        System.out.println("Saindo...");
                        return;

                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    // =====================================================================================
    // INICIAR NOVO JOGO
    // =====================================================================================
    private static void iniciarNovo() {

        try {
            System.out.print("Digite o nome do personagem: ");
            String nome = sc.nextLine();

            jogadorAtual = new Jogador(nome, 100, 10);

            JogadorDAO dao = new JogadorDAO();
            dao.inserir(jogadorAtual);  // salva e gera ID

            // Criar progresso de missões no banco
            MissaoJogadorDAO mjdao = new MissaoJogadorDAO();
            mjdao.iniciarMissoesParaJogador(jogadorAtual.getId());

            // Carregar status das missões após criar
            boolean[] status = mjdao.listarMissoesComStatus(jogadorAtual.getId());
            GerenciadorMissoes gm = new GerenciadorMissoes(status);

            // Sincronizar jogador.missao_atual com missões reais
            Missao m = gm.getMissaoAtual();
            if (m != null) jogadorAtual.setMissaoAtual(m.getId());
            else jogadorAtual.setMissaoAtual(5); // todas completas

            dao.atualizar(jogadorAtual);

            // Inventário
            Inventario inv = new Inventario(jogadorAtual);
            MainHelper.carregarInventarioDoBD(inv, jogadorAtual);

            // Mapa inicial
            Mapas mapa = new Mapas(10, 20);
            mapa.setNumeroMapa(jogadorAtual.getMapaAtual());

            Game.iniciar(jogadorAtual, mapa, inv, gm, sc);

        } catch (Exception e) {
            System.out.println("Erro ao iniciar novo jogo: " + e.getMessage());
        }
    }

    // =====================================================================================
    // CARREGAR JOGADOR
    // =====================================================================================
    private static void carregarJogador(JogadorDAO jogadorDAO) {

        try {
            var lista = jogadorDAO.listarTodos();

            if (lista.isEmpty()) {
                System.out.println("Nenhum jogador salvo.");
                return;
            }

            System.out.println("\n=== Jogadores Salvos ===");
            for (var j : lista) {
                System.out.println(j.getId() + " - " + j.getNome() +
                        " (Vida " + j.getVida() +
                        ", XP " + j.getExperiencia() + ")");
            }

            System.out.print("Digite o ID do jogador para carregar: ");
            int id = Integer.parseInt(sc.nextLine());

            jogadorAtual = jogadorDAO.buscarPorId(id);

            if (jogadorAtual == null) {
                System.out.println("ID inválido.");
                return;
            }

            // Inventário
            Inventario inv = new Inventario(jogadorAtual);
            MainHelper.carregarInventarioDoBD(inv, jogadorAtual);

            // Missões
            MissaoJogadorDAO mjdao = new MissaoJogadorDAO();
            var missoes = mjdao.listarMissoesComStatus(id);
            boolean[] status = mjdao.listarMissoesComStatus(id);
            GerenciadorMissoes gm = new GerenciadorMissoes(status);

            // Sincronizar missão atual
            Missao atual = gm.getMissaoAtual();
            if (atual != null) jogadorAtual.setMissaoAtual(atual.getId());
            else jogadorAtual.setMissaoAtual(5);

            jogadorDAO.atualizar(jogadorAtual);

            // Mapa atual
            Mapas mapa = new Mapas(10, 20);
            mapa.setNumeroMapa(jogadorAtual.getMapaAtual());

            Game.iniciar(jogadorAtual, mapa, inv, gm, sc);

        } catch (Exception e) {
            System.out.println("Erro ao carregar jogador: " + e.getMessage());
        }
    }
}
