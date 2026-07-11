package com.repopublicaciones;

import com.repopublicaciones.model.Publicacion;
import com.repopublicaciones.model.TipoPublicacion;
import com.repopublicaciones.model.Visibilidad;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
public class PublicacionResourceTest {

    private String crearPublicacionDePrueba(String titulo, TipoPublicacion tipo, int anio, List<String> palabras, List<String> autores, int citaciones) {
        Publicacion p = new Publicacion();
        p.titulo = titulo;
        p.resumen = "resumen de prueba";
        p.autores = autores;
        p.palabrasClave = palabras;
        p.tipo = tipo;
        p.anio = anio;
        p.url = "http://ejemplo.com";
        p.citaciones = citaciones;
        p.visibilidad = Visibilidad.PUBLICA;

        return given()
                .contentType(ContentType.JSON)
                .body(p)
                .when().post("/publicaciones")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void testCrearPublicacion() {
        crearPublicacionDePrueba("Test Paper", TipoPublicacion.PAPER, 2024,
                List.of("ia", "ml"), List.of("Juan Perez"), 5);
    }

    @Test
    public void testListarPaginado() {
        given()
                .queryParam("page", 0)
                .queryParam("pageSize", 5)
                .when().get("/publicaciones")
                .then()
                .statusCode(200);
    }

    @Test
    public void testBuscarPorTipoYAnio() {
        crearPublicacionDePrueba("Tesis 2023", TipoPublicacion.TESIS, 2023,
                List.of("redes"), List.of("Ana Lopez"), 2);

        given()
                .queryParam("tipo", "TESIS")
                .queryParam("anio", 2023)
                .when().get("/publicaciones/tipo-anio")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testBuscarPorPalabraClave() {
        crearPublicacionDePrueba("Paper sobre blockchain", TipoPublicacion.PAPER, 2022,
                List.of("blockchain", "cripto"), List.of("Carlos Ruiz"), 3);

        given()
                .queryParam("palabra", "blockchain")
                .when().get("/publicaciones/palabra-clave")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testBuscarPorAutor() {
        crearPublicacionDePrueba("Informe X", TipoPublicacion.INFORME_TECNICO, 2021,
                List.of("varios"), List.of("Maria Gomez"), 1);

        given()
                .queryParam("nombre", "Maria Gomez")
                .when().get("/publicaciones/autor")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testEliminarPublicacion() {
        String id = crearPublicacionDePrueba("A eliminar", TipoPublicacion.TRABAJO_FINAL, 2020,
                List.of("temp"), List.of("Nadie"), 0);

        given()
                .when().delete("/publicaciones/" + id)
                .then()
                .statusCode(204);

        given()
                .when().get("/publicaciones/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    public void testEstadisticas() {
        given()
                .when().get("/publicaciones/estadisticas")
                .then()
                .statusCode(200)
                .body("size()", equalTo(4));
    }
}