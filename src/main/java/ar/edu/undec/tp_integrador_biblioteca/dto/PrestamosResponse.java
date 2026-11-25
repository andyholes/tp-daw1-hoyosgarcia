package ar.edu.undec.tp_integrador_biblioteca.dto;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PrestamosResponse {
    private Long id;
    private String socio;
    private String libro;
    private String fechaDevolucion;
    private Estado estado;
}
