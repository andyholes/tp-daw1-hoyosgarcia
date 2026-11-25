package ar.edu.undec.tp_integrador_biblioteca.exception.handler;

import ar.edu.undec.tp_integrador_biblioteca.exception.BadRequestException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ConflictException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        BadRequestException exception = new BadRequestException(ex.getMessage());

        return ResponseEntity.status(exception.getStatus()).body(
                new ErrorResponse(
                        exception.getStatus(),
                        exception.getTitle(),
                        exception.getDetail()
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                new ErrorResponse(
                        ex.getStatus(),
                        ex.getTitle(),
                        ex.getDetail()
                )
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                new ErrorResponse(
                        ex.getStatus(),
                        ex.getTitle(),
                        ex.getDetail()
                )
        );
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BadRequestException exception = new BadRequestException(
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                        FieldError::getField,
                                        err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "Dato inválido"
                                )
                        )
        );
        return ResponseEntity.status(exception.getStatus()).body(
                new ErrorResponse(
                        exception.getStatus(),
                        exception.getTitle(),
                        exception.getDetail(),
                        exception.getErrores()
                )
        );
    }
}
