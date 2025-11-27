package app;

import java.util.Scanner;
import Entidades.Jogador;
import Mundo.Mapas;
import Itens.Inventario;

public class Game {

    public static void iniciar(Jogador jogador, Mapas mapa, Inventario inv, Missoes.GerenciadorMissoes gm, Scanner sc) {

        boolean jogando = true;

        while (jogando) {

            System.out.println("\n=== JOGO ===");
            System.out.println("Nome: " + jogador.getNome() +
                    " | Vida: " + jogador.getVida() +
                    " | XP: " + jogador.getExperiencia());

            mapa.imprimir();

            System.out.println("Comandos: W/A/S/D mover | I inventário | Q sair");
            System.out.print("> ");

            String comando = sc.nextLine().trim().toUpperCase();

            switch (comando) {
                case "W": jogando = mapa.moverJogador(-1, 0, jogador, inv, gm, sc); break;
                case "S": jogando = mapa.moverJogador(1, 0, jogador, inv, gm, sc); break;
                case "A": jogando = mapa.moverJogador(0, -1, jogador, inv, gm, sc); break;
                case "D": jogando = mapa.moverJogador(0, 1, jogador, inv, gm, sc); break;

                case "I":
                    if (inv.listar().isEmpty()) {
                        System.out.println("Inventário vazio.");
                        break;
                    }

                    System.out.println("\n== INVENTÁRIO ==");
                    for (int i = 0; i < inv.listar().size(); i++) {
                        System.out.println(i + " - " + inv.listar().get(i).getNome());
                    }

                    System.out.print("Usar qual item? ");
                    int idx = -1;

                    try {
                        idx = Integer.parseInt(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("Índice inválido.");
                        break;
                    }

                    if (idx >= 0) {
                        inv.usar(idx, jogador);
                    }

                    break;

                case "Q":
                    System.out.println("Retornando ao menu...");
                    jogando = false;
                    break;

                default:
                    System.out.println("Comando inválido.");
            }

            if (!jogador.estaVivo()) {
                System.out.println("\nVocê morreu! Retornando ao menu...");
                jogando = false;
            }
        }
    }
}
