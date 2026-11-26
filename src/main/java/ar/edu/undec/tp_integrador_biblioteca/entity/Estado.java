package ar.edu.undec.tp_integrador_biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Estado {
    ACTIVO,
    VENCIDO,
    DEVUELTO;

    @JsonCreator
    public static Estado fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Estado.valueOf(value.trim().toUpperCase());
    }
}