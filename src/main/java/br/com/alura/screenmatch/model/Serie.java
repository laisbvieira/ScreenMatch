package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String urlPoster;
    private String sinopse;
    private String atores;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie() {}

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.urlPoster = dadosSerie.urlPoster();
//        this.sinopse = ConsultaChatGPT.obterTraducao(dadosSerie.sinopse()).trim();
//        this.sinopse = dadosSerie.sinopse();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
        this.atores = dadosSerie.atores();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {

        return String.format(
                """
                ━━━━━━━━━━━━ 📺 Série ━━━━━━━━━━━━
                🌟 Título: %s
                🎭 Gênero: %s
                📆 Temporadas: %d
                📊 Avaliação: %.1f
                🧑‍🤝‍🧑 Atores: %s
                📝 Sinopse:
                %s    
                🖼️ Poster: %s
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                📺 Episódios:
                %s
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """,
                titulo,
                genero,
                totalTemporadas,
                avaliacao,
                atores,
                sinopse,
                urlPoster,
                episodios
        );
    }

}
