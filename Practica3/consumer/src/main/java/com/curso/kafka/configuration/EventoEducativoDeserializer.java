package com.curso.kafka.configuration;

import com.curso.kafka.model.EventoEducativo;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class EventoEducativoDeserializer extends ObjectMapperDeserializer<EventoEducativo> {

    public EventoEducativoDeserializer() {
        super(EventoEducativo.class);
    }
}