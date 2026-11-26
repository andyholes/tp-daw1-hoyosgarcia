package ar.edu.undec.tp_integrador_biblioteca.dto;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Libros;
import ar.edu.undec.tp_integrador_biblioteca.entity.Multa;
import ar.edu.undec.tp_integrador_biblioteca.entity.Socios;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Embeddable
public class PrestamosResponse {
    private Long id;
    private Socios socio;
    private Libros libro;
    private Estado estado;
    private String fechaDevolucion;
    private String fechaRealDevolucion;
    private Multa multa;
    private boolean renovado;

}
