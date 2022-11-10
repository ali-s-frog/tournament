
package io.robusta.tournament.service.exception;

import io.robusta.tournament.common.exception.DomainException;

public class InvalidTeamException extends DomainException {

    public InvalidTeamException(Long teamId) {
        super("team.not.valid", teamId);
    }
}
