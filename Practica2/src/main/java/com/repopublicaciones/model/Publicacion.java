package com.repopublicaciones.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.util.List;

@MongoEntity(collection = "publicaciones")
public class Publicacion extends ReactivePanacheMongoEntity {

    public String titulo;
    public String resumen;
    public List<String> autores;
    public List<String> palabrasClave;
    public TipoPublicacion tipo;
    public Integer anio;
    public String url;
    public Integer citaciones;
    public Visibilidad visibilidad;
}