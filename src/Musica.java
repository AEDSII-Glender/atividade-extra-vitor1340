public class Musica implements Comparable<Musica> {
    
    private int id;
    private String titulo;
    private String artista;
    private double duracao; // Duração em minutos (decimal)

    public Musica(int id, String titulo, String artista, double duracao) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.duracao = duracao;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public double getDuracao() { return duracao; }

    @Override
    public String toString() {
        return String.format("ID: %d | Título: %s | Artista: %s | Duração: %.2f min", 
                             id, titulo, artista, duracao);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Musica outraMusica = (Musica) obj;
        return id == outraMusica.id; // Músicas são consideradas iguais se tiverem o mesmo ID
    }

    @Override
    public int compareTo(Musica outra) {
        return this.titulo.compareTo(outra.titulo); // Comparação padrão por Título
    }
}