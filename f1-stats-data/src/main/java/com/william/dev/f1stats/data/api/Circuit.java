package com.william.dev.f1stats.data.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Data
@ToString
public class Circuit implements DomainItem {
    private final String name;
    private final String country;
    private final String wiki;
}
