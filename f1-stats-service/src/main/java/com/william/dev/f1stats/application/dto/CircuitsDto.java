package com.william.dev.f1stats.application.dto;

import com.william.dev.f1stats.data.api.Circuit;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CircuitsDto {
    private Set<CircuitDto> circuits;

    public CircuitsDto(final Set<Circuit> circuits) {
        this.circuits = circuits.stream()
                .map(CircuitDto::new)
                .collect(Collectors.toSet());
    }
}
