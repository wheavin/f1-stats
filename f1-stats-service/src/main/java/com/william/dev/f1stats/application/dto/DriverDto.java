package com.william.dev.f1stats.application.dto;

import com.william.dev.f1stats.data.api.Driver;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class DriverDto {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private final String firstName;
    private final String lastName;
    private final String nationality;
    @EqualsAndHashCode.Exclude
    private final Date dateOfBirth;
    private final String wiki;

    public DriverDto(final Driver driver) {
        this.firstName = driver.getFirstName();
        this.lastName = driver.getLastName();
        this.nationality = driver.getNationality();
        this.dateOfBirth = new Date(driver.getDateOfBirth().getTime());
        this.wiki = driver.getWiki();
    }

    public String getDateOfBirth() {
        return DATE_FORMAT.format(dateOfBirth);
    }
}
