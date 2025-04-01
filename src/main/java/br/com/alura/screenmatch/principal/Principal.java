package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverterDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);

    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverterDados conversor = new ConverterDados();

    public void exibirMenu() {
        System.out.println("digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        String ENDERECO = "https://www.omdbapi.com/?t=";
        String API_KEY = "&apikey=fa0b9710";
        String url = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        var json = consumo.obterDados(url);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <=dados.totalTemporadas() ; i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);

            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);

        for (DadosTemporada temporada : temporadas) {
            List<DadosEpisodio> episodiosTemporada = temporada.episodios();
            for (DadosEpisodio dadosEpisodio : episodiosTemporada) {
                System.out.println(dadosEpisodio.titulo());
            }
        }

        //lambdas
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        temporadas.forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .toList();

        System.out.println("--------- TOP 5 EPISÓDIOS ----------");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek( e -> System.out.println("primeiro filtro(N/A " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek( e -> System.out.println("ordenacao " + e))
                .limit(5)
//                .peek( e -> System.out.println("limite " + e))
                .map(e -> e.titulo().toUpperCase())
//                .peek( e -> System.out.println("mapeamento " + e))
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).toList();

        episodios.forEach(System.out::println);

            //busca episódio
        System.out.println("digite um trecho do título do episódio desejado");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
                .findFirst();

        if (episodioBuscado.isPresent()) {
            System.out.println("episódio encontrado!");
            System.out.println("episódio: " + episodioBuscado.get());
        } else {
            System.out.println("episódio não encontrado");
        }


        System.out.println("a partir de que ano você quer visualizar os episódios?");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e-> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "temporada: " + e.getTemporada() +
                                ", episódio: " + e.getNumero() +
                                ", data de lançamento: " + e.getDataLancamento().format(formatador)
                ));

        //avaliação por temporada
        System.out.println("--------- AVALIAÇÃO POR TEMPORADA ----------");
        Map<Integer, Double>  avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("média: " + est.getAverage());
        System.out.println("melhor episódio: " + est.getMax());
        System.out.println("pior episódio: " + est.getMin());
        System.out.println("quantidade: " + est.getCount());
    }
}
