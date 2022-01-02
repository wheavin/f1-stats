package com.william.dev.f1stats.data.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Team implements DomainItem {
    private final String name;
    private final String nationality;
    private final String wiki;
}
