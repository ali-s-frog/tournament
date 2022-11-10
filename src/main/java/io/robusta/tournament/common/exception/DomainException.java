package io.robusta.tournament.common.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DomainException extends RuntimeException {
    @NonNull
    private final String code;
    private Object[] params;

    public DomainException(String code, Object... params) {
        this.code = code;
        this.params = params;
    }
}
