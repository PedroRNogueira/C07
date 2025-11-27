package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Entidades.Jogador;
import app.DB;

public class ItemDAO {

    public int inserirRetornandoId(String nome, String tipo) throws Exception {
        String sql = "INSERT INTO item (nome, tipo) VALUES (?, ?)";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            p.setString(1, nome);
            p.setString(2, tipo);
            p.executeUpdate();

            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public void inserirCompleto(Jogador j) throws Exception {

    String sql = """
        INSERT INTO jogador (id, nome, vida, ataque, mapa_atual, missao_atual, experiencia)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    try (Connection c = DB.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, j.getId());
        p.setString(2, j.getNome());
        p.setInt(3, j.getVida());
        p.setInt(4, j.getAtaque());
        p.setInt(5, j.getMapaAtual());
        p.setInt(6, j.getMissaoAtual());
        p.setInt(7, j.getExperiencia());

        p.executeUpdate();
    }
}


    public void atualizar(int id, String novoNome, String novoTipo) throws Exception {
        String sql = "UPDATE item SET nome=?, tipo=? WHERE id=?";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, novoNome);
            p.setString(2, novoTipo);
            p.setInt(3, id);
            p.executeUpdate();
        }
    }

    public void deletar(int id) throws Exception {
        String sql = "DELETE FROM item WHERE id=?";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            p.executeUpdate();
        }
    }

    public List<String> listar() throws Exception {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT * FROM item ORDER BY id";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getInt("id") + " - " + rs.getString("nome") +
                          " (" + rs.getString("tipo") + ")");
            }
        }
        return lista;
    }

    public int buscarIdPorNome(String nome) throws Exception {
        String sql = "SELECT id FROM item WHERE nome = ?";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, nome);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }
}
