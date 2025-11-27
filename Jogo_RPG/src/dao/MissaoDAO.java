package dao;

import app.DB;
import Missoes.Missao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissaoDAO {

    // ============================================================
    // INSERT: cria uma missão nova (AUTO_INCREMENT)
    // ============================================================
    public int inserir(String descricao) throws Exception {

        String sql = "INSERT INTO missao (descricao) VALUES (?)";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            p.setString(1, descricao);
            p.executeUpdate();

            ResultSet rs = p.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1); // retorna ID da missão criada
        }

        return -1;
    }

    // ============================================================
    // SELECT ALL
    // ============================================================
    public List<Missao> listarTodas() throws Exception {

        List<Missao> lista = new ArrayList<>();

        String sql = "SELECT * FROM missao ORDER BY id";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                lista.add(
                    new Missao(
                        rs.getInt("id"),
                        rs.getString("descricao")
                    )
                );
            }
        }

        return lista;
    }

    // ============================================================
    // SELECT BY ID
    // ============================================================
    public Missao buscarPorId(int id) throws Exception {

        String sql = "SELECT * FROM missao WHERE id=?";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return new Missao(
                        rs.getInt("id"),
                        rs.getString("descricao")
                );
            }
        }

        return null;
    }

    // ============================================================
    // UPDATE
    // ============================================================
    public void atualizar(int id, String novaDescricao) throws Exception {

        String sql = "UPDATE missao SET descricao=? WHERE id=?";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, novaDescricao);
            p.setInt(2, id);

            p.executeUpdate();
        }
    }

    // ============================================================
    // DELETE
    // ============================================================
    public void deletar(int id) throws Exception {

        // Primeiro apaga vínculos com jogadores
        String sql1 = "DELETE FROM missao_jogador WHERE missao_id=?";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql1)) {

            p.setInt(1, id);
            p.executeUpdate();
        }

        // Agora apaga a missão em si
        String sql2 = "DELETE FROM missao WHERE id=?";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql2)) {

            p.setInt(1, id);
            p.executeUpdate();
        }
    }

    // ============================================================
    // AJUDA O GERENCIADOR A INICIAR MISSÕES
    // (usado ao criar jogador via CRUD normal)
    // ============================================================
    public List<Integer> listarIds() throws Exception {

        List<Integer> lista = new ArrayList<>();

        String sql = "SELECT id FROM missao ORDER BY id";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next())
                lista.add(rs.getInt("id"));
        }

        return lista;
    }

    // ============================================================
    // INICIALIZA missao_jogador de acordo com a missão atual
    // (usado quando cria jogador manualmente via JOIN)
    // ============================================================
    
            
        
    
}
