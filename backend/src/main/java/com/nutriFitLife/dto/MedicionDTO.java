package com.nutriFitLife.dto;

import java.time.LocalDate;

/**
 * DTO para transferir datos de una medición antropométrica.
 * Contiene todos los campos medibles del protocolo ISAK.
 */
public class MedicionDTO {

    private Long id;
    private Long pacienteId;
    private LocalDate fechaMedicion;

    // Básicos
    private Double peso;
    private Double talla;

    // Pliegues cutáneos (mm)
    private Double pliegueTricipal;
    private Double pliegueSubescapular;
    private Double pliegueSupraespinal;
    private Double pliegueAbdominal;
    private Double pliegueMusloFrontal;
    private Double plieguePantorrillaMed;
    private Double pliegueBicipal;
    private Double pliegueCrestaIliaca;

    // Perímetros (cm)
    private Double perimetroBrazoRelajado;
    private Double perimetroBrazoFlexTension;
    private Double perimetroCinturaMinima;
    private Double perimetroCaderaMaximo;
    private Double perimetroMusloMedal;
    private Double perimetroPantorrillaMax;

    // Diámetros óseos (cm)
    private Double diametroCodo;
    private Double diametroRodilla;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public MedicionDTO() {}

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public LocalDate getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(LocalDate fechaMedicion) { this.fechaMedicion = fechaMedicion; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getTalla() { return talla; }
    public void setTalla(Double talla) { this.talla = talla; }

    public Double getPliegueTricipal() { return pliegueTricipal; }
    public void setPliegueTricipal(Double v) { this.pliegueTricipal = v; }

    public Double getPliegueSubescapular() { return pliegueSubescapular; }
    public void setPliegueSubescapular(Double v) { this.pliegueSubescapular = v; }

    public Double getPliegueSupraespinal() { return pliegueSupraespinal; }
    public void setPliegueSupraespinal(Double v) { this.pliegueSupraespinal = v; }

    public Double getPliegueAbdominal() { return pliegueAbdominal; }
    public void setPliegueAbdominal(Double v) { this.pliegueAbdominal = v; }

    public Double getPliegueMusloFrontal() { return pliegueMusloFrontal; }
    public void setPliegueMusloFrontal(Double v) { this.pliegueMusloFrontal = v; }

    public Double getPlieguePantorrillaMed() { return plieguePantorrillaMed; }
    public void setPlieguePantorrillaMed(Double v) { this.plieguePantorrillaMed = v; }

    public Double getPliegueBicipal() { return pliegueBicipal; }
    public void setPliegueBicipal(Double v) { this.pliegueBicipal = v; }

    public Double getPliegueCrestaIliaca() { return pliegueCrestaIliaca; }
    public void setPliegueCrestaIliaca(Double v) { this.pliegueCrestaIliaca = v; }

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

    public Double getDiametroCodo() { return diametroCodo; }
    public void setDiametroCodo(Double diametroCodo) { this.diametroCodo = diametroCodo; }

    public Double getDiametroRodilla() { return diametroRodilla; }
    public void setDiametroRodilla(Double diametroRodilla) { this.diametroRodilla = diametroRodilla; }
}
