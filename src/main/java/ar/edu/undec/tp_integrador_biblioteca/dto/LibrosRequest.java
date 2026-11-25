package ar.edu.undec.tp_integrador_biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LibrosRequest {
    @NotBlank(message = "No debe estar vacio")
    private String titulo;
    @NotBlank(message = "No debe estar vacio")
    private String autor;
    @NotBlank(message = "No debe estar vacio")
    private String isbn;
}
