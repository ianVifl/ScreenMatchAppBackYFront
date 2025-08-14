package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private ISerieRepository repository; //inyectamos la dependecia de nustro repository

    public List<SerieDTO> obtenerTodasLasSeries(){
        return convierteDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convierteDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie){
        return serie.stream().map(s -> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getEvaluacion(),s.getPoster(),s.getGenero(),s.getSinopsis()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obtenerLanzamientosMasRecientes(){
        return convierteDatos(repository.lanzamientosMasRecientes());
    }

    // Clase SerieService
    public SerieDTO obtenerPorId(Long id) {
        // Busca la serie por su ID en el repositorio. El resultado se envuelve en un Optional
        // para manejar el caso de que la serie no exista.
        Optional<Serie> serie = repository.findById(id);

        // Verifica si el Optional contiene una serie (es decir, si la búsqueda tuvo éxito).
        if (serie.isPresent()){
            // Si la serie existe, se extrae del Optional.
            Serie s = serie.get();
            // Se crea un nuevo DTO (Objeto de Transferencia de Datos)
            // para devolver solo la información necesaria.
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(),
                    s.getEvaluacion(), s.getPoster(), s.getGenero(), s.getSinopsis());
        }
        // Si la serie no fue encontrada, se devuelve 'null'.
        return null;
    }
}
