package io.robusta.tournament.controller;

import io.robusta.tournament.common.exception.ResourceNotFoundException;
import io.robusta.tournament.controller.payload.AddTeamPayload;
import io.robusta.tournament.controller.payload.TournamentPayload;
import io.robusta.tournament.controller.payload.UpsertTournamentPayload;
import io.robusta.tournament.entity.Tournament;
import io.robusta.tournament.repository.TournamentRepository;
import io.robusta.tournament.service.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TournamentController {
    private final TournamentService service;
    private final TournamentRepository repository;

    public TournamentController(TournamentService service, TournamentRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/tournaments/{tournamentId}")
    public TournamentPayload get(@PathVariable Long tournamentId) {
        Tournament tournament = repository.findById(tournamentId).orElseThrow(() ->
                new ResourceNotFoundException(Tournament.class, tournamentId));
        return new TournamentPayload(tournament);
    }

    @GetMapping("/tournaments")
    public List<TournamentPayload> list() {
        return repository.findAll().stream().map(TournamentPayload::new).collect(Collectors.toList());
    }

    @PostMapping("/tournaments")
    public ResponseEntity<Object> create(@Valid @RequestBody UpsertTournamentPayload payload) {
        Tournament tournament = service.create(payload);
        //Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tournament.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/tournaments/{tournamentId}")
    public ResponseEntity<Object> delete(@PathVariable Long tournamentId) {
        service.delete(tournamentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tournaments/{tournamentId}")
    public TournamentPayload update(@PathVariable Long tournamentId, @Valid @RequestBody UpsertTournamentPayload upsertTournamentPayload) {
        Tournament tournament = service.update(tournamentId, upsertTournamentPayload);
        return new TournamentPayload(tournament);
    }

    @PostMapping("/tournaments/{tournamentId}/add-team")
    public TournamentPayload addTeam(@PathVariable Long tournamentId, @Valid @RequestBody AddTeamPayload payload) {
        Tournament tournament = service.addTeam(tournamentId, payload);
        return new TournamentPayload(tournament);
    }
}
