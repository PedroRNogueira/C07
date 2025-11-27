package dao;

import app.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissaoJogadorDAO {

    // ============================================================
    // INICIAR MISSÕES DO JOGADOR (modo padrão)
    // ============================================================
    public void iniciarMissoesParaJogador(int jogadorId) throws Exception {

        // apaga tudo antes
        String del = "DELETE FROM missao_jogador WHERE jogador_id=?";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(del)) {

            p.setInt(1, jogadorId);
            p.executeUpdate();
        }

        // insere todas as missões como "não concluídas"
        String sel = "SELECT id FROM missao ORDER BY id";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sel);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                String ins = """
                    INSERT INTO missao_jogador (jogador_id, missao_id, concluida)
                    VALUES (?, ?, FALSE)
                """;

                try (PreparedStatement p2 = c.prepareStatement(ins)) {
                    p2.setInt(1, jogadorId);
                    p2.setInt(2, rs.getInt("id"));
                    p2.executeUpdate();
                }
            }
        }
    }

    // ============================================================
    // INICIAR MISSÕES USANDO MISSÃO ATUAL (JOIN)
    // ============================================================
    public void iniciarMissoesComBaseEm(int jogadorId, int missaoAtual) throws Exception {

        // apaga tudo antes
        String del = "DELETE FROM missao_jogador WHERE jogador_id=?";
        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(del)) {

            p.setInt(1, jogadorId);
            p.executeUpdate();
        }

        // recria missões com status baseado na missão atual
        String sel = "SELECT id FROM missao ORDER BY id";

        try (Connection c = DB.conectar();
             PreparedStatement p = c.prepareStatement(sel);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                int mid = rs.getInt("id");

                String ins = """
                    INSERT INTO missao_jogador (jogador_id, missao_id, concluida)
                    VALUES (?, ?, ?)
                """;

                try (PreparedStatement p2 = c.prepareStatement(ins)) {

                    p2.setInt(1, jogadorId);
                    p2.setInt(2, mid);

                    // concluída se missão < missãoAtual
                    p2.setBoolean(3, mid < missaoAtual);

                    p2.executeUpdate();
                }
            }
        }
    }

    // ============================================================
    // LISTAR MISSÕES COM STATUS
    // ============================================================
    public boolean[] listarMissoesComStatus(int jogadorId) throws Exception {

    String sql =
        """
        SELECT m.id, mj.concluida
        FROM missao_jogador mj
        JOIN missao m ON m.id = mj.missao_id
        WHERE mj.jogador_id = ?
        ORDER BY m.id
        """;

    try (Connection c = DB.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, jogadorId);
        ResultSet rs = p.executeQuery();

        // Primeiro conta quantas missões existem
        List<Boolean> temp = new ArrayList<>();

        while (rs.next()) {
            temp.add(rs.getBoolean("concluida"));
        }

        // Converter para boolean[]
        boolean[] resultado = new boolean[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            resultado[i] = temp.get(i);
        }

        return resultado;
    }
}
public List<String> listarMissoesFormatadas(int jogadorId) throws Exception {

    List<String> lista = new ArrayList<>();

    String sql =
        """
        SELECT m.id, m.descricao, mj.concluida
        FROM missao_jogador mj
        JOIN missao m ON m.id = mj.missao_id
        WHERE mj.jogador_id = ?
        ORDER BY m.id
        """;

    try (Connection c = DB.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, jogadorId);
        ResultSet rs = p.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String desc = rs.getString("descricao");
            boolean done = rs.getBoolean("concluida");

            lista.add(id + " - " + desc + (done ? " [CONCLUÍDA]" : " [PENDENTE]"));
        }
    }

    return lista;
}
public void concluirMissao(int jogadorId, int missaoId) throws Exception {

    String sql =
        "UPDATE missao_jogador SET concluida = TRUE " +
        "WHERE jogador_id = ? AND missao_id = ?";

    try (Connection c = DB.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, jogadorId);
        p.setInt(2, missaoId);

        p.executeUpdate();
    }
}




}
