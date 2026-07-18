package com.curso.kafka.repository;

import com.curso.kafka.entity.EventoHistorial;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventoHistorialRepository implements PanacheMongoRepository<EventoHistorial> {
}