package com.aluracursos.screenmatch.model;

import com.aluracursos.screenmatch.service.ConsultaGemini;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;


@Entity // indicamos a JPA que va a ser una Entidad en mi base e datos
@Table(name = "series") //indicamos el nombre de la tabla en nuestra BD pq es diferente a el de esta clase es decir sin la S


public class Serie {
    @Id// indicamos a jpa que el sig es el Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//genera un id autoincremental automaticamente
    private long Id;// id para la bd

    @Column(unique = true) //hacemos unico el atributo título
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;

    @Enumerated(EnumType.STRING) //como es un Enum , indicamos que tipo de enum va a ser y sera para String porque toma el
    // texto y no la posicion ya que es el futuro puede variar la posicion por ejemplo de "accion"
    private Categoria genero; //enum creado como Categoria


    private String actores;
    private String sinopsis;


   // @Transient //existe pero no la utilizaremos ahora
    //indicamos la relación de la PK con la FK
    @OneToMany (mappedBy = "serie" ,cascade = CascadeType.ALL,fetch = FetchType.EAGER) //mapea la realcion a travez del campo "serie" de la clase Episodio
    // el cascade es para actaulizar si hay alguna actaulizacion en el codigo y tambien se refleje en BD
    // el fetch es como va a traer los datos , si precargados o cada vez que los solicitemos
    private List<Episodio>episodios;

    public Serie (){}//Jpa nos obliga a poner un contructor default para que funcione el código

    public Serie (DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        //
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        this.genero=Categoria.fromString(datosSerie.genero().split(",")[0]);
        this.actores = datosSerie.actores();
        this.sinopsis= ConsultaGemini.obtenerTraduccion(datosSerie.sinopsis());
    }



    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        //indicamos el id de cada serie para que con cada episodio asigne ese episodio a esa serie o a ese IdSerie
        episodios.forEach(e->e.setSerie(this)); //matcheamos la FK
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return
                "genero=" + genero +'\'' +
                "titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'' +
                ", sinopsis='" + sinopsis + '\''+
                ", episodios ='" + episodios + '\'';
    }
}

