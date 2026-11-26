package ar.edu.undec.tp_integrador_biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SociosRequest {
    @NotBlank(message = "no debe estar vacio")
    private String nombre;
    @Email
    @NotBlank(message = "no debe estar vacio")
    private String email;
}
