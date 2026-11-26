package ar.edu.undec.tp_integrador_biblioteca.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Prestamos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "socio_id", nullable = false)
    private Socios socio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id")
    private Libros libro;
    private Estado estado;
    @NotNull
    private LocalDate fechaDevolucion;
    private LocalDate fechaRealDevolucion;
    @Embedded
    private Multa multa;
    private boolean renovado;

    public Prestamos(Socios socio, Libros libro, LocalDate fechaDevolucion) {
        this.socio = socio;
        this.libro = libro;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = Estado.ACTIVO;
    }
}
