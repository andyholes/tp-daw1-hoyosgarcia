package ar.edu.undec.tp_integrador_biblioteca.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private int status = 409;
    private String title = "Conflicto interno";
    private String detail;

    public ConflictException(String detail) {
        this.detail = detail;
    }
}
