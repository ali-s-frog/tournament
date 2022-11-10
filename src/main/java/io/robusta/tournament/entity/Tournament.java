package io.robusta.tournament.entity;

import io.robusta.tournament.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tournament extends BaseEntity {
    private String name;
    @ManyToMany
    @JoinTable(name = "Tournament_Team",
            joinColumns = { @JoinColumn(name = "tournament_id") },
            inverseJoinColumns = { @JoinColumn(name = "team_id") }
    )
    private List<Team> teams;

    public Tournament(String name) {
        this.name = name;
        teams = new ArrayList<>();
    }
}
