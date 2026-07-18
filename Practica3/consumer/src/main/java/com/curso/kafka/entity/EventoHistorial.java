package com.curso.kafka.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.UUID;

public class EventoHistorial extends PanacheMongoEntity {

    public UUID eventoId;
    public String alumnoId;
    public String tipo;
    public String cursoId;
    public String detalle;
    public Instant timestamp;

    @BsonProperty("procesadoEn")
    public Instant procesadoEn;

    public EventoHistorial() {
    }
}
