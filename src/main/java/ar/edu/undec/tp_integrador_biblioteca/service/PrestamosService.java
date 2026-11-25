package ar.edu.undec.tp_integrador_biblioteca.service;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Prestamos;
import ar.edu.undec.tp_integrador_biblioteca.repository.PrestamosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamosService {
    private final PrestamosRepository prestamosRepository;
    public List<PrestamosResponse> getPrestamosBySocioId(Long socioId) {
        return prestamosRepository.findAllBySocioId(socioId).stream().map(this::mapToDtoWithTitulo).toList();
    }

    private PrestamosResponse mapToDtoWithTitulo(Prestamos entity){
        return PrestamosResponse.builder()
                .id(entity.getId())
                .libro(entity.getLibro().getTitulo())
                .fechaDevolucion(entity.getFechaDevolucion().format(DateTimeFormatter.ISO_DATE))
                .estado(entity.getEstado())
                .build();
    }
}
