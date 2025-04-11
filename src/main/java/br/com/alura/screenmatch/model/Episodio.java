package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.DateTimeException;
import java.time.LocalDate;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private double avaliacao;
    private LocalDate dataLancamento;
    @ManyToOne
    private Serie serie;

    public Episodio() {}

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try {
            this.avaliacao = Double.parseDouble(dadosEpisodio.avaliacao());

        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());

        } catch (DateTimeException e) {
            this.dataLancamento = null;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public Integer getNumero() {
        return numeroEpisodio;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "Episodio{" +
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento +
                '}';
    }
}
