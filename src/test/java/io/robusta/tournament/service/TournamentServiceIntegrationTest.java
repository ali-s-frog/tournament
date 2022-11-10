package io.robusta.tournament.service;

import io.robusta.tournament.controller.payload.UpsertTournamentPayload;
import io.robusta.tournament.entity.Tournament;
import io.robusta.tournament.repository.TournamentRepository;
import io.robusta.tournament.service.exception.DuplicateTournamentNameException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TournamentServiceIntegrationTest {

    @Autowired
    private TournamentService service;

    @Autowired
    private TournamentRepository repository;

    @Test
    @Transactional
    public void testCreate() {
        UpsertTournamentPayload payload = new UpsertTournamentPayload("name");
        Tournament tournament = service.create(payload);
        assertEquals("name", tournament.getName());
        assertThrows(DuplicateTournamentNameException.class, ()-> {
            service.create(payload);
        });
        assertEquals(1, repository.findAll().size());
    }

    @Test
    @Transactional
    public void testCreate2() {
        UpsertTournamentPayload payload = new UpsertTournamentPayload("name");
        Tournament tournament = service.create(payload);
        assertEquals("name", tournament.getName());
        assertThrows(DuplicateTournamentNameException.class, ()-> {
            service.create(payload);
        });
        assertEquals(1, repository.findAll().size());
    }
}
