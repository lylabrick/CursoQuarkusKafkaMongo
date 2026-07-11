package com.repopublicaciones.repository;

import com.repopublicaciones.model.Publicacion;
import com.repopublicaciones.model.TipoPublicacion;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PublicacionRepository implements ReactivePanacheMongoRepository<Publicacion> {

    public Multi<Publicacion> buscarPorTipoYAnio(TipoPublicacion tipo, Integer anio) {
        return find("tipo = ?1 and anio = ?2", tipo, anio).stream();
    }

    public Multi<Publicacion> buscarPorPalabraClave(String palabraClave) {
        return find("palabrasClave", palabraClave).stream();
    }

    public Multi<Publicacion> buscarPorAutor(String nombreAutor) {
        return find("autores", nombreAutor).stream();
    }

    public Multi<Publicacion> ordenarPorCitacionesDescendente() {
        return findAll(Sort.descending("citaciones")).stream();
    }

    public Uni<Long> contarPorTipo(TipoPublicacion tipo) {
        return count("tipo", tipo);
    }

    public Uni<List<Publicacion>> listarPaginado(int page, int pageSize) {
        return findAll().page(Page.of(page, pageSize)).list();
    }

    public Multi<TipoPublicacion> tiposDistintos() {
        return Multi.createFrom().items(TipoPublicacion.values());
    }
}