package com.turnero.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Paciente extends PanacheEntity {

    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public String apellido;

    @Column(nullable = false, unique = true)
    public String dni;

    @Column(nullable = false, unique = true)
    public String email;

    public LocalDate fechaNacimiento;
}