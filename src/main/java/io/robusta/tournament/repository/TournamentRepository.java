package io.robusta.tournament.repository;

import io.robusta.tournament.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    Boolean existsByNameIgnoreCase(String name);
    Optional<Tournament> findByNameIgnoreCase(String name);
}
