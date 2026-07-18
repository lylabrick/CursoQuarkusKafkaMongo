package com.curso.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoEducativo {
    private UUID id;
    private String alumnoId;
    private TipoEvento tipo;
    private String cursoId;
    private String detalle;
    private Instant timestamp;

    @Override
    public String toString() {
        return "EventoEducativo{" +
                "id=" + id +
                ", alumnoId='" + alumnoId + '\'' +
                ", tipo=" + tipo +
                ", cursoId='" + cursoId + '\'' +
                ", detalle='" + detalle + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
