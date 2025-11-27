package Missoes;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorMissoes {

    private List<Missao> missoes;
    private int atual; // index (0-based). atual == missoes.size() => terminou tudo

    public GerenciadorMissoes() {
        this.missoes = new ArrayList<>();
        missoes.add(new Missao(1, "Sair do mapa 1"));
        missoes.add(new Missao(2, "Sair do mapa 2"));
        missoes.add(new Missao(3, "Sair do mapa 3"));
        missoes.add(new Missao(4, "Sair do mapa 4"));
        missoes.add(new Missao(5, "Sair do mapa 5"));
        atual = 0;
    }

    // construir a partir do progresso no banco:
    // status: boolean[] where status[i]==true means missao (i+1) concluída
    public GerenciadorMissoes(boolean[] status) {
        this();
        if (status == null) return;
        for (int i = 0; i < missoes.size() && i < status.length; i++) {
            if (!status[i]) { // first not concluded
                atual = i;
                return;
            }
        }
        atual = missoes.size();
    }

    public Missao getMissaoAtual() {
        if (atual >= missoes.size()) return null;
        return missoes.get(atual);
    }

    public int getIndiceMissaoAtual() {
        return atual; // 0-based
    }

    // conclui a missão atual (se houver) e avança
    public void concluirMissaoAtual() {
        if (atual < missoes.size()) atual++;
    }

    public boolean terminouTudo() {
        return atual >= missoes.size();
    }

    // permite setar diretamente (ex.: pelo banco: missão X)
    public void setMissaoAtualById(int missaoId) {
        if (missaoId <= 0) { atual = 0; return; }
        for (int i = 0; i < missoes.size(); i++) {
            if (missoes.get(i).getId() == missaoId) { atual = i; return; }
        }
        atual = 0;
    }
}
