package Missoes;

public class Missao {

    private int id;
    private String descricao;

    public Missao(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
}
