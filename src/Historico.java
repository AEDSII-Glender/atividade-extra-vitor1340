import java.util.NoSuchElementException;

public class Historico<V extends Musica> {
    
    private NoLista<V> topo; // Mais recente
    private NoLista<V> atual; // Posição de navegação
    private int tamanho;
    
    public Historico() {
        topo = null;
        atual = null;
        tamanho = 0;
    }

    public boolean vazia() { return tamanho == 0; }

    /** Adiciona uma música ao histórico (sempre no topo, como um Stack/Pilha). */
    public void adicionar(V musica) {
        NoLista<V> novoNo = new NoLista<>(musica);
        if (!vazia()) {
            novoNo.setProximo(topo);
            topo.setAnterior(novoNo);
        }
        topo = novoNo;
        atual = topo; // Reseta a navegação para o mais recente após uma nova reprodução
        tamanho++;
    }

    /** Volta uma reprodução no histórico (move o ponteiro 'atual' para o anterior). */
    public V voltarReproducao() {
        if (vazia()) return null;
        
        if (atual.getProximo() != null) {
            atual = atual.getProximo();
            return atual.getItem();
        }
        return null; // Está na reprodução mais antiga
    }

    @Override
    public String toString() {
        if (vazia()) return "Histórico de reprodução vazio.";
        StringBuilder sb = new StringBuilder();
        sb.append("--- Histórico de Reprodução (Recente -> Antiga) ---\n");
        NoLista<V> temp = topo;
        int i = 1;
        while (temp != null) {
            String indicador = (temp == atual) ? " <--- Reprodução Atual (Voltar aqui)" : "";
            sb.append(String.format("%d. %s%s\n", i++, temp.getItem().toString(), indicador));
            temp = temp.getProximo();
        }
        return sb.toString();
    }
}