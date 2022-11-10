package io.robusta.tournament.service;

import io.robusta.tournament.common.exception.ResourceNotFoundException;
import io.robusta.tournament.controller.payload.AddTeamPayload;
import io.robusta.tournament.controller.payload.TeamPayload;
import io.robusta.tournament.controller.payload.UpsertTournamentPayload;
import io.robusta.tournament.entity.Team;
import io.robusta.tournament.entity.Tournament;
import io.robusta.tournament.repository.TeamRepository;
import io.robusta.tournament.repository.TournamentRepository;
import io.robusta.tournament.service.exception.DuplicateTournamentNameException;
import io.robusta.tournament.service.exception.InvalidTeamException;
import io.robusta.tournament.service.exception.TeamAlreadyExistingInTournamentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository repository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamRestService teamRestService;

    @InjectMocks
    private TournamentService service;

    @Test
    public void testCreate() {
        given(repository.existsByNameIgnoreCase("test")).willReturn(false);
        service.create(new UpsertTournamentPayload("test"));
        ArgumentCaptor<Tournament> savedTournament = ArgumentCaptor.forClass(Tournament.class);
        verify(repository).save(savedTournament.capture());
        assertEquals("test", savedTournament.getValue().getName());
    }

    @Test
    public void testCreateNameAlreadyExisting() {
        given(repository.existsByNameIgnoreCase("test")).willReturn(true);
        assertThrows(DuplicateTournamentNameException.class, () -> {
            service.create(new UpsertTournamentPayload("test"));
        });
    }

    @Test
    public void testDelete() {
        given(repository.findById(1L)).willReturn(Optional.of(new Tournament("test")));
        service.delete(1L);
        ArgumentCaptor<Tournament> deletedTournament = ArgumentCaptor.forClass(Tournament.class);
        verify(repository).delete(deletedTournament.capture());
        assertEquals("test", deletedTournament.getValue().getName());
    }

    @Test
    public void testDeleteTournamentNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });
    }

    @Test
    public void testUpdate() {
        given(repository.findById(1L)).willReturn(Optional.of(new Tournament("test")));
        given(repository.findByNameIgnoreCase("test_updated")).willReturn(Optional.empty());
        service.update(1L, new UpsertTournamentPayload("test_updated"));
        ArgumentCaptor<Tournament> updatedTournament = ArgumentCaptor.forClass(Tournament.class);
        verify(repository).save(updatedTournament.capture());
        assertEquals("test_updated", updatedTournament.getValue().getName());
    }

    @Test
    public void testUpdateTournamentNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(1L, new UpsertTournamentPayload("test_updated"));
        });
    }

    @Test
    public void testUpdateTournamentSameNameAlreadyExisting() {
        given(repository.findById(1L)).willReturn(Optional.of(new Tournament("test")));
        Tournament existingTournament = new Tournament("test2");
        existingTournament.setId(2L);
        given(repository.findByNameIgnoreCase("test_updated")).willReturn(Optional.of(existingTournament));
        assertThrows(DuplicateTournamentNameException.class, () -> {
            service.update(1L, new UpsertTournamentPayload("test_updated"));
        });
    }

    @Test
    public void testAddTeamToTournamentTeamAlreadyExisting() {
        Team existingTeam = new Team();
        existingTeam.setId(1L);
        given(repository.findById(10L)).willReturn(Optional.of(new Tournament("test", Collections.singletonList(existingTeam))));
        assertThrows(TeamAlreadyExistingInTournamentException.class, () -> service.addTeam(10L, new AddTeamPayload(1L)));
    }

    @Test
    public void testAddTeamToTournamenTournamentNotExisting() {
        Team existingTeam = new Team();
        existingTeam.setId(1L);
        given(repository.findById(10L)).willReturn(Optional.ofNullable(null));
        assertThrows(ResourceNotFoundException.class, () -> service.addTeam(10L, new AddTeamPayload(2L)));
    }

    @Test
    public void testAddTeamToTournamentTeamNotExisting() {
        Team existingTeam = new Team();
        existingTeam.setId(1L);
        given(repository.findById(10L)).willReturn(Optional.of(new Tournament("test", Collections.singletonList(existingTeam))));
        given(teamRepository.findById(2L)).willReturn(Optional.ofNullable(null));
        given(teamRestService.retrieveTeamById(2L)).willReturn(null);
        assertThrows(InvalidTeamException.class, () -> service.addTeam(10L, new AddTeamPayload(2L)));
    }

    @Test
    public void testAddTeamToTournamentRetrieveTeamFromTeamService() {
        Team existingTeam = new Team();
        existingTeam.setId(1L);
        existingTeam.setName("existing");

        given(repository.findById(10L)).willReturn(Optional.of(new Tournament("test", new ArrayList<>(Collections.singletonList(existingTeam)))));
        given(teamRepository.findById(2L)).willReturn(Optional.ofNullable(null));
        given(teamRestService.retrieveTeamById(2L)).willReturn(new TeamPayload(2L, "test"));
        Team savedTeam = mock(Team.class);
        given(savedTeam.getName()).willReturn("test22");
        given(teamRepository.save(any(Team.class))).willReturn(savedTeam);
        Tournament tournament = service.addTeam(10L, new AddTeamPayload(2L));

        assertEquals(2, tournament.getTeams().size());
        assertTrue(tournament.getTeams().stream().anyMatch(team -> team.getName().equals("test22")));
    }


    @Test
    public void testAddTeamToTournamentSuccessfullyWhenTeamAlreadyInTheDb() {
        given(repository.findById(10L)).willReturn(Optional.of(new Tournament("test")));
        Team newly_added_team = new Team("Newly Added Team");
        given(teamRepository.findById(2L)).willReturn(Optional.of(newly_added_team));
        Tournament tournament = service.addTeam(10L, new AddTeamPayload(2L));
        assertEquals(1, tournament.getTeams().size());
        assertTrue(tournament.getTeams().stream().anyMatch(team -> team.getName().equals("Newly Added Team")));
    }

    @Test
    public void testAddTeamToTournamentSuccessfullyWhenTeamListIsNotEmpty() {
        Team existingTeam = new Team("a");
        existingTeam.setId(1L);
        ArrayList<Team> teamList = new ArrayList<>();
        teamList.add(existingTeam);
        given(repository.findById(10L)).willReturn(Optional.of(new Tournament("test", teamList)));
        Team teamInTheDb = new Team("Newly Added Team");
        teamInTheDb.setId(2L);
        given(teamRepository.findById(2L)).willReturn(Optional.of(teamInTheDb));
        Tournament tournament = service.addTeam(10L, new AddTeamPayload(2L));
        assertEquals(2, tournament.getTeams().size());
        assertTrue(tournament.getTeams().stream().anyMatch(team -> team.getId().equals(2L)));
    }
}
