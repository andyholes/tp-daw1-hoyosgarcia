package ar.edu.undec.tp_integrador_biblioteca.service;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.dto.SociosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.SociosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Socios;
import ar.edu.undec.tp_integrador_biblioteca.exception.ConflictException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ResourceNotFoundException;
import ar.edu.undec.tp_integrador_biblioteca.repository.SociosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SociosService {
    private final SociosRepository sociosRepository;
    private final PrestamosService prestamosService;

    public List<SociosResponse> getAll() {
        return sociosRepository.findAll().stream().map(this::mapToDto).toList();
    }

    public SociosResponse getById(Long id) {
        return mapToDto(sociosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Socio con id %s no encontrado", id)
                ))
        );
    }

    public SociosResponse create(SociosRequest request) {
        if (sociosRepository.existsByEmail(request.getEmail()))
            throw new ConflictException("El correo ya se encuentra registrado");

        Socios saved = sociosRepository.save(
                Socios.builder()
                        .nombre(request.getNombre())
                        .email(request.getEmail())
                        .build());

        return mapToDto(saved);
    }

    public List<PrestamosResponse> consultarPrestamos(Long socioId) {
        return prestamosService.getPrestamosBySocioId(socioId);
    }

    private SociosResponse mapToDto(Socios entity) {
        return SociosResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .build();
    }
}
