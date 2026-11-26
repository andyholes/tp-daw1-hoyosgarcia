package ar.edu.undec.tp_integrador_biblioteca.controller;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.dto.SociosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.SociosResponse;
import ar.edu.undec.tp_integrador_biblioteca.service.SociosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SociosController {

    private final SociosService sociosService;

    @GetMapping
    public ResponseEntity<List<SociosResponse>> getAll() {
        return ResponseEntity.ok(sociosService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SociosResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sociosService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SociosResponse> create(@RequestBody @Valid SociosRequest request) {
        SociosResponse created = sociosService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}/prestamos")
    public ResponseEntity<List<PrestamosResponse>> obtenerMasPrestados(@RequestParam Long id) {
        return ResponseEntity.ok(sociosService.consultarPrestamos(id));
    }

}
