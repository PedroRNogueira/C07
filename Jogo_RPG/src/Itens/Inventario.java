package Itens;

import java.util.ArrayList;
import java.util.List;
import Entidades.Personagens;

public class Inventario {

    private List<Item> itens = new ArrayList<>();
    private List<Integer> idsBD = new ArrayList<>();

    private Personagens dono;

    public Inventario(Personagens dono) {
        this.dono = dono;
    }

    public void adicionar(Item item) {
        itens.add(item);
        idsBD.add(-1);
    }

    public void adicionarComId(Item item, int idBD) {
        itens.add(item);
        idsBD.add(idBD);
    }

    public List<Item> listar() {
        return itens;
    }

    public void usar(int idx, Personagens alvo) {
        if (idx >= 0 && idx < itens.size()) {
            itens.get(idx).aplicar(alvo);
            itens.remove(idx);
            idsBD.remove(idx);
        }
    }
}
