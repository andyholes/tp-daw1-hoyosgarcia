package ar.edu.undec.tp_integrador_biblioteca.service;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Libros;
import ar.edu.undec.tp_integrador_biblioteca.entity.Prestamos;
import ar.edu.undec.tp_integrador_biblioteca.entity.Socios;
import ar.edu.undec.tp_integrador_biblioteca.exception.ConflictException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ResourceNotFoundException;
import ar.edu.undec.tp_integrador_biblioteca.repository.LibrosRepository;
import ar.edu.undec.tp_integrador_biblioteca.repository.PrestamosRepository;
import ar.edu.undec.tp_integrador_biblioteca.repository.SociosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.undec.tp_integrador_biblioteca.entity.Estado.ACTIVO;
import static ar.edu.undec.tp_integrador_biblioteca.entity.Estado.VENCIDO;

@Service
@RequiredArgsConstructor
public class PrestamosService {
    public static final List<Estado> ESTADOS_NO_FINALES = List.of(ACTIVO, VENCIDO);
    private final PrestamosRepository prestamosRepository;
    private final SociosRepository sociosRepository;
    private final LibrosRepository librosRepository;

    public List<PrestamosResponse> getPrestamosBySocioId(Long socioId) {
        return prestamosRepository.findAllBySocioId(socioId).stream().map(this::mapToDtoWithLibro).toList();
    }

    public PrestamosResponse create(PrestamosRequest request) {
        Long socioId = request.getSocioId();
        Socios socio = sociosRepository.findById(socioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Socio con id %s no encontrado", socioId))
                );

        validarMultasAdeudadas(socioId);
        validarPrestamosDelSocio(socioId);

        Long libroId = request.getLibroId();
        Libros libro = librosRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Libro con id %s no encontrado", libroId))
                );

        validarEstadoLibro(libroId);

        return mapToDtoWithLibroAndSocio(
                prestamosRepository.save(
                        Prestamos.builder()
                                .libro(libro)
                                .socio(socio)
                                .fechaDevolucion(request.getFechaDevolucion())
                                .estado(ACTIVO)
                                .build())
        );
    }

    private void validarMultasAdeudadas(Long socioId) {
        List<Prestamos> prestamosConMulta = prestamosRepository.findAllBySocioIdAndMultaGreaterThan(socioId, BigDecimal.ZERO);

        if (!prestamosConMulta.isEmpty()){
            String multas = prestamosConMulta.stream().map(p -> "Prestamo ID " + p.getId() + " con multa por $" +p.getMulta()).collect(Collectors.joining(", "));
            throw new ConflictException("El socio posee multas en los siguientes prestamos: " + multas);
        }
    }

    private void validarPrestamosDelSocio(Long socioId) {
        List<Prestamos> prestamosNoFinales = prestamosRepository.findAllBySocioIdAndEstadoIn(socioId, ESTADOS_NO_FINALES);
        boolean socioTieneVencidos = prestamosNoFinales.stream().anyMatch(prestamo -> VENCIDO.equals(prestamo.getEstado()));
        boolean socioTieneTresActivos = prestamosNoFinales.stream().filter(prestamo -> ACTIVO.equals(prestamo.getEstado())).count() >= 3;

        if (socioTieneVencidos) {
            throw new ConflictException("El socio tiene prestamos vencidos");
        } else if (socioTieneTresActivos) {
            throw new ConflictException("El socio alcanzo limite de 3 prestamos activos");
        }
    }

    private void validarEstadoLibro(Long libroId) {
        boolean libroNoDisponible = prestamosRepository.existsByLibroIdAndEstadoIn(libroId, ESTADOS_NO_FINALES);
        if (libroNoDisponible) {
            throw new ConflictException("El libro esta en prestamo y aun no fue devuelto");
        }
    }

    private PrestamosResponse mapToDtoWithLibro(Prestamos entity) {
        return PrestamosResponse.builder()
                .id(entity.getId())
                .libro(entity.getLibro())
                .fechaDevolucion(entity.getFechaDevolucion().format(DateTimeFormatter.ISO_DATE))
                .estado(entity.getEstado())
                .build();
    }

    private PrestamosResponse mapToDtoWithLibroAndSocio(Prestamos entity) {
        return PrestamosResponse.builder()
                .id(entity.getId())
                .libro(entity.getLibro())
                .socio(entity.getSocio())
                .fechaDevolucion(entity.getFechaDevolucion().format(DateTimeFormatter.ISO_DATE))
                .estado(entity.getEstado())
                .build();
    }
}
