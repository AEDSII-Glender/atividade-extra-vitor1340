public class NoLista<V> {
    
    private V item;
    private NoLista<V> proximo;
    private NoLista<V> anterior;

    public NoLista(V item) {
        this.item = item;
        this.proximo = null;
        this.anterior = null;
    }

    // Getters e Setters
    public V getItem() { return item; }
    public NoLista<V> getProximo() { return proximo; }
    public void setProximo(NoLista<V> proximo) { this.proximo = proximo; }
    public NoLista<V> getAnterior() { return anterior; }
    public void setAnterior(NoLista<V> anterior) { this.anterior = anterior; }
}