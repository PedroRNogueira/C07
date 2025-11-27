package Itens;

import Entidades.Personagens;

public class ItemDesconhecido implements Item {

    private String nome;

    public ItemDesconhecido(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome + " (?)";
    }

    @Override
    public void aplicar(Personagens alvo) {
        System.out.println("Você olha para o item \"" + nome + "\"…");
        System.out.println("Você não sabe o que isso faz.");
        System.out.println("O item foi descartado.");
    }
}
