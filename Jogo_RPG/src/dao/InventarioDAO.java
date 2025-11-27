package dao;

import java.sql.*;
import java.util.*;
import app.DB;

public class InventarioDAO {

    // -------------------------
    // CREATE
    // -------------------------
    public void adicionarItem(int jogadorId, int itemId) throws Exception {
        String sql = "INSERT INTO inventario (jogador_id, item_id) VALUES (?, ?)";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, jogadorId);
            p.setInt(2, itemId);
            p.executeUpdate();
        }
    }

    // -------------------------
    // UPDATE - trocar item do inventário
    // -------------------------
    public void atualizarItemDoJogador(int jogadorId, int itemAntigo, int itemNovo) throws Exception {
    String sql = "UPDATE inventario SET item_id=? WHERE jogador_id=? AND item_id=? LIMIT 1";
    try (Connection c = DB.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, itemNovo);
        p.setInt(2, jogadorId);
        p.setInt(3, itemAntigo);
        p.executeUpdate();
    }
    }

    

    // -------------------------
    // DELETE - remover item do inventário
    // -------------------------
        public void removerItemDoJogador(int jogadorId, int itemId) throws Exception {
        String sql = "DELETE FROM inventario WHERE jogador_id=? AND item_id=? LIMIT 1";
        try (Connection c = DB.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, jogadorId);
        p.setInt(2, itemId);
        p.executeUpdate();
        }
        }

    // -------------------------
    // SELECT
    // -------------------------
    public List<InventarioEntry> listarDetalhado(int jogadorId) throws Exception {

        List<InventarioEntry> itens = new ArrayList<>();

        String sql = """
                     SELECT inv.id AS inv_id, i.id AS item_id, i.nome AS item_nome
                     FROM inventario inv
                     JOIN item i ON i.id = inv.item_id
                     WHERE inv.jogador_id = ?
                     """;

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, jogadorId);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                itens.add(new InventarioEntry(
                        rs.getInt("inv_id"),
                        rs.getInt("item_id"),
                        rs.getString("item_nome")
                ));
            }
        }
        return itens;
    }

    // DTO
    public static class InventarioEntry {
        public final int invId;
        public final int itemId;
        public final String itemNome;

        public InventarioEntry(int invId, int itemId, String itemNome) {
            this.invId = invId;
            this.itemId = itemId;
            this.itemNome = itemNome;
        }
    }
}
