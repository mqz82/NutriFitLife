package com.nutriFitLife.controller;

import com.nutriFitLife.dto.MedicionDTO;
import com.nutriFitLife.dto.ResultadoAntropometricoDTO;
import com.nutriFitLife.service.MedicionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de mediciones antropométricas.
 * Expone los endpoints bajo /api/mediciones.
 */
@RestController
@RequestMapping("/api/mediciones")
public class MedicionController {

    private final MedicionService medicionService;

    public MedicionController(MedicionService medicionService) {
        this.medicionService = medicionService;
    }

    /** POST /api/mediciones — Registra una nueva medición */
    @PostMapping
    public ResponseEntity<MedicionDTO> registrar(@Valid @RequestBody MedicionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicionService.registrar(dto));
    }

    /** GET /api/mediciones/paciente/{pacienteId} — Historial de mediciones de un paciente */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<MedicionDTO>> listarPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(medicionService.listarPorPaciente(pacienteId));
    }

    /** GET /api/mediciones/{id} — Obtiene una medición por ID */
    @GetMapping("/{id}")
    public ResponseEntity<MedicionDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(medicionService.obtenerPorId(id));
    }

    /** GET /api/mediciones/{id}/resultado — Calcula y retorna el resultado completo */
    @GetMapping("/{id}/resultado")
    public ResponseEntity<ResultadoAntropometricoDTO> calcularResultado(
            @PathVariable Long id) {
        return ResponseEntity.ok(medicionService.calcularResultado(id));
    }

    /** DELETE /api/mediciones/{id} — Elimina una medición */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
