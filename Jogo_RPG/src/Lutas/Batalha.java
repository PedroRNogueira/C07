package Lutas;

import Entidades.Personagens;
import java.util.Scanner;

public class Batalha {

    public static boolean enfrentar(Personagens p1, Personagens p2, Scanner sc) {

        limparTela();
        System.out.println("=== BATALHA INICIADA ===");
        System.out.println(p1.getNome() + " VS " + p2.getNome());
        System.out.println("Aperte ENTER para começar...");
        sc.nextLine();

        while (p1.estaVivo() && p2.estaVivo()) {

            limparTela();
            System.out.println("=== TURNO DO JOGADOR ===");
            int dano1 = p1.atacar();
            p2.receberDano(dano1);
            System.out.println(p1.getNome() + " causou " + dano1 + " de dano!");
            System.out.println(p2.getNome() + " agora tem " + p2.getVida() + " HP.");
            System.out.println("\nPressione ENTER para o próximo turno...");
            sc.nextLine();

            if (!p2.estaVivo()) break;

            limparTela();
            System.out.println("=== TURNO DO INIMIGO ===");
            int dano2 = p2.atacar();
            p1.receberDano(dano2);
            System.out.println(p2.getNome() + " causou " + dano2 + " de dano!");
            System.out.println(p1.getNome() + " agora tem " + p1.getVida() + " HP.");
            System.out.println("\nPressione ENTER para o próximo turno...");
            sc.nextLine();
        }

        limparTela();

        System.out.println("=== FIM DA BATALHA ===");
        if (p1.estaVivo()) System.out.println(p1.getNome() + " venceu!");
        else System.out.println(p2.getNome() + " venceu!");

        System.out.println("\nStatus do jogador:");
        System.out.println("Nome: " + p1.getNome());
        System.out.println("HP: " + p1.getVida());
        try {
            Entidades.Jogador j = (Entidades.Jogador) p1;
            System.out.println("XP: " + j.getExperiencia());
        } catch (Exception e) {
            // não é jogador, ignora
        }

        System.out.println("\nPressione ENTER para voltar ao mapa...");
        sc.nextLine();

        return p1.estaVivo();
    }

    private static void limparTela() {
        // ANSI escape: funciona em terminais que suportam (Linux/Mac, Windows 10+ com suporte)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
