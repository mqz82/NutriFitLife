package com.nutriFitLife.repository;

import com.nutriFitLife.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Paciente.
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca pacientes cuyo nombre completo o RUT contengan el texto indicado.
     * La búsqueda es insensible a mayúsculas/minúsculas (LOWER).
     */
    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(CONCAT(p.nombres, ' ', p.apellidos)) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(p.rut) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Paciente> buscarPorNombreORut(@Param("q") String q);

    /** Verifica si ya existe un paciente con el mismo RUT */
    boolean existsByRut(String rut);

    /** Busca un paciente por RUT exacto */
    Optional<Paciente> findByRut(String rut);
}
