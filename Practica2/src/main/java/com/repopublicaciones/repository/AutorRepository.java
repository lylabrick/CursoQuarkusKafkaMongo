package com.repopublicaciones.repository;

import com.repopublicaciones.model.Autor;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AutorRepository implements ReactivePanacheMongoRepository<Autor> {
}