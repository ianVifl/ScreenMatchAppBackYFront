package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ISerieRepository extends JpaRepository<Serie,Long> {

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findBytotalTemporadas(Integer numeroTemporadas);

    Optional<Serie> findByEvaluacion(Double evaluacionDigitada);

    //NativeQuerys
    //@Query(value = "select * from series where series.total_temporadas <=6 AND series.evaluacion >=7.5;", nativeQuery = true)

    //Lenguaeje JPQL
    // s = representacion de la entidad; series = Nombre de mi clase NO DE TABLA y damos un alias "s"
    // total_temporadas = como se llama en la calse "totalTemporadas"
    // 6 y 7.5 = sustituye por variables que nosotros pasaremos y para diferenciar agregamos " : " para esas var que pasaremos
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas, double evaluacion);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio% ")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie  ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios (Serie serie );

}
