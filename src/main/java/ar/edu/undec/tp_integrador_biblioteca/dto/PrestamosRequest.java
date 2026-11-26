package ar.edu.undec.tp_integrador_biblioteca.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PrestamosRequest {
    @NotNull(message = "no debe estar vacio")
    private Long socioId;
    @NotNull(message = "no debe estar vacio")
    private Long libroId;
    @Future
    @NotNull(message = "no debe estar vacio")
    private LocalDate fechaDevolucion;
}