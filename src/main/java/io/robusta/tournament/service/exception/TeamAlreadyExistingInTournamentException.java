package io.robusta.tournament.service.exception;

import io.robusta.tournament.common.exception.DomainException;

public class TeamAlreadyExistingInTournamentException extends DomainException {
    public TeamAlreadyExistingInTournamentException(String teamName, String tournamentName) {
        super("tournament.team.already.existing", teamName, tournamentName);
    }
}
