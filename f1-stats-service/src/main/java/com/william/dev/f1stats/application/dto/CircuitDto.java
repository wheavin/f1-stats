package com.william.dev.f1stats.application.dto;

import com.william.dev.f1stats.data.api.Circuit;
import lombok.Data;

@Data
public class CircuitDto {
    private String name;
    private String country;
    private String wiki;

    public CircuitDto(final Circuit circuit) {
        this.name = circuit.getName();
        this.country = circuit.getCountry();
        this.wiki = circuit.getWiki();
    }
}
