package com.redhat.training;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.redhat.training.service.SolverService;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.CoreMatchers;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;

@QuarkusTest
@TestHTTPEndpoint(AdderResource.class)
public class AdderResourceTest {

    @InjectMock
    @RestClient 
    SolverService solverService;

    @Test
    public void givenTwoPositiveValuesMustMultiplyAndReturnAnAnswer() {
        // Given
        when(solverService.solve("2")).thenReturn(Float.valueOf("2"));
        when(solverService.solve("3")).thenReturn(Float.valueOf("3"));

        // When
        RestAssured.given()
        .when()
        .get("2/3")
        .then()
        .statusCode(200)
                .body(CoreMatchers.is("5.0"));
    }

    @Test
    public void givenOnePositiveValueAndOneNegativeValueMustMultiplyAndReturnAnAnswer() {
        // Given
        when(solverService.solve("-2")).thenReturn(Float.valueOf("-2"));
        when(solverService.solve("3")).thenReturn(Float.valueOf("3"));

        RestAssured.given()
        .when()
        .get("-2/3")
        .then()
        .statusCode(200)
                .body(CoreMatchers.is("1.0"));
    }

    @Test
    public void givenOnePositiveValueAndOneWrongValueMustMultiplyAndReturnAnError() {
        // Given
        WebApplicationException cause = new WebApplicationException("Unkown error", Response.Status.BAD_REQUEST);
        when(solverService.solve("b")).thenThrow(new ResteasyWebApplicationException(cause));
        when(solverService.solve("3")).thenReturn(Float.valueOf("3"));

        RestAssured.given()
        .when()
        .get("b/3")
        .then()
        .statusCode(400);
    }

}
