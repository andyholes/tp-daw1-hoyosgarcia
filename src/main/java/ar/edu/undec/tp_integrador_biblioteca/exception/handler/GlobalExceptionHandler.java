package ar.edu.undec.tp_integrador_biblioteca.exception.handler;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.exception.BadRequestException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ConflictException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.ValueInstantiationException;

import java.util.Arrays;
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
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable cause = ex.getCause();
        Throwable root = ex.getMostSpecificCause();

        Class<?> targetEnumType = null;
        String valorRecibido = null;

        if (cause instanceof InvalidFormatException invalidEx) {
            targetEnumType = invalidEx.getTargetType();
            valorRecibido = String.valueOf(invalidEx.getValue());
        } else if (cause instanceof ValueInstantiationException valueEx && root instanceof IllegalArgumentException) {
            targetEnumType = valueEx.getType() != null ? valueEx.getType().getRawClass() : Estado.class;
        }

        if (targetEnumType != null && targetEnumType.isEnum()) {
            String valoresValidos = Arrays.stream(targetEnumType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String mensaje = (valorRecibido != null)
                    ? String.format("El valor '%s' no es valido para %s. Valores permitidos: %s",
                    valorRecibido, targetEnumType.getSimpleName(), valoresValidos)
                    : String.format("Valor invalido para %s. Valores permitidos: %s",
                    targetEnumType.getSimpleName(), valoresValidos);

            ErrorResponse error = new ErrorResponse(400, "Valor de enum invalido", mensaje);
            return ResponseEntity.status(400).body(error);
        }

        ErrorResponse error = new ErrorResponse(
                400,
                "Cuerpo de peticion invalido",
                "El formato del JSON enviado no es valido o contiene datos ilegibles"
        );
        return ResponseEntity.status(400).body(error);
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
