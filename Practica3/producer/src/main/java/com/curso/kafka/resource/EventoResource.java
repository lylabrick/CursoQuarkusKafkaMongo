package com.curso.kafka.resource;

import com.curso.kafka.dto.EventoResponse;
import com.curso.kafka.model.EventoEducativo;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.Instant;
import java.util.UUID;

@Path("/eventos")
public class EventoResource {

    @Inject
    @Channel("eventos-educativos-out")
    Emitter<EventoEducativo> emitter;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response publicar(@Valid EventoEducativo evento) {
        if (evento.getId() == null) {
            evento.setId(UUID.randomUUID());
        }
        if (evento.getTimestamp() == null) {
            evento.setTimestamp(Instant.now());
        }

        emitter.send(evento);

        return Response.accepted(new EventoResponse(evento.getId(), "PUBLICADO")).build();
    }
}
