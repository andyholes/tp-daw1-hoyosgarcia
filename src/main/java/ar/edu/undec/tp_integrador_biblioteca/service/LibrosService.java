package ar.edu.undec.tp_integrador_biblioteca.service;

import ar.edu.undec.tp_integrador_biblioteca.dto.LibrosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.LibrosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Libros;
import ar.edu.undec.tp_integrador_biblioteca.exception.ConflictException;
import ar.edu.undec.tp_integrador_biblioteca.exception.ResourceNotFoundException;
import ar.edu.undec.tp_integrador_biblioteca.repository.LibrosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibrosService {
    private final LibrosRepository librosRepository;

    public List<LibrosResponse> getAll(boolean disponible) {
        List<Libros> libros;

        if (disponible){
            libros = librosRepository.getAllLibrosByEstado(Estado.ACTIVO);
        } else {
            libros = librosRepository.findAll();
        }

        return libros.stream().map(this::mapToDto).toList();
    }

    public LibrosResponse getById(Long id) {
        return mapToDto(findById(id));
    }

    public void deleteById(Long id) {
        librosRepository.delete(findById(id));
    }

    public LibrosResponse create(LibrosRequest request) {
        if (librosRepository.existsByIsbn(request.getIsbn()))
            throw new ConflictException("El ISBN informado ya esta en uso por otro libro");

        Libros saved = librosRepository.save(
                Libros.builder()
                        .titulo(request.getTitulo())
                        .autor(request.getAutor())
                        .isbn(request.getIsbn())
                        .build());
        return mapToDto(saved);
    }

    public LibrosResponse update(Long id, LibrosRequest request) {
        Libros entity = findById(id);

        // valido que no existe solo si es diferente del que ya tiene
        if (!request.getIsbn().equals(entity.getIsbn()) && librosRepository.existsByIsbn(request.getIsbn()))
            throw new ConflictException("El ISBN informado ya esta en uso por otro libro");

        entity.setTitulo(request.getTitulo());
        entity.setAutor(request.getAutor());
        entity.setIsbn(request.getIsbn());
        librosRepository.save(entity);
        return mapToDto(entity);
    }

    public List<LibrosResponse> obtenerMasPrestados(Integer top) {
        return librosRepository.findLibrosMasPrestados(
                Pageable.ofSize(top != null ? top : 5)
        );
    }

    private Libros findById(Long id) {
        return librosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Libro con id %s no encontrado", id))
                );
    }

    private LibrosResponse mapToDto(Libros entity) {
        return LibrosResponse.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .autor(entity.getAutor())
                .isdb(entity.getIsbn())
                .build();
    }
}
