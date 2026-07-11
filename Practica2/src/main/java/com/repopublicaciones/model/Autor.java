package com.repopublicaciones.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.util.List;

@MongoEntity(collection = "autores")
public class Autor extends ReactivePanacheMongoEntity {

    public String nombre;
    public String apellido;
    public String orcid;
    public String institucion;
    public List<String> areasDeInteres;
}