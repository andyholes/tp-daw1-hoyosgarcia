package ar.edu.undec.tp_integrador_biblioteca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibrosResponse {
    private Long id;
    private String titulo;
    private String autor;
    private String isdb;
    private Integer vecesPrestado;
}
