package io.robusta.tournament.controller.payload;

import io.robusta.tournament.entity.Tournament;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class TournamentPayload {
    private Long id;
    private String name;
    private List<TeamPayload> teams;

    public TournamentPayload(Tournament tournament) {
        this.id = tournament.getId();
        this.name = tournament.getName();
        this.teams = tournament.getTeams().stream().map(TeamPayload::new).collect(Collectors.toList());
    }
}
