package com.nutriFitLife.controller;

import com.nutriFitLife.dto.PacienteDTO;
import com.nutriFitLife.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de pacientes.
 * Expone los endpoints bajo /api/pacientes.
 */
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    /** POST /api/pacientes — Crea un nuevo paciente */
    @PostMapping
    public ResponseEntity<PacienteDTO> crear(@Valid @RequestBody PacienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.crear(dto));
    }

    /** GET /api/pacientes — Lista todos los pacientes */
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listar() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    /** GET /api/pacientes/{id} — Obtiene un paciente por ID */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    /** PUT /api/pacientes/{id} — Actualiza un paciente */
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody PacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.actualizar(id, dto));
    }

    /** DELETE /api/pacientes/{id} — Elimina un paciente y sus mediciones */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /** GET /api/pacientes/buscar?q= — Busca por nombre o RUT */
    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteDTO>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(pacienteService.buscar(q));
    }
}
