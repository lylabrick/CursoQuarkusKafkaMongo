package com.curso.kafka.consumer;

import com.curso.kafka.model.EventoEducativo;
import com.curso.kafka.model.TipoEvento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

@ApplicationScoped
public class CertificadoConsumer {

    private static final Logger LOG = Logger.getLogger(CertificadoConsumer.class);

    @Inject
    @Channel("eventos-fallidos-out")
    Emitter<EventoEducativo> dlqEmitter;

    @Incoming("certificados-in")
    public void procesar(EventoEducativo evento) {
        try {
            if (evento.getTipo() != TipoEvento.CERTIFICADO_EMITIDO) {
                return;
            }

            MDC.put("eventoId", evento.getId().toString());
            MDC.put("alumnoId", evento.getAlumnoId());
            MDC.put("cursoId", evento.getCursoId());

            LOG.infof("Certificado emitido: alumno=%s curso=%s detalle=%s timestamp=%s",
                    evento.getAlumnoId(), evento.getCursoId(), evento.getDetalle(), evento.getTimestamp());

            MDC.clear();
        } catch (Exception e) {
            LOG.errorf(e, "Error al registrar certificado, enviando a DLQ: id=%s", evento.getId());
            dlqEmitter.send(evento);
        }
    }
}