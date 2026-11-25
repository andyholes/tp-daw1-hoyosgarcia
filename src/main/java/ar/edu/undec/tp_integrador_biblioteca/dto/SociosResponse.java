package ar.edu.undec.tp_integrador_biblioteca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SociosResponse {
    private Long id;
    private String nombre;
    private String email;
}