package Entidades;

public class Inimigo extends Personagens {

    public Inimigo(String nome, int vida, int ataque) {
        super(nome, vida, ataque);
    }

    @Override
    public int atacar() {
        return ataque;
    }
}
