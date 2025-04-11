package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String atores, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria genero);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer numeroTemporadas, Double avaliacao);

//    @Query(value = "select * from series where series.total_temporadas <= 5 and series.avaliacao >= 7.5", nativeQuery = true)
    @Query("select s from Serie s WHERE s.totalTemporadas <= :numeroTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer numeroTemporadas, Double avaliacao);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE LOWER(e.titulo) ILIKE %:trecho%")
    List<Episodio> episodiosPorTrecho(String trecho);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);


}
