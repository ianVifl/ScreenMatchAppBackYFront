package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=e010b7cc";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();

    private ISerieRepository repositorio;

    private List<Serie> series;// hacemos la variabel global

    private Optional<Serie> serieBuscada;

    public Principal(ISerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por Titulo
                    5 . Top 5 Mejores series
                    6 - Buscar series por categoria
                    7 - Buscar serie por numero de temporadas
                    8 - Buscar episodios por nombre
                    9 - Top 5 Episodios 
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    //buscarSeriesPorNumeroTemporada();
                    buscaTotalTemporadasYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorNombre();
                    break;
                case 9 :
                    buscarTop5Episodios();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }



    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        //DatosSerie datosSerie = getDatosSerie(); hace la requisicion en nuestra API
        mostrarSeriesBuscadas(); //almacenadas en nuestra BD
        System.out.println("Escribe el nombre de la series de la cualq uieres ver lo episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s-> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada = serie.get(); //en caso de encontrarla almacenar en serie y pueda acceder a esos datos con get

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios =temporadas.stream()
                    .flatMap(d->d.episodios().stream()
                            .map(e -> new Episodio(d.numero(),e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }

    private void buscarSerieWeb() {
        var datos = getDatosSerie(); //trae los datos del método
        //datosSeries.add(datos);//adicionamos los datos a la lista 'datosSerie'
        Serie serie = new Serie(datos); //usamos ya la interfaz que creamos para poder guardar los datos
        repositorio.save(serie); //usamos ya un metodo de repository para guardar esa serie con sus datos
    }

    private void mostrarSeriesBuscadas() {
         series = repositorio.findAll(); //usando la nueva interfaz "ISerieRepository",
        // utlilizamos el findAll para traer la info de la BD y ya no utilizamos el stream()

//                new ArrayList<>();
//        series = datosSeries.stream()
//                .map(d-> new Serie(d))
//                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();


        //Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        //convertimos el Optional a una variable global ya que la ocuparemos en otro metodo (buscarEpisodiosPorNombre)
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es : "+serieBuscada.get());
        }else{
            System.out.println("Serie no encontrada ");
        }
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s-> System.out.println("Serie : "+ s.getTitulo()+"Evaluacion: "+ s.getEvaluacion()));
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Escribe el genero/categoria de la serie que deseas buscar");
        var genero= teclado.nextLine();

        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria= repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria "+genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriesPorNumeroTemporada(){
        System.out.println("Numero de temporadas:");
        var numeroTemporadas = teclado.nextInt();
        List<Serie> seriesPorTemporadas = repositorio.findBytotalTemporadas(numeroTemporadas);
        System.out.println("Las series con "+ numeroTemporadas+ " temporadas son :");
        seriesPorTemporadas.forEach(System.out::println);

    }

    private void buscarSeriePorEvaluacion(){
        System.out.println("Digita la evaluacion de la serie a buscar :");
        var evaluacionDigitada = teclado.nextDouble();
        Optional<Serie> seriesPorEvaluacion = repositorio.findByEvaluacion(evaluacionDigitada);

        if(seriesPorEvaluacion.isPresent()){
            System.out.println("Las series con "+evaluacionDigitada+" evaluacion son :"+seriesPorEvaluacion.get());
        }else {
            System.out.println("No se encontraron series con "+evaluacionDigitada+" de evaluacion.");
        }
    }

    private void buscaTotalTemporadasYEvaluacion(){
        System.out.println("Total de temporadas:");
        var temp = teclado.nextInt();
        System.out.println("Evaluacion de la serie a buscar:");
        var evalua = teclado.nextDouble();
        List<Serie> serie = repositorio.seriesPorTemporadaYEvaluacion(temp,evalua);
        serie.forEach(System.out::println);
    }

    private void buscarEpisodiosPorNombre(){
        System.out.println("NOmbre del episodio que quieres buscar :");
        var nombreEpisodio= teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie : %s Temporada %s Episodio %s Evaluacion %s \n",
                        e.getSerie(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
    }


    private void buscarTop5Episodios() {
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie : %s Temporada %s Episodio %s Evaluacion %s \n",
                            e.getSerie(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
        }
    }


}

