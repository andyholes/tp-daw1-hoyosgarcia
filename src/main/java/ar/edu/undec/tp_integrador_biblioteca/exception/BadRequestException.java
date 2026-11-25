package ar.edu.undec.tp_integrador_biblioteca.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BadRequestException extends RuntimeException{
    private int status = 400;
    private String title = "Datos invalidos";
    private String detail = "La peticion tiene errores";
    private Map<String, String> errores;

    public BadRequestException(Map<String, String> errores) {
        this.errores = errores;
    }

    public BadRequestException(String detail) {
        this.detail = detail;
    }
}
