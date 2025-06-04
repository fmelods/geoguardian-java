package com.fiap.geoguardian.controller;

import com.fiap.geoguardian.model.Pais;
import com.fiap.geoguardian.service.PaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@RestController
@RequestMapping("/api/paises")
@Tag(name = "Países", description = "Operações relacionadas aos países")
public class PaisController {

    @Autowired
    private PaisService paisService;

    @GetMapping
    @Operation(summary = "Listar todos os países", description = "Retorna uma lista paginada de países")
    public ResponseEntity<Page<Pais>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<Pais> paises;
        if (nome != null && !nome.trim().isEmpty()) {
            paises = paisService.findByNome(nome, pageable);
        } else {
            paises = paisService.findAll(pageable);
        }

        return ResponseEntity.ok(paises);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar país por ID", description = "Retorna um país específico pelo ID")
    public ResponseEntity<Pais> findById(@PathVariable Long id) {
        Optional<Pais> pais = paisService.findById(id);
        return pais.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo país", description = "Cria um novo país")
    public ResponseEntity<Pais> create(@Valid @RequestBody Pais pais) {
        try {
            Pais savedPais = paisService.save(pais);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPais);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar país", description = "Atualiza um país existente")
    public ResponseEntity<Pais> update(@PathVariable Long id, @Valid @RequestBody Pais pais) {
        try {
            Pais updatedPais = paisService.update(id, pais);
            return ResponseEntity.ok(updatedPais);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar país", description = "Remove um país")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            paisService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
