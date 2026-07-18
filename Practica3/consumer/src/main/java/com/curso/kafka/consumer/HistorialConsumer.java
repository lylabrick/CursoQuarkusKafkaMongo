package com.curso.kafka.consumer;

import com.curso.kafka.entity.EventoHistorial;
import com.curso.kafka.model.EventoEducativo;
import com.curso.kafka.repository.EventoHistorialRepository;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.time.Instant;

@ApplicationScoped
public class HistorialConsumer {

    private static final Logger LOG = Logger.getLogger(HistorialConsumer.class);

    @Inject
    EventoHistorialRepository repository;

    @Inject
    @Channel("eventos-fallidos-out")
    Emitter<EventoEducativo> dlqEmitter;

    @Incoming("historial-in")
    @Blocking
    public void persistir(EventoEducativo evento) {
        try {
            EventoHistorial historial = new EventoHistorial();
            historial.eventoId = evento.getId();
            historial.alumnoId = evento.getAlumnoId();
            historial.tipo = evento.getTipo().name();
            historial.cursoId = evento.getCursoId();
            historial.detalle = evento.getDetalle();
            historial.timestamp = evento.getTimestamp();
            historial.procesadoEn = Instant.now();

            repository.persist(historial);

            LOG.infof("Evento persistido en historial: id=%s tipo=%s alumno=%s",
                    evento.getId(), evento.getTipo(), evento.getAlumnoId());
        } catch (Exception e) {
            LOG.errorf(e, "Error al persistir evento en historial, enviando a DLQ: id=%s", evento.getId());
            dlqEmitter.send(evento);
        }
    }
}