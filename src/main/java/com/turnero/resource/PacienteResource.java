package com.turnero.resource;

import com.turnero.dto.PacienteDTO;
import com.turnero.entity.Paciente;
import com.turnero.exception.ConflictException;
import com.turnero.repository.PacienteRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteRepository pacienteRepository;

    @GET
    public Uni<List<Paciente>> listar() {
        return pacienteRepository.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Uni<Paciente> buscarPorId(@PathParam("id") Long id) {
        return pacienteRepository.buscarPorId(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Paciente no encontrado: " + id));
    }

    @POST
    @WithTransaction
    public Uni<Response> crear(@Valid PacienteDTO dto) {

        return pacienteRepository.buscarPorDni(dto.dni)
                .onItem().transformToUni(existente -> {
                    if (existente != null) {
                        return Uni.createFrom().failure(new ConflictException("Ya existe un paciente con ese DNI"));
                    }
                    Paciente paciente = new Paciente();
                    paciente.nombre = dto.nombre;
                    paciente.apellido = dto.apellido;
                    paciente.dni = dto.dni;
                    paciente.email = dto.email;
                    paciente.fechaNacimiento = dto.fechaNacimiento;

                    return pacienteRepository.persist(paciente)
                            .onItem().transform(p -> Response.status(Response.Status.CREATED).entity(p).build());
                });
    }

    @PUT
    @Path("/{id}")
    @WithTransaction
    public Uni<Paciente> actualizar(@PathParam("id") Long id, @Valid PacienteDTO dto) {
        return pacienteRepository.buscarPorId(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Paciente no encontrado: " + id))
                .onItem().transform(paciente -> {
                    paciente.nombre = dto.nombre;
                    paciente.apellido = dto.apellido;
                    paciente.email = dto.email;
                    paciente.fechaNacimiento = dto.fechaNacimiento;
                    return paciente;
                });
    }
}