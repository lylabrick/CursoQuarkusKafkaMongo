package com.repopublicaciones.config;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;

@ApplicationScoped
public class MongoIndexInitializer {

    @Inject
    ReactiveMongoClient client;

    void onStart(@Observes StartupEvent ev) {
        var collection = client.getDatabase("repositorio_academico").getCollection("publicaciones");
        collection.createIndex(new Document("palabrasClave", 1)).subscribe().with(id -> {});
        collection.createIndex(new Document("anio", 1)).subscribe().with(id -> {});
        collection.createIndex(new Document("tipo", 1)).subscribe().with(id -> {});
        var collectioAutores = client.getDatabase("repositorio_academico").getCollection("autores");
        collectioAutores.createIndex(new Document("orcid", 1)).subscribe().with(id -> {});
    }
}