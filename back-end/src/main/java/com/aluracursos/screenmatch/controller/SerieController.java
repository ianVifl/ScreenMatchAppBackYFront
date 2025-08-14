package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController //indicamos que trabajamos con el modelo Rest
@RequestMapping ("/series")
public class SerieController {

    @Autowired
    private SeriesService servicio;


    @GetMapping() //GET(trae) (/series) <- ese es el "endpoint"
    public List<SerieDTO> obtenerTodasLasSeries() {
        return servicio.obtenerTodasLasSeries();

    }

    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5(){
        return servicio.obtenerTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtenerLanzaminetosMasRecientes(){
        return servicio.obtenerLanzamientosMasRecientes();
    }

    @GetMapping("/{id}")// La anotación @GetMapping("/{id}") indica que este método maneja peticiones GET
    // a una URL como /series/5, donde '5' es el ID.
    public SerieDTO obtenerPorId(@PathVariable Long id){
        // La anotación @PathVariable extrae el valor del ID de la URL.
        // Se llama al servicio para obtener la serie por su ID y se devuelve el DTO resultante.
        return servicio.obtenerPorId(id);
    }
}
