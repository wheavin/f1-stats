package com.william.dev.f1stats.data.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Driver implements DomainItem {
    private final String firstName;
    private final String lastName;
    private final String nationality;
    @EqualsAndHashCode.Exclude
    private final long dateOfBirth;
    private final String wiki;

    public Driver(final String firstName, final String lastName, final String nationality, final Date dateOfBirth, final String wiki) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth.getTime();
        this.wiki = wiki;
    }

    public Date getDateOfBirth() {
        return new Date(dateOfBirth);
    }
}
