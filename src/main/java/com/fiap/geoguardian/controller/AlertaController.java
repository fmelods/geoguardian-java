package com.fiap.geoguardian.controller;

import com.fiap.geoguardian.model.Alerta;
import com.fiap.geoguardian.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas", description = "Operações relacionadas aos alertas")
public class AlertaController {
    
    @Autowired
    private AlertaService alertaService;
    
    @GetMapping
    @Operation(summary = "Listar todos os alertas", description = "Retorna uma lista paginada de alertas")
    public ResponseEntity<Page<Alerta>> findAll(
            @RequestParam(required = false) Integer nivelRisco,
            @RequestParam(required = false) Long tipoAlertaId,
            @RequestParam(required = false) Long areaRiscoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Alerta> alertas;
        if (inicio != null && fim != null) {
            alertas = alertaService.findByDataHoraBetween(inicio, fim, pageable);
        } else if (nivelRisco != null) {
            alertas = alertaService.findByNivelRisco(nivelRisco, pageable);
        } else if (tipoAlertaId != null) {
            alertas = alertaService.findByTipoAlertaId(tipoAlertaId, pageable);
        } else if (areaRiscoId != null) {
            alertas = alertaService.findByAreaRiscoId(areaRiscoId, pageable);
        } else {
            alertas = alertaService.findAll(pageable);
        }
        
        return ResponseEntity.ok(alertas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID", description = "Retorna um alerta específico pelo ID")
    public ResponseEntity<Alerta> findById(@PathVariable Long id) {
        Optional<Alerta> alerta = alertaService.findById(id);
        return alerta.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Criar novo alerta", description = "Cria um novo alerta")
    public ResponseEntity<Alerta> create(@Valid @RequestBody Alerta alerta) {
        try {
            Alerta savedAlerta = alertaService.save(alerta);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAlerta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar alerta", description = "Atualiza um alerta existente")
    public ResponseEntity<Alerta> update(@PathVariable Long id, @Valid @RequestBody Alerta alerta) {
        try {
            Alerta updatedAlerta = alertaService.update(id, alerta);
            return ResponseEntity.ok(updatedAlerta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar alerta", description = "Remove um alerta")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            alertaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/estatisticas/tipo/{tipoAlertaId}")
    @Operation(summary = "Contar alertas por tipo", description = "Retorna a quantidade de alertas por tipo")
    public ResponseEntity<Long> countByTipoAlertaId(@PathVariable Long tipoAlertaId) {
        Long count = alertaService.countByTipoAlertaId(tipoAlertaId);
        return ResponseEntity.ok(count);
    }
}
