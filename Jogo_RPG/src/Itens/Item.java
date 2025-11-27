package Itens;

import Entidades.Personagens;

public interface Item {
    String getNome();
    void aplicar(Personagens alvo);
}
