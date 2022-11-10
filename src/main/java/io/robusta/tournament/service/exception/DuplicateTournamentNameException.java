package io.robusta.tournament.service.exception;

import io.robusta.tournament.common.exception.DomainException;

public class DuplicateTournamentNameException extends DomainException {
    public DuplicateTournamentNameException(String name) {
        super("tournament.name.existing", name);
    }
}
