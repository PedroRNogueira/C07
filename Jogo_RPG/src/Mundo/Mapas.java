package Mundo;

import java.util.Random;
import java.util.Scanner;

import Entidades.Inimigo;
import Entidades.Jogador;
import Itens.Cura;
import Itens.Inventario;
import Lutas.Batalha;
import Missoes.GerenciadorMissoes;
import Missoes.Missao;

public class Mapas {

    private char[][] mapa;
    private int linhas, colunas;
    private int px, py;

    private int numeroMapa;
    public int getNumeroMapa() { return numeroMapa; }
    public void setNumeroMapa(int n) { numeroMapa = n; }

    private Random r = new Random();

    public Mapas(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        mapa = new char[linhas][colunas];
        iniciar();
    }

    private void iniciar() {
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                mapa[i][j] = '.';

        px = linhas / 2;
        py = colunas / 2;
        mapa[px][py] = 'X';
    }

    public void imprimir() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++)
                System.out.print(mapa[i][j]);
            System.out.println();
        }
    }

    public boolean moverJogador(int dx, int dy, Jogador jogador, Inventario inv, GerenciadorMissoes gm, Scanner sc) {

        int nx = px + dx;
        int ny = py + dy;

        // ============================
        // SAIR DO MAPA
        // ============================
        if (nx < 0 || nx >= linhas || ny < 0 || ny >= colunas) {

            System.out.println("\nVocê saiu do mapa " + numeroMapa + "!");

            // 1) Concluir missão no BD
            try {
                dao.MissaoJogadorDAO mjdao = new dao.MissaoJogadorDAO();
                Missao atual = gm.getMissaoAtual();
                if (atual != null) {
                    mjdao.concluirMissao(jogador.getId(), atual.getId());
                }
            } catch (Exception e) {
                System.out.println("Erro ao marcar missão no BD: " + e.getMessage());
            }

            // 2) Avançar gerenciador de missões
            gm.concluirMissaoAtual();

            // 3) Atualizar jogador.missao_atual e jogador.mapa_atual
            try {
                dao.JogadorDAO jdao = new dao.JogadorDAO();

                Missao nova = gm.getMissaoAtual();
                if (nova != null) jogador.setMissaoAtual(nova.getId());
                else jogador.setMissaoAtual(5); // terminou tudo

                jogador.setMapaAtual(numeroMapa);

                jdao.atualizar(jogador);

                System.out.println("Progresso salvo (mapa_atual e missao_atual)!");
            } catch (Exception e) {
                System.out.println("Erro ao salvar automaticamente: " + e.getMessage());
            }

            // 4) Se terminou tudo
            if (gm.terminouTudo()) {
                System.out.println("\nPARABÉNS! Você concluiu TODAS as missões!");
                sc.nextLine();
                return false;
            }

            // 5) Criar próximo mapa
            int proximo = numeroMapa + 1;
            Mapas novo = new Mapas(linhas, colunas);
            novo.setNumeroMapa(proximo);

            System.out.println("Avançando para o mapa " + proximo + "...");
            System.out.println("\nPressione ENTER para continuar...");
            sc.nextLine();

            app.Game.iniciar(jogador, novo, inv, gm, sc);
            return false;
        }

        // ============================
        // MOVIMENTO NORMAL
        // ============================
        mapa[px][py] = '.';
        px = nx;
        py = ny;
        mapa[px][py] = 'X';

        evento(jogador, inv, sc);

        return true;
    }

    private void evento(Jogador j, Inventario inv, Scanner sc) {

        int chance = r.nextInt(100);

        if (chance < 40) {
            System.out.println("Você encontrou uma poção!");

            try {
                dao.ItemDAO itemDao = new dao.ItemDAO();
                dao.InventarioDAO invDao = new dao.InventarioDAO();

                int itemId = itemDao.buscarIdPorNome("Poção de Cura");
                if (itemId == -1)
                    itemId = itemDao.inserirRetornandoId("Poção de Cura", "cura");

                invDao.adicionarItem(j.getId(), itemId);
                var lista = invDao.listarDetalhado(j.getId());
                int ultimoId = lista.get(lista.size() - 1).invId;

                inv.adicionarComId(new Cura(30), ultimoId);

                System.out.println("Poção salva no BD e adicionada ao inventário!");

            } catch (Exception e) {
                System.out.println("Erro ao salvar item: " + e.getMessage());
            }

        } else if (chance < 80) {

            System.out.println("Um inimigo apareceu!");
            Inimigo ini = new Inimigo("Goblin", 30, 8);

            boolean venceu = Batalha.enfrentar(j, ini, sc);

            if (venceu) {
                System.out.println("Você venceu! +20 XP");
                j.ganharExperiencia(20);
            } else {
                System.out.println("Você foi derrotado...");
            }

        } else {
            System.out.println("Nada aconteceu.");
        }
    }
}
