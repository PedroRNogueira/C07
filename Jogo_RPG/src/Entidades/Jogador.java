package Entidades;

public class Jogador extends Personagens {

    private int experiencia;
    private int capacidadeInventario;
    private int mapaAtual = 1;
    private int missaoAtual = 1;

    public Jogador(String nome, int vida, int ataque) {
        super(nome, vida, ataque);
        experiencia = 0;
        capacidadeInventario = 5;
    }

    public int getExperiencia() { return experiencia; }
    public void ganharExperiencia(int xp) { experiencia += xp; }

    public int getCapacidadeInventario() { return capacidadeInventario; }
    public void setCapacidadeInventario(int capacidadeInventario) { this.capacidadeInventario = capacidadeInventario; }

    public int getMapaAtual() { return mapaAtual; }
    public void setMapaAtual(int mapaAtual) { this.mapaAtual = mapaAtual; }

    public int getMissaoAtual() { return missaoAtual; }
    public void setMissaoAtual(int missaoAtual) { this.missaoAtual = missaoAtual; }

    @Override
    public int atacar() {
        return ataque + (experiencia / 10);
    }
}
