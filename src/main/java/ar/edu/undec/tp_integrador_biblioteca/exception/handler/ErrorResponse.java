package ar.edu.undec.tp_integrador_biblioteca.exception.handler;

import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {
    private int status;
    private String title;
    private String detail;
    private Map<String, String> errores;

    public ErrorResponse(int status, String title, String detail) {
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public ErrorResponse(int status, String title, String detail, Map<String, String> errores) {
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.errores = errores;
    }
}