package Entidades;

public abstract class Personagens {

    protected int id;
    protected String nome;
    protected int vida;
    protected int ataque;

    public Personagens(String nome, int vida, int ataque) {
        this.nome = nome;
        this.vida = vida;
        this.ataque = ataque;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }

    public void receberDano(int d) { vida -= d; if (vida < 0) vida = 0; }

    public void curar(int v) { vida += v; }

    public boolean estaVivo() { return vida > 0; }

    public abstract int atacar();
}
