package ar.edu.undec.tp_integrador_biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LibrosRequest {
    @NotBlank(message = "no debe estar vacio")
    private String titulo;
    @NotBlank(message = "no debe estar vacio")
    private String autor;
    @NotBlank(message = "no debe estar vacio")
    private String isbn;
}
