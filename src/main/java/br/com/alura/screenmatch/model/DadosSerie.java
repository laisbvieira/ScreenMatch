package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//categoria, imagem do poster, sinopse


@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Poster") String urlPoster,
                         @JsonAlias("Plot") String sinopse,
                         @JsonAlias("Actors") String atores) {

    public String toString() {
        return String.format(
                """
                📺 Série: %s
                🎭 Gênero: %s
                🎬 Atores: %s
                📆 Total de Temporadas: %d
                ✪  Avaliação IMDb: %s
                📝 Sinopse: %s
                🖼️ Poster: %s
                """,
                titulo, genero, atores, totalTemporadas, avaliacao, sinopse, urlPoster
        );
    }




}
