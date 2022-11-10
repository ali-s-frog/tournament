package io.robusta.tournament.common.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResourceNotFoundException extends RuntimeException {
    @NonNull
    private Class<?> clazz;
    @NonNull
    private Long identifier;
}
