package ar.edu.undec.tp_integrador_biblioteca.controller;

import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosRequest;
import ar.edu.undec.tp_integrador_biblioteca.dto.PrestamosResponse;
import ar.edu.undec.tp_integrador_biblioteca.service.PrestamosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamosController {
    private final PrestamosService prestamosService;

    @PostMapping
    public ResponseEntity<PrestamosResponse> create(@RequestBody @Valid PrestamosRequest request){
        PrestamosResponse created = prestamosService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
}
