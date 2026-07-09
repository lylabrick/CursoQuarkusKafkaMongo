package com.turnero;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

@QuarkusTest
public class PacienteResourceTest {

    @Test
    public void testCrearPacienteExitoso() {
        String body = """
            {
              "nombre": "Juan",
              "apellido": "Perez",
              "dni": "30111222",
              "email": "juan.perez@test.com",
              "fechaNacimiento": "1990-05-10"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/pacientes")
                .then()
                .statusCode(201)
                .body("nombre", equalTo("Juan"))
                .body("dni", equalTo("30111222"));
    }

    @Test
    public void testCrearPacienteDniDuplicadoDevuelve409() {
        String body = """
            {
              "nombre": "Ana",
              "apellido": "Gomez",
              "dni": "30222333",
              "email": "ana.gomez@test.com",
              "fechaNacimiento": "1992-03-15"
            }
            """;

        given().contentType(ContentType.JSON).body(body).when().post("/pacientes").then().statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/pacientes")
                .then()
                .statusCode(409);
    }

    @Test
    public void testBuscarPacientePorIdInexistenteDevuelve404() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/pacientes/99999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCrearPacienteSinCamposObligatoriosDevuelve400() {
        String body = """
            {
              "nombre": "",
              "apellido": "Sinapellido",
              "dni": "",
              "email": ""
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/pacientes")
                .then()
                .statusCode(400);
    }

    @Test
    public void testListarPacientes() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/pacientes")
                .then()
                .statusCode(200)
                .body("$", isA(java.util.List.class));
    }
}