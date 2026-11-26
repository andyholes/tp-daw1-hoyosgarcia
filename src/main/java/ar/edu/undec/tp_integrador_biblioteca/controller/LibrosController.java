package ar.edu.undec.tp_integrador_biblioteca.controller;

import ar.edu.undec.tp_integrador_biblioteca.dto.LibrosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.LibrosResponse;
import ar.edu.undec.tp_integrador_biblioteca.service.LibrosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibrosController {
    private final LibrosService librosService;

    @GetMapping
    public ResponseEntity<List<LibrosResponse>> getAll(@RequestParam(required = false) boolean disponible) {
        return ResponseEntity.ok(librosService.getAll(disponible));
    }

    @GetMapping("/mas-prestados")
    public ResponseEntity<List<LibrosResponse>> obtenerMasPrestados(@RequestParam(required = false) Integer top) {
        return ResponseEntity.ok(librosService.obtenerMasPrestados(top));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibrosResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(librosService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LibrosResponse> create(@RequestBody @Valid LibrosRequest request) {
        LibrosResponse created = librosService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibrosResponse> update (@PathVariable Long id, @RequestBody @Valid LibrosRequest request){
        return ResponseEntity.ok(librosService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}