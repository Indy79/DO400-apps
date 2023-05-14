package com.redhat.training;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
@TestHTTPEndpoint(SolverResource.class)
public class SolverResourceTest {

    @Test
    public void multiplyPositivInteger() {
        RestAssured.given()
                .when()
                .get("2*3")
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("6.0"));
    }


    @Test
    public void addPositivInteger() {
        RestAssured.given()
                .when()
                .get("2+3")
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("5.0"));
    }

    @Test
    public void addBadInteger() {
        RestAssured.given()
                .when()
                .get("b+3")
                .then()
                .statusCode(400);
    }
}
