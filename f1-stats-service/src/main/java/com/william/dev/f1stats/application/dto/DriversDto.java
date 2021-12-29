package com.william.dev.f1stats.application.dto;

import com.william.dev.f1stats.data.api.Driver;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class DriversDto {
    private Set<DriverDto> drivers;

    public DriversDto(final Set<Driver> drivers) {
        this.drivers = drivers.stream()
                .map(DriverDto::new)
                .collect(Collectors.toSet());
    }
}
