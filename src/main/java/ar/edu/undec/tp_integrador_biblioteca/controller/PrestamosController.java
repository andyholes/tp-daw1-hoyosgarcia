package ar.edu.undec.tp_integrador_biblioteca.controller;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.service.PrestamosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamosController {
    private final PrestamosService prestamosService;

    @PostMapping
    public ResponseEntity<PrestamosResponse> create(@RequestBody @Valid PrestamosRequest request) {
        PrestamosResponse created = prestamosService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<PrestamosResponse>> getAll(@RequestParam(required = false) Estado estado) {
        return ResponseEntity.ok(prestamosService.getAll(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamosResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prestamosService.getById(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PrestamosResponse> changeStatus(@PathVariable Long id, @RequestBody Estado estado) {
        return ResponseEntity.ok(prestamosService.changeStatus(id, estado));
    }

    @PatchMapping("/{id}/renovar")
    public ResponseEntity<PrestamosResponse> renovarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(prestamosService.renovarStatus(id));
    }

    @GetMapping("/{id}/pagar-multa")
    public ResponseEntity<PrestamosResponse> pagarMulta(@PathVariable Long id) {
        return ResponseEntity.ok(prestamosService.pagarMulta(id));
    }
}
