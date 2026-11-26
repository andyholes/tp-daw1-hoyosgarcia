package ar.edu.undec.tp_integrador_biblioteca.dto;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Libros;
import ar.edu.undec.tp_integrador_biblioteca.entity.Socios;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PrestamosResponse {
    private Long id;
    private Socios socio;
    private Libros libro;
    private String fechaDevolucion;
    private Estado estado;
}
