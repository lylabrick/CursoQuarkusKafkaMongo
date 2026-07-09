package com.turnero.repository;

import com.turnero.entity.Paciente;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PacienteRepository implements PanacheRepository<Paciente> {

    public Uni<List<Paciente>> listarTodos() {
        return listAll();
    }

    public Uni<Paciente> buscarPorId(Long id) {
        return findById(id);
    }

    public Uni<Paciente> buscarPorDni(String dni) {
        return find("dni", dni).firstResult();
    }

    public Uni<Paciente> buscarPorEmail(String email) {
        return find("email", email).firstResult();
    }
}