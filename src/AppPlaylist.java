import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Scanner;

public class AppPlaylist {

    static ListaDupla<Musica> playlist;
    static Historico<Musica> historico;
    static Scanner teclado;
    static ABB<Double, Musica> buscadorPorDuracao; 
    
    // Catálogo inicial de músicas disponíveis (simulação do catálogo global)
    static Musica[] catalogoMusicas = {
        new Musica(101, "Bohemian Rhapsody", "Queen", 5.55),
        new Musica(102, "Stairway to Heaven", "Led Zeppelin", 8.02),
        new Musica(103, "Imagine", "John Lennon", 3.04),
        new Musica(104, "Smells Like Teen Spirit", "Nirvana", 5.01),
        new Musica(105, "Like a Rolling Stone", "Bob Dylan", 6.13),
        new Musica(106, "Hotel California", "Eagles", 6.30),
        new Musica(107, "Hey Jude", "The Beatles", 7.11),
        new Musica(108, "What a Wonderful World", "Louis Armstrong", 2.18),
        new Musica(109, "Yesterday", "The Beatles", 2.05),
        new Musica(110, "Thriller", "Michael Jackson", 5.57),
    };

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("\nPressione ENTER para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("🎵 SPOTIFY - GERENCIADOR DE PLAYLIST 🎧");
        System.out.println("======================================");
    }
   
    static int menu() {
        cabecalho();
        System.out.println("1 - Adicionar Música à Playlist");
        System.out.println("2 - Remover Música da Playlist");
        System.out.println("3 - Ordenar Playlist (Iterativo/Particionamento)");
        System.out.println("4 - Reproduzir Música na ordem");
        System.out.println("5 - Reproduzir Música aleatoriamente");
        System.out.println("6 - Exibir Playlist Atual");
        System.out.println("7 - Exibir Histórico de Reprodução");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        
        try { return Integer.parseInt(teclado.nextLine()); } 
        catch (NumberFormatException e) { return -1; }
    }
    
    static void adicionarMusica() {
        cabecalho();
        System.out.println("--- MÚSICAS DISPONÍVEIS NO BUSCADOR (Ordenadas por Duração) ---");
        // Exibe o conteúdo da ABB (buscador) em ordem (pela chave: Duração)
        System.out.println(buscadorPorDuracao.percorrer()); 
        
        System.out.print("Digite o ID da música que deseja adicionar à playlist: ");
        int idAdicionar;
        try { idAdicionar = Integer.parseInt(teclado.nextLine()); } 
        catch (NumberFormatException e) { System.out.println("ID inválido."); return; }

        // Busca a música no catálogo simulado (como se estivesse buscando no buscador)
        Musica musicaParaAdicionar = null;
        for (Musica m : catalogoMusicas) { 
            if (m.getId() == idAdicionar) {
                musicaParaAdicionar = m;
                break;
            }
        }
        
        if (musicaParaAdicionar != null) {
            playlist.adicionar(musicaParaAdicionar);
            System.out.println("✅ Música adicionada à playlist: " + musicaParaAdicionar.getTitulo());
        } else {
            System.out.println("❌ Música com ID " + idAdicionar + " não encontrada no catálogo.");
        }
    }

    static void removerMusica() {
        cabecalho();
        System.out.println("--- PLAYLIST ATUAL ---");
        System.out.println(playlist);
        System.out.print("Digite o ID ou o TÍTULO da música que deseja remover: ");
        String entrada = teclado.nextLine();
        
        Musica musicaRemovida = null;
        try {
            int idRemover = Integer.parseInt(entrada);
            musicaRemovida = playlist.remover(idRemover);
        } catch (NumberFormatException e) {
            // Tenta remover por título
            Musica musica = playlist.buscarPorTitulo(entrada);
            if (musica != null) musicaRemovida = playlist.remover(musica.getId());
        }

        if (musicaRemovida != null) {
            System.out.println("✅ Música removida: " + musicaRemovida.getTitulo());
        } else {
            System.out.println("❌ Música não encontrada na playlist.");
        }
    }
    
    static void ordenarPlaylist() {
        cabecalho();
        System.out.println("--- OPÇÕES DE ORDENAÇÃO ---");
        System.out.println("1 - Ordenar por Título");
        System.out.println("2 - Ordenar por Artista");
        System.out.println("3 - Ordenar por Duração");
        System.out.print("Escolha o critério (1-3): ");
        String criterio = teclado.nextLine();

        System.out.println("\n--- OPÇÕES DE MÉTODO ---");
        System.out.println("I - Método Iterativo (Bubble Sort)");
        System.out.println("P - Método por Particionamento (QuickSort)");
        System.out.print("Escolha o método (I/P): ");
        String metodo = teclado.nextLine().toUpperCase();

        Comparator<Musica> comparador;
        switch (criterio) {
            case "1": comparador = Comparator.comparing(Musica::getTitulo); break;
            case "2": comparador = Comparator.comparing(Musica::getArtista); break;
            case "3": comparador = Comparator.comparing(Musica::getDuracao); break;
            default: System.out.println("❌ Critério inválido."); return;
        }

        if ("I".equals(metodo)) {
            playlist.ordenarIterativo(comparador);
            System.out.println("✅ Ordenação iterativa (Bubble Sort) concluída.");
        } else if ("P".equals(metodo)) {
            playlist.ordenarParticionamento(comparador);
            System.out.println("✅ Ordenação por particionamento (QuickSort) concluída.");
        } else {
            System.out.println("❌ Método de ordenação inválido.");
        }
    }

    static void reproduzirEmOrdem() {
        if (playlist.vazia()) { System.out.println("Playlist vazia!"); return; }

        NoLista<Musica> atual; 
        cabecalho();
        System.out.println("Deseja iniciar no (I)nício ou (F)im da playlist?");
        String inicio = teclado.nextLine().toUpperCase();
        
        if (inicio.equals("I")) atual = playlist.getPrimeiro();
        else if (inicio.equals("F")) atual = playlist.getUltimo();
        else { System.out.println("Opção inválida."); return; }
        
        String opcao;
        do {
            if (atual == null) {
                System.out.println("Playlist finalizada. Deseja (R)einiciar ou (V)oltar ao menu?");
                opcao = teclado.nextLine().toUpperCase();
                if (opcao.equals("R")) { 
                    atual = playlist.getPrimeiro();
                    if (atual == null) break; // Sai se a playlist foi esvaziada
                    continue; 
                }
                else break;
            }
            
            Musica musicaAtual = atual.getItem();
            System.out.printf("Reproduzindo: %s - %s\n", musicaAtual.getTitulo(), musicaAtual.getArtista());
            historico.adicionar(musicaAtual); // Salva no histórico
            
            System.out.println("\n(P)róxima música, (A)nterior música, (S)air da reprodução?");
            opcao = teclado.nextLine().toUpperCase();

            if (opcao.equals("P")) {
                if (atual.getProximo() != null) atual = atual.getProximo(); 
                else atual = null; // Chegou ao fim
            } else if (opcao.equals("A")) {
                if (atual.getAnterior() != null) atual = atual.getAnterior(); 
                else System.out.println("Você está no início da playlist.");
            } else if (opcao.equals("S")) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        } while (true);
    }

    static void reproduzirAleatoriamente() {
        if (playlist.vazia()) { System.out.println("Playlist vazia!"); return; }

        String opcao;
        do {
            cabecalho();
            System.out.println("--- PLAYLIST ATUAL ---");
            System.out.println(playlist);
            
            System.out.println("Digite o ID da próxima música a reproduzir, (H)istórico, ou (S)air:");
            opcao = teclado.nextLine();
            
            if (opcao.toUpperCase().equals("S")) break;
            
            if (opcao.toUpperCase().equals("H")) { navegarHistorico(); continue; }

            try {
                int idReproduzir = Integer.parseInt(opcao);
                Musica musica = playlist.buscarPorId(idReproduzir);
                
                if (musica != null) {
                    System.out.printf("Reproduzindo: %s - %s\n", musica.getTitulo(), musica.getArtista());
                    historico.adicionar(musica); // Salva no histórico
                } else {
                    System.out.println("❌ Música com ID " + idReproduzir + " não encontrada na playlist.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
            }
            pausa();
        } while (true);
    }
    
    static void navegarHistorico() {
        cabecalho();
        System.out.println("--- NAVEGAÇÃO NO HISTÓRICO ---");
        System.out.println(historico);
        System.out.println("(V)oltar Reprodução (música anterior) ou qualquer tecla para (S)air:");
        String opcao = teclado.nextLine().toUpperCase();
        
        if (opcao.equals("V")) {
            Musica anterior = historico.voltarReproducao();
            if (anterior != null) System.out.println("Voltando para: " + anterior.getTitulo() + " - " + anterior.getArtista());
            else System.out.println("Você está na reprodução mais antiga do histórico.");
        }
    }

    static void exibirPlaylist() {
        cabecalho();
        System.out.println("--- PLAYLIST ATUAL ---");
        System.out.println(playlist);
    }
    
    static void exibirHistorico() {
        cabecalho();
        System.out.println(historico);
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        playlist = new ListaDupla<>();
        historico = new Historico<>();
        
        // Inicializa o Buscador (ABB)
        // Usa um comparador de Double para ordenar pela Duração (Chave)
        buscadorPorDuracao = new ABB<>(Comparator.comparing(Double::doubleValue));
        for (Musica m : catalogoMusicas) {
            buscadorPorDuracao.inserir(m.getDuracao(), m);
        }

        int opcao = -1;
      
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> adicionarMusica();
                case 2 -> removerMusica();
                case 3 -> ordenarPlaylist();
                case 4 -> reproduzirEmOrdem();
                case 5 -> reproduzirAleatoriamente();
                case 6 -> exibirPlaylist();
                case 7 -> exibirHistorico();
                case 0 -> System.out.println("Obrigado por usar o sistema! 🎶");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
            if (opcao != 0) pausa();
        } while (opcao != 0);       

        teclado.close();    
    }
}