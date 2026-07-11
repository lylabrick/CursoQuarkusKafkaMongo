package com.repopublicaciones;

import com.repopublicaciones.model.Autor;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AutorResourceTest {

    @Test
    public void testCrearYListarAutor() {
        Autor a = new Autor();
        a.nombre = "Laura";
        a.apellido = "Test";
        a.orcid = "0000-0000-0000-0000";
        a.institucion = "UNLP";
        a.areasDeInteres = List.of("IA", "Cloud");

        given()
                .contentType(ContentType.JSON)
                .body(a)
                .when().post("/autores")
                .then()
                .statusCode(201);

        given()
                .when().get("/autores")
                .then()
                .statusCode(200);
    }
}
