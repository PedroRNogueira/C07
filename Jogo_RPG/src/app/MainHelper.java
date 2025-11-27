package app;

import dao.InventarioDAO;
import dao.ItemDAO;
import Entidades.Jogador;
import Itens.Cura;
import Itens.Inventario;
import Itens.ItemDesconhecido;

public class MainHelper {

    public static void carregarInventarioDoBD(Inventario inv, Jogador jogador) throws Exception {

        InventarioDAO invDao = new InventarioDAO();
        ItemDAO itemDAO = new ItemDAO();

        var itensBD = invDao.listarDetalhado(jogador.getId());

        for (var it : itensBD) {

            String nomeBD = it.itemNome.toLowerCase();

            if (nomeBD.contains("cura")) {
                inv.adicionarComId(new Cura(30), it.invId);
            } else {
                inv.adicionarComId(new ItemDesconhecido(it.itemNome), it.invId);
            }
        }
    }
}
