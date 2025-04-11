package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverterDados;

import java.util.*;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private final List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    private List<Serie> series = new ArrayList<>();

    public void exibirMenu() {

        var opcao = -1;
        while(opcao != 0) {
            var menu = """
               \s
                1 - Buscar séries
                2 - Buscar episódios
                3 - Visualizar séries buscadas
                4 - Buscar série por título
                5 - Buscar séries por ator/atriz
                6 - Buscar top 5 séries
                7 - Buscar séries por gênero
                8 - Buscar séries por temporadas e avaliação
                9 - Buscar episódios por trecho
                10 - Buscar top 5 episódios de uma série
               \s   
                0 - Sair                                \s
               \s""";

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    listarTop5();
                    break;
                case 7:
                    buscarSeriesPorGenero();
                    break;
                case 8:
                    buscarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodiosPorTrecho();
                    break;
                case 10:
                    listarTop5Episodios();
                    break;
                case 11:

                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        dadosSeries.add(dados);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
//        DadosSerie dadosSerie = getDadosSerie();
        System.out.println("Escolha uma série pelo nome");
        var nomeSerie = leitura.nextLine();

//        Optional<Serie> serie = series.stream()
//                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
//                .findFirst();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            Serie serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream()
                        .map(e-> new Episodio(t.numero(), e)))
                .toList();
        serieEncontrada.setEpisodios(episodios);
        repositorio.save(serieEncontrada);
        
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {

        series = repositorio.findAll();
//        series = dadosSeries.stream()
//                        .map(Serie::new)
//                .toList();
//
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo título");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            Serie serieEncontrada = serieBusca.get();
            System.out.println(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do autor/da atriz");
        var ator = leitura.nextLine();

        System.out.println("serão as séries com a avaliação a partir de qual nota?");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(ator.trim(), avaliacao);

        if (!seriesEncontradas.isEmpty()) {
            System.out.println(seriesEncontradas);
        } else {
            System.out.println("série não encontrada!");
        }


    }

    private void listarTop5() {
        List<Serie> top5 = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println(top5);
    }

    private void buscarSeriesPorGenero() {
        System.out.println("escolha o gênero");
        String nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.categoriaPortugues(nomeGenero);
        List<Serie> series = repositorio.findByGenero(categoria);
        series.forEach(System.out::println);
    }

    private void buscarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Quantas temporadas?");
        Integer temporadas = leitura.nextInt();
        System.out.println("Qual avaliação?");
        var avaliacao = leitura.nextDouble();

        List<Serie> series = repositorio.seriesPorTemporadaEAvaliacao(temporadas, avaliacao);
        series.forEach(System.out::println);
    }

    private void buscarEpisodiosPorTrecho() {
        System.out.println("qual o nome do episódio?");
        var episodio = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(episodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumero(), e.getTitulo()));
    }

    private void listarTop5Episodios() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> episodios = repositorio.topEpisodiosPorSerie(serie);
            episodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumero(), e.getTitulo()));
;        }


    }
}