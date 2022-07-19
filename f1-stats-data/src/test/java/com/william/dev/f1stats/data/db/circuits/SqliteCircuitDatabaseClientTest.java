package com.william.dev.f1stats.data.db.circuits;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.db.DatabaseClientTestBase;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.allCircuitNamesResultSet;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.allCircuitsResultSet;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.defaultCircuitResultSet;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.emptyCircuitResultSet;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.noCircuitsResultSet;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.partialCircuitResultSet;
import static com.william.dev.f1stats.data.db.circuits.CircuitTestData.silverstoneCircuit;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SqliteCircuitDatabaseClientTest extends DatabaseClientTestBase {

    @InjectMocks
    private SqliteCircuitDatabaseClient objUnderTest;

    @Test
    public void gets_all_circuits() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(allCircuitsResultSet());
        final Set<Circuit> circuits = objUnderTest.getAllCircuits();
        assertThat(circuits).hasSize(2);
    }

    @Test
    public void gets_empty_list_of_circuits() throws Exception {
        lenient().when(mockStatement.executeQuery()).thenReturn(noCircuitsResultSet());
        final Set<Circuit> circuits = objUnderTest.getAllCircuits();
        assertThat(circuits).isEmpty();
    }

    @Test
    public void get_all_circuits_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () -> objUnderTest.getAllCircuits());
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching all circuits from database");
    }

    @ParameterizedTest
    @MethodSource
    public void gets_all_circuits_invalid_data_returns_empty_list(final ResultSet queryResult) throws Exception {
        when(mockStatement.executeQuery()).thenReturn(queryResult);
        final Set<Circuit> circuits = objUnderTest.getAllCircuits();
        assertThat(circuits).isEmpty();
    }

    private static Stream<ResultSet> gets_all_circuits_invalid_data_returns_empty_list() {
        return Stream.of(partialCircuitResultSet(), defaultCircuitResultSet(), emptyCircuitResultSet());
    }

    @Test
    public void gets_all_circuit_names() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(allCircuitNamesResultSet());
        final Set<String> circuitNames = objUnderTest.getAllCircuitNames();
        assertThat(circuitNames).containsExactly("Silverstone", "Monza");
    }

    @Test
    public void gets_empty_list_of_circuit_names() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noCircuitsResultSet());
        final Set<String> circuitNames = objUnderTest.getAllCircuitNames();
        assertThat(circuitNames).isEmpty();
    }

    @Test
    public void gets_all_circuits_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () -> objUnderTest.getAllCircuitNames());
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching all circuit names from database");
    }

    @Test
    public void gets_specified_circuit() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(silverstoneCircuit());
        final Optional<Circuit> circuit = objUnderTest.getCircuit("Silverstone");
        assertThat(circuit.isPresent()).isTrue();
        assertThat(circuit.get().getName()).isEqualTo("Silverstone");
    }

    @Test
    public void does_not_find_specified_circuit() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noCircuitsResultSet());
        final Optional<Circuit> circuit = objUnderTest.getCircuit("Silverstone");
        assertThat(circuit.isPresent()).isFalse();
    }

    @Test
    public void get_specified_circuit_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () -> objUnderTest.getCircuit("Silverstone"));
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching circuit 'Silverstone' from database");
    }

    @Test
    public void adds_circuits_successfully() throws Exception {
        final Set<Circuit> circuitsToAdd = createCircuits();
        objUnderTest.addCircuits(circuitsToAdd);
        assertExecuteBatch(2);
    }

    @Test
    public void add_empty_circuits_does_no_inserts() throws Exception {
        objUnderTest.addCircuits(new HashSet<>());
        verify(mockStatement, never()).execute();
    }

    @Test
    public void add_circuits_throws_exception() throws Exception {
        mockSqlExceptionOnAdd();
        final Set<Circuit> circuitsToAdd = createCircuits();
        final Exception exceptionThrown = assertThrows(DataInsertionException.class, () -> objUnderTest.addCircuits(circuitsToAdd));
        assertThat(exceptionThrown.getMessage()).contains("Some error occurred");
    }

    private Set<Circuit> createCircuits() {
        final Set<Circuit> circuits = new HashSet<>();
        circuits.add(new Circuit("Silverstone", "United Kingdom", "wikilink"));
        circuits.add(new Circuit("Monza", "Italy", "wikilink"));
        return circuits;
    }
}
