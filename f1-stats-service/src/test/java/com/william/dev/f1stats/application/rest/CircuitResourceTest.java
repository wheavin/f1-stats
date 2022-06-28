package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.TestData;
import com.william.dev.f1stats.application.dto.CircuitDto;
import com.william.dev.f1stats.application.dto.CircuitsDto;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.william.dev.f1stats.application.rest.assertions.ResponseAssertions.assertThatResponse;
import static org.mockito.Mockito.when;

public class CircuitResourceTest extends ResourceTest {

    @Inject
    private CircuitResource objUnderTest;

    @Test
    public void list_all_circuits_returns_list_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allCircuitsDataSet());

        final Response response = objUnderTest.listAllCircuits();
        assertThatResponse(response).isOk();

        final CircuitsDto circuitsReturned = (CircuitsDto) response.getEntity();
        assertThat(circuitsReturned.getCircuits()).hasSize(2);
        final CircuitDto circuitReturned = circuitsReturned.getCircuits().iterator().next();
        assertThat(circuitReturned.getName()).isEqualTo("Silverstone");
        assertThat(circuitReturned.getCountry()).isEqualTo("United Kingdom");
        assertThat(circuitReturned.getWiki()).isEqualTo("http://en.wikipedia.org/wiki/Silverstone_Circuit");
    }

    @Test
    public void list_all_circuits_returns_empty_list() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());

        final Response response = objUnderTest.listAllCircuits();
        assertThatResponse(response).isOk();

        final CircuitsDto circuitsDtoReturned = (CircuitsDto) response.getEntity();
        assertThat(circuitsDtoReturned.getCircuits()).isEmpty();
    }

    @Test
    public void list_all_circuits_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Error when executing query"));
        final Response response = objUnderTest.listAllCircuits();
        assertThatResponse(response).isInternalServerError();
    }

    @Test
    public void list_all_circuit_names_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allCircuitsDataSet());
        final Response response = objUnderTest.listAllCircuitNames();
        assertThatResponse(response).isOk();
        final Set<String> circuitNames = (Set<String>) response.getEntity();
        assertThat(circuitNames).containsExactly("Monza", "Silverstone");
    }

    @Test
    public void list_all_circuit_names_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.listAllCircuitNames();
        assertThatResponse(response).isInternalServerError();
    }

    @Test
    public void get_specified_circuit_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.silverstoneDataSet());

        final Response response = objUnderTest.getCircuit("Silverstone");
        assertThatResponse(response).isOk();

        final CircuitDto circuitReturned = (CircuitDto) response.getEntity();
        assertThat(circuitReturned.getName()).isEqualTo("Silverstone");
        assertThat(circuitReturned.getCountry()).isEqualTo("United Kingdom");
        assertThat(circuitReturned.getWiki()).isEqualTo("http://en.wikipedia.org/wiki/Silverstone_Circuit");
    }

    @Test
    public void specified_circuit_not_found() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());
        final Response response = objUnderTest.getCircuit("Silverstone");
        assertThatResponse(response).isBadRequest();
        assertThatResponse(response).hasMessage("Circuit 'Silverstone' not found");
    }

    @Test
    public void get_specified_circuit_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.getCircuit("Silverstone");
        assertThatResponse(response).isInternalServerError();
    }
}
