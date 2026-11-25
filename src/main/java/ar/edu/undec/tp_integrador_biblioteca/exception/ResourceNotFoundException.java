package ar.edu.undec.tp_integrador_biblioteca.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private int status = 404;
    private String title = "No encontrada";
    private String detail = "La entidad solicitada no ha podido encontrarse";

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String detail) {
        this.detail = detail;
    }
}
