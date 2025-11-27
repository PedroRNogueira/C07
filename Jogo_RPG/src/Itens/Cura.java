package Itens;

import Entidades.Personagens;

public class Cura implements Item {

    private int valor;

    public Cura(int valor) {
        this.valor = valor;
    }

    public String getNome() {
        return "Poção de Cura";
    }

    public void aplicar(Personagens alvo) {

        int vidaAntes = alvo.getVida();
        int vidaDepois = vidaAntes + valor;

        // limite de 100 HP
        if (vidaDepois > 100)
            vidaDepois = 100;

        alvo.setVida(vidaDepois);

        int curado = vidaDepois - vidaAntes;

        System.out.println("Você recuperou +" + curado + " de vida!");
    }
}
