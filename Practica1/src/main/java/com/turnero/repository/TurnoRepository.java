package com.turnero.repository;

import com.turnero.entity.Especialidad;
import com.turnero.entity.EstadoTurno;
import com.turnero.entity.Turno;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class TurnoRepository implements PanacheRepository<Turno> {

    public Uni<List<Turno>> listarTodos() {
        return listAll();
    }

    public Uni<Turno> buscarPorId(Long id) {
        return findById(id);
    }

    public Uni<List<Turno>> buscarPorPacienteId(Long pacienteId) {
        return list("pacienteId", pacienteId);
    }

    public Uni<List<Turno>> buscarPorEspecialidadYEstado(Especialidad especialidad, EstadoTurno estado) {
        return list("especialidad = ?1 and estado = ?2", especialidad, estado);
    }

    public Uni<Boolean> existeConflicto(Long pacienteId, LocalDate fecha, LocalTime hora) {
        return count("pacienteId = ?1 and fecha = ?2 and hora = ?3 and estado != ?4",
                pacienteId, fecha, hora, EstadoTurno.CANCELADO)
                .map(c -> c > 0);
    }
}