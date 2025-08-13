package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //indicamos que trabajamos con el modelo Rest
public class SerieController {
    @Autowired
    private ISerieRepository repository; //inyectamos la dependecia de nustro repository

    @GetMapping("/series") //GET(trae) (/series) <- ese es el "endpoint"
    public List<Serie> obtenerTodasLasSeries(){
        return repository.findAll(); //traemos todo lo que contenga
    }

}
