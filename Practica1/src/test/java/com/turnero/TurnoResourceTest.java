package com.turnero;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class TurnoResourceTest {

    private Long crearPacienteDePrueba(String dni) {
        String body = String.format("""
            {
              "nombre": "Test",
              "apellido": "Paciente",
              "dni": "%s",
              "email": "test.%s@test.com",
              "fechaNacimiento": "1995-01-01"
            }
            """, dni, dni);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/pacientes")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    @Test
    public void testCrearTurnoExitoso() {
        Long pacienteId = crearPacienteDePrueba("40111222");

        String body = String.format("""
            {
              "pacienteId": %d,
              "especialidad": "CARDIOLOGIA",
              "fecha": "2026-08-10",
              "hora": "10:30:00"
            }
            """, pacienteId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/turnos")
                .then()
                .statusCode(201)
                .body("estado", equalTo("PENDIENTE"));
    }

    @Test
    public void testCrearTurnoPacienteInexistenteDevuelve404() {
        String body = """
            {
              "pacienteId": 999999,
              "especialidad": "PEDIATRIA",
              "fecha": "2026-08-11",
              "hora": "11:00:00"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/turnos")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCrearTurnoConflictoHorarioDevuelve409() {
        Long pacienteId = crearPacienteDePrueba("40222333");

        String body = String.format("""
            {
              "pacienteId": %d,
              "especialidad": "CLINICA",
              "fecha": "2026-08-12",
              "hora": "09:00:00"
            }
            """, pacienteId);

        given().contentType(ContentType.JSON).body(body).when().post("/turnos").then().statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/turnos")
                .then()
                .statusCode(409);
    }

    @Test
    public void testCambiarEstadoTurno() {
        Long pacienteId = crearPacienteDePrueba("40333444");

        String bodyTurno = String.format("""
            {
              "pacienteId": %d,
              "especialidad": "NEUROLOGIA",
              "fecha": "2026-08-13",
              "hora": "14:00:00"
            }
            """, pacienteId);

        Long turnoId = given()
                .contentType(ContentType.JSON)
                .body(bodyTurno)
                .when().post("/turnos")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        String bodyEstado = """
            { "estado": "CONFIRMADO" }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(bodyEstado)
                .when().put("/turnos/" + turnoId + "/estado")
                .then()
                .statusCode(200)
                .body("estado", equalTo("CONFIRMADO"));
    }

    @Test
    public void testEliminarTurnoLogicamente() {
        Long pacienteId = crearPacienteDePrueba("40444555");

        String bodyTurno = String.format("""
            {
              "pacienteId": %d,
              "especialidad": "CLINICA",
              "fecha": "2026-08-14",
              "hora": "16:00:00"
            }
            """, pacienteId);

        Long turnoId = given()
                .contentType(ContentType.JSON)
                .body(bodyTurno)
                .when().post("/turnos")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .contentType(ContentType.JSON)
                .when().delete("/turnos/" + turnoId)
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .when().get("/turnos/" + turnoId)
                .then()
                .statusCode(200)
                .body("estado", equalTo("CANCELADO"));
    }
}