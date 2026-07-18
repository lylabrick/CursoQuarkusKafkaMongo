package com.curso.kafka.consumer;

import com.curso.kafka.model.EventoEducativo;
import com.curso.kafka.model.TipoEvento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AlertaDocenteConsumer {

    private static final Logger LOG = Logger.getLogger(AlertaDocenteConsumer.class);

    @Inject
    @Channel("alertas-docentes-out")
    Emitter<EventoEducativo> alertaEmitter;

    @Inject
    @Channel("eventos-fallidos-out")
    Emitter<EventoEducativo> dlqEmitter;

    @Incoming("alertas-in")
    public void procesar(EventoEducativo evento) {
        try {
            if (evento.getTipo() != TipoEvento.EXAMEN_DESAPROBADO) {
                return;
            }

            alertaEmitter.send(evento);

            LOG.warnf("Alerta generada para docente: alumno=%s curso=%s detalle=%s",
                    evento.getAlumnoId(), evento.getCursoId(), evento.getDetalle());
        } catch (Exception e) {
            LOG.errorf(e, "Error al generar alerta docente, enviando a DLQ: id=%s", evento.getId());
            dlqEmitter.send(evento);
        }
    }
}