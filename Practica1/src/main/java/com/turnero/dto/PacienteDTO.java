package com.turnero.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class PacienteDTO {
    @NotBlank(message = "El nombre es obligatorio")
    public String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    public String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    public String dni;

    @NotBlank(message = "El email es obligatorio")
    public String email;

    public LocalDate fechaNacimiento;
}