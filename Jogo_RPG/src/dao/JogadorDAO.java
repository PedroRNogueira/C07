package dao;

import app.DB;
import Entidades.Jogador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {

    // ==========================
    // INSERT NORMAL (AUTO-ID)
    // ==========================
    public void inserir(Jogador j) throws Exception {
        String sql = """
            INSERT INTO jogador (nome, vida, ataque, mapa_atual, missao_atual, experiencia)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            p.setString(1, j.getNome());
            p.setInt(2, j.getVida());
            p.setInt(3, j.getAtaque());
            p.setInt(4, j.getMapaAtual());
            p.setInt(5, j.getMissaoAtual());
            p.setInt(6, j.getExperiencia());

            p.executeUpdate();

            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) j.setId(rs.getInt(1));
        }
    }

    // ==========================
    // INSERT COMPLETO (ID MANUAL)
    // ==========================
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

    // ==========================
    // SELECT ALL
    // ==========================
    public List<Jogador> listarTodos() throws Exception {

        List<Jogador> lista = new ArrayList<>();

        String sql = "SELECT * FROM jogador ORDER BY id";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                Jogador j = new Jogador(
                        rs.getString("nome"),
                        rs.getInt("vida"),
                        rs.getInt("ataque")  // PRESTE ATENÇÃO AQUI
                );

                j.setId(rs.getInt("id"));
                j.ganharExperiencia(rs.getInt("experiencia"));
                j.setMapaAtual(rs.getInt("mapa_atual"));
                j.setMissaoAtual(rs.getInt("missao_atual"));

                lista.add(j);
            }
        }

        return lista;
    }

    // ==========================
    // SELECT BY ID
    // ==========================
    public Jogador buscarPorId(int id) throws Exception {

        String sql = "SELECT * FROM jogador WHERE id = ?";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {

                Jogador j = new Jogador(
                        rs.getString("nome"),
                        rs.getInt("vida"),
                        rs.getInt("ataque")
                );

                j.setId(rs.getInt("id"));
                j.setMapaAtual(rs.getInt("mapa_atual"));
                j.setMissaoAtual(rs.getInt("missao_atual"));
                j.ganharExperiencia(rs.getInt("experiencia"));

                return j;
            }
        }

        return null;
    }

    // ==========================
    // UPDATE
    // ==========================
    public void atualizar(Jogador j) throws Exception {

        String sql = """
            UPDATE jogador 
            SET nome=?, vida=?, ataque=?, mapa_atual=?, missao_atual=?, experiencia=? 
            WHERE id=?
        """;

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, j.getNome());
            p.setInt(2, j.getVida());
            p.setInt(3, j.getAtaque());
            p.setInt(4, j.getMapaAtual());
            p.setInt(5, j.getMissaoAtual());
            p.setInt(6, j.getExperiencia());
            p.setInt(7, j.getId());

            p.executeUpdate();
        }
    }

    // ==========================
    // DELETE
    // ==========================
    public void deletar(int id) throws Exception {

        String sql = "DELETE FROM jogador WHERE id=?";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            p.executeUpdate();
        }
    }
}
