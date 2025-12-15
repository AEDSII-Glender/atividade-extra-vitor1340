import java.util.Comparator;
import java.util.NoSuchElementException;

public class ListaDupla<V extends Musica> {
    
    private NoLista<V> primeiro;
    private NoLista<V> ultimo;
    private int tamanho;
    
    public ListaDupla() {
        primeiro = null;
        ultimo = null;
        tamanho = 0;
    }
    
    // Getters de Suporte para Reprodução em Ordem
    public NoLista<V> getPrimeiro() { return primeiro; }
    public NoLista<V> getUltimo() { return ultimo; }

    public boolean vazia() { return tamanho == 0; }
    public int tamanho() { return tamanho; }

    /** Adiciona uma música ao final da playlist. */
    public void adicionar(V musica) {
        NoLista<V> novoNo = new NoLista<>(musica);
        if (vazia()) {
            primeiro = novoNo;
            ultimo = novoNo;
        } else {
            ultimo.setProximo(novoNo);
            novoNo.setAnterior(ultimo);
            ultimo = novoNo;
        }
        tamanho++;
    }

    /** Remove uma música da playlist pelo seu ID. */
    public V remover(int id) {
        NoLista<V> atual = primeiro;
        while (atual != null) {
            if (atual.getItem().getId() == id) {
                // Lógica de remoção
                if (atual == primeiro) {
                    primeiro = atual.getProximo();
                    if (primeiro != null) primeiro.setAnterior(null); else ultimo = null; 
                } else if (atual == ultimo) {
                    ultimo = atual.getAnterior();
                    ultimo.setProximo(null);
                } else {
                    atual.getAnterior().setProximo(atual.getProximo());
                    atual.getProximo().setAnterior(atual.getAnterior());
                }
                tamanho--;
                return atual.getItem();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    /** Busca uma música na playlist pelo ID. */
    public V buscarPorId(int id) {
        NoLista<V> atual = primeiro;
        while (atual != null) {
            if (atual.getItem().getId() == id) return atual.getItem();
            atual = atual.getProximo();
        }
        return null;
    }
    
    /** Busca uma música na playlist pelo Título (não sensível a caso). */
    public V buscarPorTitulo(String titulo) {
        NoLista<V> atual = primeiro;
        String tituloLower = titulo.toLowerCase();
        while (atual != null) {
            if (atual.getItem().getTitulo().toLowerCase().equals(tituloLower)) return atual.getItem();
            atual = atual.getProximo();
        }
        return null;
    }

    @Override
    public String toString() {
        if (vazia()) return "Playlist vazia.";
        StringBuilder sb = new StringBuilder();
        NoLista<V> atual = primeiro;
        int i = 1;
        while (atual != null) {
            sb.append(String.format("%d. %s\n", i++, atual.getItem().toString()));
            atual = atual.getProximo();
        }
        return sb.toString();
    }
    
    // --- MÉTODOS DE ORDENAÇÃO ---
    
    /** Ordena a playlist usando o método Iterativo (Bubble Sort na lista duplamente encadeada). */
    public void ordenarIterativo(Comparator<V> comparador) {
        if (tamanho < 2) return;
        boolean trocou;
        NoLista<V> fim = null;
        do {
            trocou = false;
            NoLista<V> atual = primeiro;
            while (atual.getProximo() != fim) {
                NoLista<V> proximoNo = atual.getProximo();
                if (comparador.compare(atual.getItem(), proximoNo.getItem()) > 0) {
                    // Lógica complexa de ajuste de ponteiros para a troca de nós
                    if (atual.getAnterior() != null) atual.getAnterior().setProximo(proximoNo); else primeiro = proximoNo;
                    if (proximoNo.getProximo() != null) proximoNo.getProximo().setAnterior(atual); else ultimo = atual;
                    atual.setProximo(proximoNo.getProximo());
                    proximoNo.setAnterior(atual.getAnterior());
                    proximoNo.setProximo(atual);
                    atual.setAnterior(proximoNo);
                    trocou = true;
                    NoLista<V> temp = proximoNo.getAnterior();
                    proximoNo = atual;
                    atual = temp != null ? temp : primeiro;
                } else {
                    atual = atual.getProximo();
                }
            }
            fim = atual; 
        } while (trocou);
    }
    
    /** Ordena a playlist usando o método por Particionamento (QuickSort em array auxiliar). */
    public void ordenarParticionamento(Comparator<V> comparador) {
        if (tamanho < 2) return;
        
        // 1. Copiar os itens para um array
        @SuppressWarnings("unchecked")
        V[] arrayMusicas = (V[]) new Musica[tamanho];
        NoLista<V> atual = primeiro;
        for (int i = 0; i < tamanho; i++) {
            arrayMusicas[i] = atual.getItem();
            atual = atual.getProximo();
        }

        // 2. Aplicar QuickSort
        quickSort(arrayMusicas, 0, tamanho - 1, comparador);
        
        // 3. Reconstruir a lista
        primeiro = null;
        ultimo = null;
        tamanho = 0;
        for (V musica : arrayMusicas) {
            adicionar(musica);
        }
    }

    private void quickSort(V[] arr, int baixo, int alto, Comparator<V> comparador) {
        if (baixo < alto) {
            int indicePivot = particionar(arr, baixo, alto, comparador);
            quickSort(arr, baixo, indicePivot - 1, comparador);
            quickSort(arr, indicePivot + 1, alto, comparador);
        }
    }

    private int particionar(V[] arr, int baixo, int alto, Comparator<V> comparador) {
        V pivot = arr[alto];
        int i = (baixo - 1);

        for (int j = baixo; j < alto; j++) {
            if (comparador.compare(arr[j], pivot) <= 0) {
                i++;
                V temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
        }
        V temp = arr[i + 1]; arr[i + 1] = arr[alto]; arr[alto] = temp;
        return i + 1;
    }
}