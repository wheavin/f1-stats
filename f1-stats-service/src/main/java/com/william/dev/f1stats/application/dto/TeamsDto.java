package com.william.dev.f1stats.application.dto;

import com.william.dev.f1stats.data.api.Team;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TeamsDto {
    private Set<TeamDto> teams;

    public TeamsDto(final Set<Team> teams) {
        this.teams = teams.stream()
                .map(TeamDto::new)
                .collect(Collectors.toSet());
    }
}
