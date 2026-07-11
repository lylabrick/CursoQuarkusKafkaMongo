package com.repopublicaciones.resource;

import com.repopublicaciones.model.Autor;
import com.repopublicaciones.repository.AutorRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

@Path("/autores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutorResource {

    @Inject
    AutorRepository repository;

    @GET
    public Multi<Autor> listar() {
        return repository.streamAll();
    }

    @GET
    @Path("/{id}")
    public Uni<Response> buscarPorId(@PathParam("id") String id) {
        return repository.findById(new ObjectId(id))
                .onItem().ifNotNull().transform(autor -> Response.ok(autor).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Uni<Response> crear(Autor autor) {
        return repository.persist(autor)
                .onItem().transform(a -> Response.status(Response.Status.CREATED).entity(a).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> actualizar(@PathParam("id") String id, Autor datos) {
        return repository.findById(new ObjectId(id))
                .onItem().ifNotNull().transformToUni(autor -> {
                    autor.nombre = datos.nombre;
                    autor.apellido = datos.apellido;
                    autor.orcid = datos.orcid;
                    autor.institucion = datos.institucion;
                    autor.areasDeInteres = datos.areasDeInteres;
                    return repository.update(autor).onItem().transform(v -> Response.ok(autor).build());
                })
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> eliminar(@PathParam("id") String id) {
        return repository.deleteById(new ObjectId(id))
                .onItem().transform(borrado -> borrado
                        ? Response.noContent().build()
                        : Response.status(Response.Status.NOT_FOUND).build());
    }
}