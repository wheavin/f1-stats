package com.william.dev.f1stats.data.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Team implements DomainItem {
    private final String name;
    private final String country;
    private final String description;
    private final String yearFounded;
    private final int numberOfConstructorsChampionships;
}
