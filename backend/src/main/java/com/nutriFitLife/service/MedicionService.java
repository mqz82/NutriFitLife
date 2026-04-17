package com.nutriFitLife.service;

import com.nutriFitLife.dto.MedicionDTO;
import com.nutriFitLife.dto.ResultadoAntropometricoDTO;
import com.nutriFitLife.model.Medicion;
import com.nutriFitLife.model.Paciente;
import com.nutriFitLife.repository.MedicionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para la gestión de mediciones antropométricas.
 */
@Service
@Transactional
public class MedicionService {

    private final MedicionRepository medicionRepository;
    private final PacienteService pacienteService;
    private final AntropometriaCalculatorService calculadorService;

    public MedicionService(MedicionRepository medicionRepository,
                           PacienteService pacienteService,
                           AntropometriaCalculatorService calculadorService) {
        this.medicionRepository  = medicionRepository;
        this.pacienteService     = pacienteService;
        this.calculadorService   = calculadorService;
    }

    /** Registra una nueva medición para un paciente existente */
    public MedicionDTO registrar(MedicionDTO dto) {
        Paciente paciente = pacienteService.buscarEntidadPorId(dto.getPacienteId());
        Medicion entidad  = dtoAEntidad(dto, paciente);
        return entidadADto(medicionRepository.save(entidad));
    }

    /** Lista el historial de mediciones de un paciente (orden descendente por fecha) */
    @Transactional(readOnly = true)
    public List<MedicionDTO> listarPorPaciente(Long pacienteId) {
        return medicionRepository
                .findByPacienteIdOrderByFechaMedicionDesc(pacienteId)
                .stream()
                .map(this::entidadADto)
                .collect(Collectors.toList());
    }

    /** Obtiene una medición por ID */
    @Transactional(readOnly = true)
    public MedicionDTO obtenerPorId(Long id) {
        return entidadADto(buscarEntidadPorId(id));
    }

    /** Calcula y retorna el resultado antropométrico completo de una medición */
    @Transactional(readOnly = true)
    public ResultadoAntropometricoDTO calcularResultado(Long id) {
        Medicion medicion = buscarEntidadPorId(id);
        return calculadorService.calcularTodo(medicion, medicion.getPaciente());
    }

    /** Elimina una medición por ID */
    public void eliminar(Long id) {
        if (!medicionRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Medición no encontrada: " + id);
        }
        medicionRepository.deleteById(id);
    }

    /** Retorna la entidad JPA por ID (uso interno) */
    public Medicion buscarEntidadPorId(Long id) {
        return medicionRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Medición no encontrada: " + id));
    }

    // -------------------------------------------------------------------------
    // Mapeo entidad ↔ DTO
    // -------------------------------------------------------------------------

    private MedicionDTO entidadADto(Medicion m) {
        MedicionDTO dto = new MedicionDTO();
        dto.setId(m.getId());
        dto.setPacienteId(m.getPaciente().getId());
        dto.setFechaMedicion(m.getFechaMedicion());
        dto.setPeso(m.getPeso());
        dto.setTalla(m.getTalla());
        dto.setPliegueTricipal(m.getPliegueTricipal());
        dto.setPliegueSubescapular(m.getPliegueSubescapular());
        dto.setPliegueSupraespinal(m.getPliegueSupraespinal());
        dto.setPliegueAbdominal(m.getPliegueAbdominal());
        dto.setPliegueMusloFrontal(m.getPliegueMusloFrontal());
        dto.setPlieguePantorrillaMed(m.getPlieguePantorrillaMed());
        dto.setPliegueBicipal(m.getPliegueBicipal());
        dto.setPliegueCrestaIliaca(m.getPliegueCrestaIliaca());
        dto.setPerimetroBrazoRelajado(m.getPerimetroBrazoRelajado());
        dto.setPerimetroBrazoFlexTension(m.getPerimetroBrazoFlexTension());
        dto.setPerimetroCinturaMinima(m.getPerimetroCinturaMinima());
        dto.setPerimetroCaderaMaximo(m.getPerimetroCaderaMaximo());
        dto.setPerimetroMusloMedal(m.getPerimetroMusloMedal());
        dto.setPerimetroPantorrillaMax(m.getPerimetroPantorrillaMax());
        dto.setDiametroCodo(m.getDiametroCodo());
        dto.setDiametroRodilla(m.getDiametroRodilla());
        return dto;
    }

    private Medicion dtoAEntidad(MedicionDTO dto, Paciente paciente) {
        Medicion m = new Medicion();
        m.setPaciente(paciente);
        m.setFechaMedicion(dto.getFechaMedicion());
        m.setPeso(dto.getPeso());
        m.setTalla(dto.getTalla());
        m.setPliegueTricipal(dto.getPliegueTricipal());
        m.setPliegueSubescapular(dto.getPliegueSubescapular());
        m.setPliegueSupraespinal(dto.getPliegueSupraespinal());
        m.setPliegueAbdominal(dto.getPliegueAbdominal());
        m.setPliegueMusloFrontal(dto.getPliegueMusloFrontal());
        m.setPlieguePantorrillaMed(dto.getPlieguePantorrillaMed());
        m.setPliegueBicipal(dto.getPliegueBicipal());
        m.setPliegueCrestaIliaca(dto.getPliegueCrestaIliaca());
        m.setPerimetroBrazoRelajado(dto.getPerimetroBrazoRelajado());
        m.setPerimetroBrazoFlexTension(dto.getPerimetroBrazoFlexTension());
        m.setPerimetroCinturaMinima(dto.getPerimetroCinturaMinima());
        m.setPerimetroCaderaMaximo(dto.getPerimetroCaderaMaximo());
        m.setPerimetroMusloMedal(dto.getPerimetroMusloMedal());
        m.setPerimetroPantorrillaMax(dto.getPerimetroPantorrillaMax());
        m.setDiametroCodo(dto.getDiametroCodo());
        m.setDiametroRodilla(dto.getDiametroRodilla());
        return m;
    }
}
