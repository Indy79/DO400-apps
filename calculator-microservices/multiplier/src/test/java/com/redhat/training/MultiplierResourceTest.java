package com.redhat.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.redhat.training.service.SolverService;

import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiplierResourceTest {

    SolverService solverService;
    MultiplierResource multiplierResource;

    @BeforeEach
    public void setup() {
        solverService = mock(SolverService.class);
        multiplierResource = new MultiplierResource(solverService);
    }

    @Test
    public void givenTwoPositiveValuesMustMultiplyAndReturnAnAnswer() {
        // Given
        when(solverService.solve("2")).thenReturn(Float.valueOf("2"));
        when(solverService.solve("3")).thenReturn(Float.valueOf("3"));

        // When
        Float result = multiplierResource.multiply("2", "3");

        // Then
        assertEquals(6.0f, result);
    }

    @Test
    public void givenOnePositiveValueAndOneNegativeValueMustMultiplyAndReturnAnAnswer() {
        // Given
        when(solverService.solve("-2")).thenReturn(Float.valueOf("-2"));
        when(solverService.solve("3")).thenReturn(Float.valueOf("3"));

        // When
        Float result = multiplierResource.multiply("-2", "3");

        // Then
        assertEquals(-6.0f, result);
    }

    @Test
    public void givenOnePositiveValueAndOneWrongValueMustMultiplyAndReturnAnError() {
        // Given
        WebApplicationException cause = new WebApplicationException("Unkown error", Response.Status.BAD_REQUEST);
        when(solverService.solve("b")).thenThrow(new ResteasyWebApplicationException(cause));
        when(solverService.solve("3")).thenReturn(Float.valueOf("3"));

        // Then
        assertThrows(ResteasyWebApplicationException.class, () -> {
            // When
            Float result = multiplierResource.multiply("b", "3");
        });
    }


}
