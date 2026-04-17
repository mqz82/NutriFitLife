package com.nutriFitLife.repository;

import com.nutriFitLife.model.Medicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Medicion.
 */
@Repository
public interface MedicionRepository extends JpaRepository<Medicion, Long> {

    /**
     * Retorna todas las mediciones de un paciente ordenadas por fecha descendente
     * (la más reciente primero).
     */
    List<Medicion> findByPacienteIdOrderByFechaMedicionDesc(Long pacienteId);
}
