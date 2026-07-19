package com.curso.kafka.model;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private UUID id;

    @NotNull
    private String alumnoId;

    @NotNull
    private TipoEvento tipo;

    @NotNull
    private String cursoId;

    private String detalle;

    @NotNull
    private Instant timestamp;

}