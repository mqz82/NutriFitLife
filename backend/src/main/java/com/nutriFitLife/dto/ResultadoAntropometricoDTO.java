package com.nutriFitLife.dto;

/**
 * DTO con todos los resultados calculados de una evaluación antropométrica.
 * Este objeto es generado por AntropometriaCalculatorService y devuelto
 * al frontend para mostrar los gráficos y generar el PDF.
 */
public class ResultadoAntropometricoDTO {

    // -------------------------------------------------------------------------
    // Datos del paciente
    // -------------------------------------------------------------------------
    private Long medicionId;
    private Long pacienteId;
    private String nombreCompleto;
    private double edadDecimal;
    private String sexo;
    private String fechaMedicion;

    // -------------------------------------------------------------------------
    // Medidas básicas
    // -------------------------------------------------------------------------
    private double peso;
    private double talla;
    private double imc;
    private String clasificacionIMC;

    // -------------------------------------------------------------------------
    // Fraccionamiento 2 masas (Durnin-Womersley + Siri)
    // -------------------------------------------------------------------------
    private double porcentajeGrasa;
    private double masaGrasaKg;
    private double porcentajeMasaMagra;
    private double masaMagraKg;

    // -------------------------------------------------------------------------
    // Suma de pliegues
    // -------------------------------------------------------------------------
    private double suma6Pliegues;
    private double suma8Pliegues;

    /**
     * Valores de los 8 pliegues en orden para el gráfico de líneas:
     * [tríceps, subescapular, supraespinal, abdominal, muslo, pantorrilla, bíceps, crestailíaca]
     */
    private double[] perfilesDePliegues;
    private String[] etiquetasPliegues;

    // -------------------------------------------------------------------------
    // Ratio Cintura-Cadera
    // -------------------------------------------------------------------------
    private double ratioCinturaCadera;
    private String clasificacionRCC;

    // -------------------------------------------------------------------------
    // Somatotipo Heath-Carter (1967)
    // -------------------------------------------------------------------------
    private double endomorfia;
    private double mesomorfia;
    private double ectomorfia;
    private double xSomatocarta;
    private double ySomatocarta;
    private String clasificacionSomatotipo;

    // -------------------------------------------------------------------------
    // Perímetros para gráfico de barras
    // [brazoRelajado, brazoFlex, cintura, cadera, muslo, pantorrilla]
    // -------------------------------------------------------------------------
    private double[] perimetrosCorregidos;
    private String[] etiquetasPerimetros;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public ResultadoAntropometricoDTO() {}

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Long getMedicionId() { return medicionId; }
    public void setMedicionId(Long medicionId) { this.medicionId = medicionId; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public double getEdadDecimal() { return edadDecimal; }
    public void setEdadDecimal(double edadDecimal) { this.edadDecimal = edadDecimal; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(String fechaMedicion) { this.fechaMedicion = fechaMedicion; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getTalla() { return talla; }
    public void setTalla(double talla) { this.talla = talla; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public String getClasificacionIMC() { return clasificacionIMC; }
    public void setClasificacionIMC(String clasificacionIMC) { this.clasificacionIMC = clasificacionIMC; }

    public double getPorcentajeGrasa() { return porcentajeGrasa; }
    public void setPorcentajeGrasa(double porcentajeGrasa) { this.porcentajeGrasa = porcentajeGrasa; }

    public double getMasaGrasaKg() { return masaGrasaKg; }
    public void setMasaGrasaKg(double masaGrasaKg) { this.masaGrasaKg = masaGrasaKg; }

    public double getPorcentajeMasaMagra() { return porcentajeMasaMagra; }
    public void setPorcentajeMasaMagra(double porcentajeMasaMagra) { this.porcentajeMasaMagra = porcentajeMasaMagra; }

    public double getMasaMagraKg() { return masaMagraKg; }
    public void setMasaMagraKg(double masaMagraKg) { this.masaMagraKg = masaMagraKg; }

    public double getSuma6Pliegues() { return suma6Pliegues; }
    public void setSuma6Pliegues(double suma6Pliegues) { this.suma6Pliegues = suma6Pliegues; }

    public double getSuma8Pliegues() { return suma8Pliegues; }
    public void setSuma8Pliegues(double suma8Pliegues) { this.suma8Pliegues = suma8Pliegues; }

    public double[] getPerfilesDePliegues() { return perfilesDePliegues; }
    public void setPerfilesDePliegues(double[] perfilesDePliegues) { this.perfilesDePliegues = perfilesDePliegues; }

    public String[] getEtiquetasPliegues() { return etiquetasPliegues; }
    public void setEtiquetasPliegues(String[] etiquetasPliegues) { this.etiquetasPliegues = etiquetasPliegues; }

    public double getRatioCinturaCadera() { return ratioCinturaCadera; }
    public void setRatioCinturaCadera(double ratioCinturaCadera) { this.ratioCinturaCadera = ratioCinturaCadera; }

    public String getClasificacionRCC() { return clasificacionRCC; }
    public void setClasificacionRCC(String clasificacionRCC) { this.clasificacionRCC = clasificacionRCC; }

    public double getEndomorfia() { return endomorfia; }
    public void setEndomorfia(double endomorfia) { this.endomorfia = endomorfia; }

    public double getMesomorfia() { return mesomorfia; }
    public void setMesomorfia(double mesomorfia) { this.mesomorfia = mesomorfia; }

    public double getEctomorfia() { return ectomorfia; }
    public void setEctomorfia(double ectomorfia) { this.ectomorfia = ectomorfia; }

    public double getXSomatocarta() { return xSomatocarta; }
    public void setXSomatocarta(double xSomatocarta) { this.xSomatocarta = xSomatocarta; }

    public double getYSomatocarta() { return ySomatocarta; }
    public void setYSomatocarta(double ySomatocarta) { this.ySomatocarta = ySomatocarta; }

    public String getClasificacionSomatotipo() { return clasificacionSomatotipo; }
    public void setClasificacionSomatotipo(String clasificacionSomatotipo) { this.clasificacionSomatotipo = clasificacionSomatotipo; }

    public double[] getPerimetrosCorregidos() { return perimetrosCorregidos; }
    public void setPerimetrosCorregidos(double[] perimetrosCorregidos) { this.perimetrosCorregidos = perimetrosCorregidos; }

    public String[] getEtiquetasPerimetros() { return etiquetasPerimetros; }
    public void setEtiquetasPerimetros(String[] etiquetasPerimetros) { this.etiquetasPerimetros = etiquetasPerimetros; }
}
