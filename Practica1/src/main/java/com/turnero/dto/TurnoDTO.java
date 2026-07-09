package com.turnero.dto;

import com.turnero.entity.Especialidad;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoDTO {
    public Long pacienteId;
    public Especialidad especialidad;
    public LocalDate fecha;
    public LocalTime hora;
}