package com.fiap.geoguardian.controller;

import com.fiap.geoguardian.model.Sensor;
import com.fiap.geoguardian.service.SensorService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/sensores")
@Tag(name = "Sensores", description = "Operações relacionadas aos sensores")
public class SensorController {
    
    @Autowired
    private SensorService sensorService;
    
    @GetMapping
    @Operation(summary = "Listar todos os sensores", description = "Retorna uma lista paginada de sensores")
    public ResponseEntity<Page<Sensor>> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long areaRiscoId,
            @RequestParam(required = false) Long modeloSensorId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Sensor> sensores;
        if (status != null && !status.trim().isEmpty()) {
            sensores = sensorService.findByStatus(status, pageable);
        } else if (areaRiscoId != null) {
            sensores = sensorService.findByAreaRiscoId(areaRiscoId, pageable);
        } else if (modeloSensorId != null) {
            sensores = sensorService.findByModeloSensorId(modeloSensorId, pageable);
        } else {
            sensores = sensorService.findAll(pageable);
        }
        
        return ResponseEntity.ok(sensores);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar sensor por ID", description = "Retorna um sensor específico pelo ID")
    public ResponseEntity<Sensor> findById(@PathVariable Long id) {
        Optional<Sensor> sensor = sensorService.findById(id);
        return sensor.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Buscar sensor por UUID", description = "Retorna um sensor específico pelo UUID")
    public ResponseEntity<Sensor> findByUuid(@PathVariable String uuid) {
        Optional<Sensor> sensor = sensorService.findByUuid(uuid);
        return sensor.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Criar novo sensor", description = "Cria um novo sensor")
    public ResponseEntity<Sensor> create(@Valid @RequestBody Sensor sensor) {
        try {
            Sensor savedSensor = sensorService.save(sensor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSensor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sensor", description = "Atualiza um sensor existente")
    public ResponseEntity<Sensor> update(@PathVariable Long id, @Valid @RequestBody Sensor sensor) {
        try {
            Sensor updatedSensor = sensorService.update(id, sensor);
            return ResponseEntity.ok(updatedSensor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sensor", description = "Remove um sensor")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            sensorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
