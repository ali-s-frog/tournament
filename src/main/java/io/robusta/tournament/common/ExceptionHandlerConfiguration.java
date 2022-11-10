package io.robusta.tournament.common;

import io.robusta.tournament.common.exception.DomainException;
import io.robusta.tournament.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerConfiguration {

    private final MessageSource messageSource;

    public ExceptionHandlerConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public List<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getBindingResult().getAllErrors().stream().map(it ->
                new ApiError("invalid.argument",
                        ((FieldError) it).getField() + " -- " + it.getDefaultMessage())).collect(Collectors.toList());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ApiError handleDomainException(DomainException exception) {
        log.error(exception.getMessage(), exception);
        String message = messageSource.getMessage(exception.getCode(), exception.getParams(), Locale.getDefault());
        return new ApiError(exception.getCode(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleDomainException(ResourceNotFoundException exception) {
        log.warn("Resource " + exception.getClazz().getName() + " with id " + exception.getIdentifier() + " not found!", exception);
        return new ApiError("resource.not.found",
                "Resource: " + exception.getClazz().getSimpleName() + " - identifier: " + exception.getIdentifier());
    }
}
