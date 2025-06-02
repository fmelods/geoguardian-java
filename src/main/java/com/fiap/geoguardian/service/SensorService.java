package com.fiap.geoguardian.service;

import com.fiap.geoguardian.model.Sensor;
import com.fiap.geoguardian.repository.AreaRiscoRepository;
import com.fiap.geoguardian.repository.ModeloSensorRepository;
import com.fiap.geoguardian.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SensorService {
    
    @Autowired
    private SensorRepository sensorRepository;
    
    @Autowired
    private AreaRiscoRepository areaRiscoRepository;
    
    @Autowired
    private ModeloSensorRepository modeloSensorRepository;
    
    public Page<Sensor> findAll(Pageable pageable) {
        return sensorRepository.findAll(pageable);
    }
    
    public Page<Sensor> findByStatus(String status, Pageable pageable) {
        return sensorRepository.findByStatusContainingIgnoreCase(status, pageable);
    }
    
    public Page<Sensor> findByAreaRiscoId(Long areaRiscoId, Pageable pageable) {
        return sensorRepository.findByAreaRiscoId(areaRiscoId, pageable);
    }
    
    public Page<Sensor> findByModeloSensorId(Long modeloSensorId, Pageable pageable) {
        return sensorRepository.findByModeloSensorId(modeloSensorId, pageable);
    }
    
    public Optional<Sensor> findById(Long id) {
        return sensorRepository.findById(id);
    }
    
    public Optional<Sensor> findByUuid(String uuid) {
        return sensorRepository.findByUuid(uuid);
    }
    
    public Sensor save(Sensor sensor) {
        if (sensorRepository.existsByUuid(sensor.getUuid())) {
            throw new RuntimeException("Já existe um sensor com este UUID");
        }
        
        if (!areaRiscoRepository.existsById(sensor.getAreaRisco().getId())) {
            throw new RuntimeException("Área de risco não encontrada");
        }
        
        if (!modeloSensorRepository.existsById(sensor.getModeloSensor().getId())) {
            throw new RuntimeException("Modelo de sensor não encontrado");
        }
        
        return sensorRepository.save(sensor);
    }
    
    public Sensor update(Long id, Sensor sensor) {
        Optional<Sensor> existingSensor = sensorRepository.findById(id);
        if (existingSensor.isEmpty()) {
            throw new RuntimeException("Sensor não encontrado");
        }
        
        if (sensorRepository.existsByUuid(sensor.getUuid()) && 
            !existingSensor.get().getUuid().equals(sensor.getUuid())) {
            throw new RuntimeException("Já existe um sensor com este UUID");
        }
        
        if (!areaRiscoRepository.existsById(sensor.getAreaRisco().getId())) {
            throw new RuntimeException("Área de risco não encontrada");
        }
        
        if (!modeloSensorRepository.existsById(sensor.getModeloSensor().getId())) {
            throw new RuntimeException("Modelo de sensor não encontrado");
        }
        
        sensor.setId(id);
        return sensorRepository.save(sensor);
    }
    
    public void deleteById(Long id) {
        if (!sensorRepository.existsById(id)) {
            throw new RuntimeException("Sensor não encontrado");
        }
        sensorRepository.deleteById(id);
    }
}
