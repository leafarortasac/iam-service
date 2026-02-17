package com.br.iam_service.handler;

import com.br.iam_service.exception.ConflictException;
import com.br.iam_service.exception.UnauthorizedException;
import com.br.shared.contracts.model.ErrorResponseRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseRepresentation> handleUnauthorized(UnauthorizedException ex) {
        var error = new ErrorResponseRepresentation();
        error.setMensagem(ex.getMessage());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseRepresentation> handleConflict(ConflictException ex) {
        var error = new ErrorResponseRepresentation();
        error.setMensagem(ex.getMessage());
        error.setStatus(HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
