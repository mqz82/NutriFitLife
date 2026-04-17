package com.nutriFitLife.service;

import com.nutriFitLife.dto.PacienteDTO;
import com.nutriFitLife.model.Paciente;
import com.nutriFitLife.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para la gestión de pacientes.
 * Convierte entre entidades JPA y DTOs para exponer solo lo necesario en la API.
 */
@Service
@Transactional
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /** Crea un nuevo paciente a partir de los datos del DTO */
    public PacienteDTO crear(PacienteDTO dto) {
        if (pacienteRepository.existsByRut(dto.getRut())) {
            throw new IllegalArgumentException("Ya existe un paciente con el RUT: " + dto.getRut());
        }
        Paciente entidad = dtoAEntidad(dto);
        return entidadADto(pacienteRepository.save(entidad));
    }

    /** Retorna todos los pacientes ordenados por apellidos */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::entidadADto)
                .collect(Collectors.toList());
    }

    /** Obtiene un paciente por ID; lanza excepción si no existe */
    @Transactional(readOnly = true)
    public PacienteDTO obtenerPorId(Long id) {
        return entidadADto(buscarEntidadPorId(id));
    }

    /** Actualiza los datos de un paciente existente */
    public PacienteDTO actualizar(Long id, PacienteDTO dto) {
        Paciente existente = buscarEntidadPorId(id);

        // Verificar que el nuevo RUT no pertenezca a otro paciente
        if (!existente.getRut().equals(dto.getRut())
                && pacienteRepository.existsByRut(dto.getRut())) {
            throw new IllegalArgumentException("Ya existe un paciente con el RUT: " + dto.getRut());
        }

        existente.setNombres(dto.getNombres());
        existente.setApellidos(dto.getApellidos());
        existente.setRut(dto.getRut());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setSexo(dto.getSexo());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());

        return entidadADto(pacienteRepository.save(existente));
    }

    /** Elimina un paciente y en cascada todas sus mediciones */
    public void eliminar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Paciente no encontrado: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    /** Busca pacientes por nombre o RUT */
    @Transactional(readOnly = true)
    public List<PacienteDTO> buscar(String q) {
        return pacienteRepository.buscarPorNombreORut(q)
                .stream()
                .map(this::entidadADto)
                .collect(Collectors.toList());
    }

    /** Retorna la entidad JPA por ID (uso interno de otros servicios) */
    public Paciente buscarEntidadPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Paciente no encontrado: " + id));
    }

    // -------------------------------------------------------------------------
    // Mapeo entidad ↔ DTO
    // -------------------------------------------------------------------------

    private PacienteDTO entidadADto(Paciente p) {
        return new PacienteDTO(
                p.getId(), p.getNombres(), p.getApellidos(),
                p.getRut(), p.getFechaNacimiento(), p.getSexo(),
                p.getEmail(), p.getTelefono(), p.getCreadoEn());
    }

    private Paciente dtoAEntidad(PacienteDTO dto) {
        Paciente p = new Paciente();
        p.setNombres(dto.getNombres());
        p.setApellidos(dto.getApellidos());
        p.setRut(dto.getRut());
        p.setFechaNacimiento(dto.getFechaNacimiento());
        p.setSexo(dto.getSexo());
        p.setEmail(dto.getEmail());
        p.setTelefono(dto.getTelefono());
        return p;
    }
}
