package ar.edu.undec.tp_integrador_biblioteca.service;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Libros;
import ar.edu.undec.tp_integrador_biblioteca.entity.Multa;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.undec.tp_integrador_biblioteca.entity.Estado.ACTIVO;
import static ar.edu.undec.tp_integrador_biblioteca.entity.Estado.DEVUELTO;
import static ar.edu.undec.tp_integrador_biblioteca.entity.Estado.VENCIDO;
import static ar.edu.undec.tp_integrador_biblioteca.repository.specification.PrestamosSpecs.conEstados;
import static ar.edu.undec.tp_integrador_biblioteca.repository.specification.PrestamosSpecs.conMultaImpaga;
import static ar.edu.undec.tp_integrador_biblioteca.repository.specification.PrestamosSpecs.conSocioId;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Service
@RequiredArgsConstructor
public class PrestamosService {
    public static final List<Estado> ESTADOS_NO_FINALES = List.of(ACTIVO, VENCIDO);
    private final PrestamosRepository prestamosRepository;
    private final SociosRepository sociosRepository;
    private final LibrosRepository librosRepository;

    public List<PrestamosResponse> getPrestamosBySocioId(Long socioId) {
        return prestamosRepository.findAllBySocioId(socioId).stream().map(this::mapToDto).toList();
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

        return mapToDto(
                prestamosRepository.save(
                        new Prestamos(socio, libro, request.getFechaDevolucion())
                )
        );
    }

    public List<PrestamosResponse> getAll(Estado estado) {
        return prestamosRepository.findAll(conEstados(estado)).stream()
                .map(this::mapToDto).toList();
    }

    public PrestamosResponse getById(Long id) {
        return mapToDto(getPrestamoById(id));
    }

    public PrestamosResponse changeStatus(Long id, Estado estadoNuevo) {
        Prestamos prestamo = getPrestamoById(id);
        Estado estadoActual = prestamo.getEstado();

        if (!estadoActual.equals(estadoNuevo)) {
            if (DEVUELTO.equals(estadoActual) || VENCIDO.equals(estadoActual) && ACTIVO.equals(estadoNuevo)) {
                throw new ConflictException("No puede reactivarse un prestamo en estado " + estadoActual.name().toLowerCase());
            }

            LocalDate fechaReal = LocalDate.now();
            prestamo.setFechaRealDevolucion(fechaReal);

            if (VENCIDO.equals(estadoActual) && DEVUELTO.equals(estadoNuevo)) {
                LocalDate fechaLimite = prestamo.getFechaDevolucion();
                long diasEnFalta = Math.max(0L, fechaReal.toEpochDay() - fechaLimite.toEpochDay());
                BigDecimal montoPorDia = BigDecimal.valueOf(500);
                BigDecimal montoTotal = montoPorDia.multiply(BigDecimal.valueOf(diasEnFalta));

                prestamo.setMulta(new Multa(montoTotal));
            }

            prestamo.setEstado(estadoNuevo);
            prestamosRepository.save(prestamo);
        }

        return mapToDto(prestamo);
    }

    public PrestamosResponse renovarStatus(Long id) {
        Prestamos prestamo = getPrestamoById(id);
        if (prestamo.isRenovado()) {
            throw new ConflictException("Los prestamos solo pueden renovarse una vez");
        }
        if (!ACTIVO.equals(prestamo.getEstado())) {
            throw new ConflictException("Solo pueden renovarse los prestamos activos");
        }

        prestamo.setRenovado(true);
        prestamo.setFechaDevolucion(prestamo.getFechaDevolucion().plusDays(7));
        prestamosRepository.save(prestamo);
        return mapToDto(prestamo);

    }

    public PrestamosResponse pagarMulta(Long id) {
        Prestamos prestamo = getPrestamoById(id);
        prestamo.getMulta().setSaldado(true);
        prestamosRepository.save(prestamo);

        return mapToDto(prestamo);
    }

    private Prestamos getPrestamoById(Long id) {
        return prestamosRepository.findWithLibroAndSocioById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Prestamo con id %s no encontrado", id)
                ));
    }

    private void validarMultasAdeudadas(Long socioId) {
        List<Prestamos> prestamosConMulta = prestamosRepository.findAll(conSocioId(socioId).and(conMultaImpaga()));

        if (!prestamosConMulta.isEmpty()) {
            String multas = prestamosConMulta.stream().map(p -> "ID " + p.getId() + " debe $" + p.getMulta().getMonto()).collect(Collectors.joining(", "));
            throw new ConflictException("El socio posee multas impagas en los siguientes prestamos: " + multas);
        }
    }

    private void validarPrestamosDelSocio(Long socioId) {
        List<Prestamos> prestamosNoFinales = prestamosRepository.findAll(conSocioId(socioId).and(conEstados(ESTADOS_NO_FINALES)));
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
            throw new ConflictException("El libro se encuentra actualmente en prestamo");
        }
    }

    private PrestamosResponse mapToDto(Prestamos entity) {
        Socios socio = entity.getSocio() != null && getPersistenceUtil().isLoaded(entity, "socio") ? entity.getSocio() : null;
        Libros libro = entity.getLibro() != null && getPersistenceUtil().isLoaded(entity, "libro") ? entity.getLibro() : null;

        LocalDate fechaRealDevolucion = entity.getFechaRealDevolucion();
        return PrestamosResponse.builder()
                .id(entity.getId())
                .libro(libro)
                .socio(socio)
                .estado(entity.getEstado())
                .fechaDevolucion(entity.getFechaDevolucion().format(DateTimeFormatter.ISO_DATE))
                .fechaRealDevolucion(fechaRealDevolucion != null ? fechaRealDevolucion.format(DateTimeFormatter.ISO_DATE) : null)
                .multa(entity.getMulta())
                .renovado(entity.isRenovado())
                .build();
    }
}
