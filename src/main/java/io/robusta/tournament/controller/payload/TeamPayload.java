package io.robusta.tournament.controller.payload;

import io.robusta.tournament.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamPayload {
    private Long id;
    private String name;

    public TeamPayload() {
    }

    public TeamPayload(Team team) {
        this.id = team.getId();
        this.name = team.getName();
    }
}
