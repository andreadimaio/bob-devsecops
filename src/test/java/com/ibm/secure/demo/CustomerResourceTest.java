package com.ibm.secure.demo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class CustomerResourceTest {

    // ─────────────────────────────────────────────
    // GET /users/search
    // ─────────────────────────────────────────────

    @Test
    void searchWithEmptyQueryReturnsEmptyList() {
        given()
            .queryParam("query", "")
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .body("size()", equalTo(0));
    }

    // ─────────────────────────────────────────────
    // POST /users/register
    // ─────────────────────────────────────────────

    @Test
    void registerWithMissingUsernamReturnsBadRequest() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"username\": \"\", \"password\": \"secret\"}")
        .when()
            .post("/users/register")
        .then()
            .statusCode(400)
            .body(containsString("Username obbligatorio"));
    }
}
