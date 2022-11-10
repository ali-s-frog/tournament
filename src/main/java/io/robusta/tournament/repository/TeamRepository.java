package io.robusta.tournament.repository;

import io.robusta.tournament.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
