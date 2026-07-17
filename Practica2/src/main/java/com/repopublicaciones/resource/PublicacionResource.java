package com.repopublicaciones.resource;

import com.repopublicaciones.dto.EstadisticaTipo;
import com.repopublicaciones.model.Publicacion;
import com.repopublicaciones.model.TipoPublicacion;
import com.repopublicaciones.repository.PublicacionRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/publicaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublicacionResource {

    @Inject
    PublicacionRepository repository;

    @GET
    public Uni<List<Publicacion>> listar(@RestQuery Integer page, @RestQuery Integer pageSize) {
        int p = page != null ? page : 0;
        int size = pageSize != null ? pageSize : 10;
        return repository.listarPaginado(p, size);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> buscarPorId(@PathParam("id") String id) {
        return repository.findById(new ObjectId(id))
                .onItem().ifNotNull().transform(pub -> Response.ok(pub).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/tipo-anio")
    public Multi<Publicacion> porTipoYAnio(@RestQuery TipoPublicacion tipo, @RestQuery Integer anio) {
        return repository.buscarPorTipoYAnio(tipo, anio);
    }

    @GET
    @Path("/palabra-clave")
    public Multi<Publicacion> porPalabraClave(@RestQuery String palabra) {
        return repository.buscarPorPalabraClave(palabra);
    }

    @GET
    @Path("/autor")
    public Multi<Publicacion> porAutor(@RestQuery String nombre) {
        return repository.buscarPorAutor(nombre);
    }

    @GET
    @Path("/ordenadas-por-citaciones")
    public Multi<Publicacion> ordenadasPorCitaciones() {
        return repository.ordenarPorCitacionesDescendente();
    }

    @GET
    @Path("/estadisticas")
    public Uni<List<EstadisticaTipo>> estadisticas() {
        return Uni.join().all(
                Arrays.stream(TipoPublicacion.values())
                        .map(tipo -> repository.contarPorTipo(tipo)
                                .onItem().transform(cantidad -> new EstadisticaTipo(tipo, cantidad)))
                        .collect(Collectors.toList())
        ).usingConcurrencyOf(4)
                .andCollectFailures();
    }

    @POST
    public Uni<Response> crear(Publicacion publicacion) {
        return repository.persist(publicacion)
                .onItem().transform(p -> Response.status(Response.Status.CREATED).entity(p).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> actualizar(@PathParam("id") String id, Publicacion datos) {
        return repository.findById(new ObjectId(id))
                .onItem().ifNotNull().transformToUni(pub -> {
                    pub.titulo = datos.titulo;
                    pub.resumen = datos.resumen;
                    pub.autores = datos.autores;
                    pub.palabrasClave = datos.palabrasClave;
                    pub.tipo = datos.tipo;
                    pub.anio = datos.anio;
                    pub.url = datos.url;
                    pub.citaciones = datos.citaciones;
                    pub.visibilidad = datos.visibilidad;
                    return repository.update(pub).onItem().transform(v -> Response.ok(pub).build());
                })
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
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