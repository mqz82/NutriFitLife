package com.nutriFitLife.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Entidad que representa una evaluación antropométrica completa de un paciente.
 * Contiene todas las mediciones corporales necesarias para calcular
 * composición corporal y somatotipo.
 */
@Entity
@Table(name = "mediciones")
public class Medicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Paciente al que pertenece esta medición (relación N a 1) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @NotNull(message = "La fecha de medición es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaMedicion;

    // =========================================================================
    // MEDICIONES BÁSICAS
    // =========================================================================

    /** Peso corporal en kilogramos */
    @Positive
    @Column(nullable = false)
    private Double peso;

    /** Talla en centímetros */
    @Positive
    @Column(nullable = false)
    private Double talla;

    // =========================================================================
    // PLIEGUES CUTÁNEOS (mm) — Protocolo ISAK 8 pliegues
    // =========================================================================

    /** Pliegue Tricipital (mm) */
    @Positive private Double pliegueTricipal;

    /** Pliegue Subescapular (mm) */
    @Positive private Double pliegueSubescapular;

    /** Pliegue Supraespinal (mm) — también llamado Suprailíaco */
    @Positive private Double pliegueSupraespinal;

    /** Pliegue Abdominal (mm) */
    @Positive private Double pliegueAbdominal;

    /** Pliegue Muslo Frontal (mm) */
    @Positive private Double pliegueMusloFrontal;

    /** Pliegue Pantorrilla Medial (mm) */
    @Positive private Double plieguePantorrillaMed;

    /** Pliegue Bicipital (mm) */
    @Positive private Double pliegueBicipal;

    /** Pliegue Cresta Ilíaca (mm) */
    @Positive private Double pliegueCrestaIliaca;

    // =========================================================================
    // PERÍMETROS (cm)
    // =========================================================================

    /** Perímetro brazo relajado (cm) */
    @Positive private Double perimetroBrazoRelajado;

    /** Perímetro brazo flexionado en tensión (cm) */
    @Positive private Double perimetroBrazoFlexTension;

    /** Perímetro cintura mínima (cm) */
    @Positive private Double perimetroCinturaMinima;

    /** Perímetro cadera máximo (cm) */
    @Positive private Double perimetroCaderaMaximo;

    /** Perímetro muslo medial (cm) */
    @Positive private Double perimetroMusloMedal;

    /** Perímetro pantorrilla máximo (cm) */
    @Positive private Double perimetroPantorrillaMax;

    // =========================================================================
    // DIÁMETROS ÓSEOS (cm) — Requeridos para cálculo de mesomorfia
    // =========================================================================

    /** Diámetro biepicondilar del húmero (codo) en cm */
    @Positive private Double diametroCodo;

    /** Diámetro bicondilar del fémur (rodilla) en cm */
    @Positive private Double diametroRodilla;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public Medicion() {}

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public LocalDate getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(LocalDate fechaMedicion) { this.fechaMedicion = fechaMedicion; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getTalla() { return talla; }
    public void setTalla(Double talla) { this.talla = talla; }

    // Pliegues
    public Double getPliegueTricipal() { return pliegueTricipal; }
    public void setPliegueTricipal(Double pliegueTricipal) { this.pliegueTricipal = pliegueTricipal; }

    public Double getPliegueSubescapular() { return pliegueSubescapular; }
    public void setPliegueSubescapular(Double pliegueSubescapular) { this.pliegueSubescapular = pliegueSubescapular; }

    public Double getPliegueSupraespinal() { return pliegueSupraespinal; }
    public void setPliegueSupraespinal(Double pliegueSupraespinal) { this.pliegueSupraespinal = pliegueSupraespinal; }

    public Double getPliegueAbdominal() { return pliegueAbdominal; }
    public void setPliegueAbdominal(Double pliegueAbdominal) { this.pliegueAbdominal = pliegueAbdominal; }

    public Double getPliegueMusloFrontal() { return pliegueMusloFrontal; }
    public void setPliegueMusloFrontal(Double pliegueMusloFrontal) { this.pliegueMusloFrontal = pliegueMusloFrontal; }

    public Double getPlieguePantorrillaMed() { return plieguePantorrillaMed; }
    public void setPlieguePantorrillaMed(Double plieguePantorrillaMed) { this.plieguePantorrillaMed = plieguePantorrillaMed; }

    public Double getPliegueBicipal() { return pliegueBicipal; }
    public void setPliegueBicipal(Double pliegueBicipal) { this.pliegueBicipal = pliegueBicipal; }

    public Double getPliegueCrestaIliaca() { return pliegueCrestaIliaca; }
    public void setPliegueCrestaIliaca(Double pliegueCrestaIliaca) { this.pliegueCrestaIliaca = pliegueCrestaIliaca; }

    // Perímetros
    public Double getPerimetroBrazoRelajado() { return perimetroBrazoRelajado; }
    public void setPerimetroBrazoRelajado(Double v) { this.perimetroBrazoRelajado = v; }

    public Double getPerimetroBrazoFlexTension() { return perimetroBrazoFlexTension; }
    public void setPerimetroBrazoFlexTension(Double v) { this.perimetroBrazoFlexTension = v; }

    public Double getPerimetroCinturaMinima() { return perimetroCinturaMinima; }
    public void setPerimetroCinturaMinima(Double v) { this.perimetroCinturaMinima = v; }

    public Double getPerimetroCaderaMaximo() { return perimetroCaderaMaximo; }
    public void setPerimetroCaderaMaximo(Double v) { this.perimetroCaderaMaximo = v; }

    public Double getPerimetroMusloMedal() { return perimetroMusloMedal; }
    public void setPerimetroMusloMedal(Double v) { this.perimetroMusloMedal = v; }

    public Double getPerimetroPantorrillaMax() { return perimetroPantorrillaMax; }
    public void setPerimetroPantorrillaMax(Double v) { this.perimetroPantorrillaMax = v; }

    // Diámetros
    public Double getDiametroCodo() { return diametroCodo; }
    public void setDiametroCodo(Double diametroCodo) { this.diametroCodo = diametroCodo; }

    public Double getDiametroRodilla() { return diametroRodilla; }
    public void setDiametroRodilla(Double diametroRodilla) { this.diametroRodilla = diametroRodilla; }
}
