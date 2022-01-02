package com.william.dev.f1stats.application.dto;

import com.william.dev.f1stats.data.api.Team;
import lombok.Data;

@Data
public class TeamDto {
    private final String name;
    private final String nationality;
    private final String wiki;

    public TeamDto(final Team team) {
        this.name = team.getName();
        this.nationality = team.getNationality();
        this.wiki = team.getWiki();
    }
}
