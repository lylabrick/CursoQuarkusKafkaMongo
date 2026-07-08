package com.turnero.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Turno extends PanacheEntity {

    @Column(nullable = false)
    public Long pacienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Especialidad especialidad;

    @Column(nullable = false)
    public LocalDate fecha;

    @Column(nullable = false)
    public LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EstadoTurno estado;
}