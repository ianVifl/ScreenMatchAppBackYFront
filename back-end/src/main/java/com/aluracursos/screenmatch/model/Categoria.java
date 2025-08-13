package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action", "Accion") ,
    ROMANCE ("Romance","Romance"),
    COMEDIA ("Comedy","Comedia"),
    DRAMA ("Drama","Drama"),
    CRIMEN("Crime","Crimen");

    private String categoriaOmdb;
    private String categoriaEspanol;

    Categoria (String categoriaOmdb,String categoriaEspanol){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol=categoriaEspanol;
    }

    //VERIFICA SI EL TEXTO ES IGUAL O PUEDE SER IGUALADO A ALGO QUE NOSOTROS TENEMOS
    public static Categoria fromString (String text){ //recibe un string para transformarlo en tipo de dato Categoria
        for (Categoria categoria : Categoria.values()){ //trae los valores de categoría
            if(categoria.categoriaOmdb.equalsIgnoreCase(text)){ //si la categoría coincide con las nuestras entonces retornarla
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: "+text); //y si no , no encontró la categoría
    }


    //VERIFICA SI EL TEXTO ES IGUAL O PUEDE SER IGUALADO A ALGO QUE NOSOTROS TENEMOS
    public static Categoria fromEspanol (String text){ //recibe un string para transformarlo en tipo de dato Categoria
        for (Categoria categoria : Categoria.values()){ //trae los valores de categoría
            if(categoria.categoriaEspanol.equalsIgnoreCase(text)){ //si la categoría coincide con las nuestras entonces retornarla
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: "+text); //y si no , no encontró la categoría
    }
}
