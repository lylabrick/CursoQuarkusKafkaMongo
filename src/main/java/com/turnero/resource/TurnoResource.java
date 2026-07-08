package com.turnero.resource;

import com.turnero.dto.CambioEstadoDTO;
import com.turnero.dto.TurnoDTO;
import com.turnero.entity.Especialidad;
import com.turnero.entity.EstadoTurno;
import com.turnero.entity.Turno;
import com.turnero.exception.ConflictException;
import com.turnero.repository.PacienteRepository;
import com.turnero.repository.TurnoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/turnos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TurnoResource {

    @Inject
    TurnoRepository turnoRepository;

    @Inject
    PacienteRepository pacienteRepository;

    @GET
    public Uni<List<Turno>> listar(
            @QueryParam("especialidad") Especialidad especialidad,
            @QueryParam("estado") EstadoTurno estado,
            @QueryParam("pacienteId") Long pacienteId) {

        if (especialidad != null && estado != null) {
            return turnoRepository.buscarPorEspecialidadYEstado(especialidad, estado);
        }
        if (pacienteId != null) {
            return turnoRepository.buscarPorPacienteId(pacienteId);
        }
        return turnoRepository.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Uni<Turno> buscarPorId(@PathParam("id") Long id) {
        return turnoRepository.buscarPorId(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Turno no encontrado: " + id));
    }

    @POST
    @Transactional
    public Uni<Response> crear(@Valid TurnoDTO dto) {
        if (dto.pacienteId == null || dto.especialidad == null || dto.fecha == null || dto.hora == null) {
            return Uni.createFrom().failure(new jakarta.validation.ValidationException("Faltan campos obligatorios"));
        }

        return pacienteRepository.buscarPorId(dto.pacienteId)
                .onItem().ifNull().failWith(() -> new NotFoundException("Paciente no encontrado: " + dto.pacienteId))
                .onItem().transformToUni(paciente ->
                        turnoRepository.existeConflicto(dto.pacienteId, dto.fecha, dto.hora)
                                .onItem().transformToUni(existeConflicto -> {
                                    if (existeConflicto) {
                                        return Uni.createFrom().failure(
                                                new ConflictException("El paciente ya tiene un turno en esa fecha y hora"));
                                    }
                                    Turno turno = new Turno();
                                    turno.pacienteId = dto.pacienteId;
                                    turno.especialidad = dto.especialidad;
                                    turno.fecha = dto.fecha;
                                    turno.hora = dto.hora;
                                    turno.estado = EstadoTurno.PENDIENTE;

                                    return turnoRepository.persist(turno)
                                            .onItem().transform(t -> Response.status(Response.Status.CREATED).entity(t).build());
                                }));
    }

    @PUT
    @Path("/{id}/estado")
    @Transactional
    public Uni<Turno> cambiarEstado(@PathParam("id") Long id, @Valid CambioEstadoDTO dto) {
        if (dto.estado == null) {
            return Uni.createFrom().failure(new jakarta.validation.ValidationException("El estado es obligatorio"));
        }

        return turnoRepository.buscarPorId(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Turno no encontrado: " + id))
                .onItem().transform(turno -> {
                    turno.estado = dto.estado;
                    return turno;
                });
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Response> eliminar(@PathParam("id") Long id) {
        return turnoRepository.buscarPorId(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Turno no encontrado: " + id))
                .onItem().transform(turno -> {
                    turno.estado = EstadoTurno.CANCELADO;
                    return Response.noContent().build();
                });
    }
}