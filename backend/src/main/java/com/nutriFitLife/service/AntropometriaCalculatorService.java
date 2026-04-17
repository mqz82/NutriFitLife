package com.nutriFitLife.service;

import com.nutriFitLife.model.Medicion;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Servicio de cálculos antropométricos.
 * Implementa las fórmulas estándar para evaluación de composición corporal
 * y somatotipo utilizadas en nutrición deportiva y clínica.
 *
 * Referencias bibliográficas:
 *   - Durnin & Womersley (1974) — Densidad corporal / fraccionamiento en 2 masas
 *   - Siri (1956) — Ecuación para % grasa a partir de densidad corporal
 *   - Heath & Carter (1967) — Somatotipo tridimensional
 *   - OMS — Clasificaciones IMC y ratio cintura-cadera
 */
@Service
public class AntropometriaCalculatorService {

    // =========================================================================
    // 1. IMC (Índice de Masa Corporal)
    // =========================================================================

    /**
     * Calcula el IMC = peso(kg) / talla(m)².
     * Referencia: OMS.
     *
     * @param pesoKg peso en kilogramos
     * @param tallaCm talla en centímetros
     * @return IMC en kg/m²
     */
    public double calcularIMC(double pesoKg, double tallaCm) {
        double tallaM = tallaCm / 100.0;
        return pesoKg / (tallaM * tallaM);
    }

    /**
     * Clasifica el IMC según los rangos de la OMS.
     *
     * @param imc valor de IMC en kg/m²
     * @return cadena con la categoría
     */
    public String clasificarIMC(double imc) {
        if (imc < 18.5)  return "Bajo peso";
        if (imc < 25.0)  return "Normal";
        if (imc < 30.0)  return "Sobrepeso";
        return "Obesidad";
    }

    // =========================================================================
    // 2. Edad decimal
    // =========================================================================

    /**
     * Calcula la edad en años como número decimal para usar en las ecuaciones
     * que requieren precisión (p. ej. Durnin-Womersley).
     *
     * Ejemplo: 43 años y 6 meses = 43.5
     *
     * @param fechaNacimiento fecha de nacimiento del paciente
     * @param fechaMedicion   fecha en que se realizó la evaluación
     * @return edad en años decimales
     */
    public double calcularEdadDecimal(LocalDate fechaNacimiento, LocalDate fechaMedicion) {
        long dias = ChronoUnit.DAYS.between(fechaNacimiento, fechaMedicion);
        // 365.25 promedia los años bisiestos
        return dias / 365.25;
    }

    // =========================================================================
    // 3. Fraccionamiento en 2 masas — Durnin & Womersley (1974) + Siri (1956)
    // =========================================================================

    /**
     * Calcula la densidad corporal usando la suma de 4 pliegues cutáneos
     * (bíceps + tríceps + subescapular + supraespinal/suprailíaco).
     *
     * Referencia: Durnin JV, Womersley J. Body fat assessed from total body
     * density and its estimation from skinfold thickness: measurements on 481
     * men and women aged from 16 to 72 years. Br J Nutr. 1974;32(1):77-97.
     *
     * @param pliegueBiceps      pliegue bicipital en mm
     * @param pliegueTricipal    pliegue tricipital en mm
     * @param pliegueSubescapular pliegue subescapular en mm
     * @param pliegueSupraespinal pliegue supraespinal (suprailíaco) en mm
     * @param edadDecimal        edad en años decimales
     * @param sexo               "M" o "F"
     * @return densidad corporal en g/cm³
     */
    public double calcularDensidadCorporal(
            double pliegueBiceps, double pliegueTricipal,
            double pliegueSubescapular, double pliegueSupraespinal,
            double edadDecimal, String sexo) {

        double suma4 = pliegueBiceps + pliegueTricipal + pliegueSubescapular + pliegueSupraespinal;
        double log10Suma4 = Math.log10(suma4);

        // Obtener coeficientes C y M según sexo y rango etario
        double[] coeficientes = obtenerCoeficientesDurninWomersley(edadDecimal, sexo);
        double C = coeficientes[0];
        double M = coeficientes[1];

        return C - (M * log10Suma4);
    }

    /**
     * Retorna los coeficientes [C, M] de la tabla Durnin-Womersley según
     * sexo y edad.
     */
    private double[] obtenerCoeficientesDurninWomersley(double edad, String sexo) {
        if ("M".equalsIgnoreCase(sexo)) {
            if (edad < 20) return new double[]{1.1620, 0.0630};
            if (edad < 30) return new double[]{1.1631, 0.0632};
            if (edad < 40) return new double[]{1.1422, 0.0544};
            if (edad < 50) return new double[]{1.1620, 0.0700};
            return             new double[]{1.1715, 0.0779};
        } else {
            if (edad < 20) return new double[]{1.1549, 0.0678};
            if (edad < 30) return new double[]{1.1599, 0.0717};
            if (edad < 40) return new double[]{1.1423, 0.0632};
            if (edad < 50) return new double[]{1.1333, 0.0612};
            return             new double[]{1.1339, 0.0645};
        }
    }

    /**
     * Calcula el porcentaje de grasa corporal a partir de la densidad
     * usando la ecuación de Siri (1956).
     *
     * Fórmula: %Grasa = ((4.95 / Densidad) - 4.50) × 100
     *
     * Referencia: Siri WE. Body composition from fluid spaces and density:
     * analysis of methods. 1956.
     *
     * @param densidad densidad corporal en g/cm³
     * @return porcentaje de grasa corporal
     */
    public double calcularPorcentajeGrasa(double densidad) {
        return ((4.95 / densidad) - 4.50) * 100.0;
    }

    /**
     * Método de conveniencia: calcula % grasa directamente desde los pliegues.
     *
     * @param pliegueBiceps      pliegue bicipital en mm
     * @param pliegueTricipal    pliegue tricipital en mm
     * @param pliegueSubescapular pliegue subescapular en mm
     * @param pliegueSupraespinal pliegue supraespinal en mm
     * @param edadDecimal        edad en años decimales
     * @param sexo               "M" o "F"
     * @return porcentaje de grasa corporal
     */
    public double calcularPorcentajeGrasaDesdePliegues(
            double pliegueBiceps, double pliegueTricipal,
            double pliegueSubescapular, double pliegueSupraespinal,
            double edadDecimal, String sexo) {
        double densidad = calcularDensidadCorporal(
                pliegueBiceps, pliegueTricipal, pliegueSubescapular, pliegueSupraespinal,
                edadDecimal, sexo);
        return calcularPorcentajeGrasa(densidad);
    }

    /**
     * Calcula la masa grasa en kilogramos.
     *
     * @param pesoKg           peso total en kg
     * @param porcentajeGrasa  porcentaje de grasa (0–100)
     * @return masa grasa en kg
     */
    public double calcularMasaGrasa(double pesoKg, double porcentajeGrasa) {
        return (porcentajeGrasa / 100.0) * pesoKg;
    }

    /**
     * Calcula la masa magra en kilogramos.
     *
     * @param pesoKg      peso total en kg
     * @param masaGrasaKg masa grasa en kg
     * @return masa magra en kg
     */
    public double calcularMasaMagra(double pesoKg, double masaGrasaKg) {
        return pesoKg - masaGrasaKg;
    }

    // =========================================================================
    // 4. Suma de pliegues
    // =========================================================================

    /**
     * Suma de 6 pliegues: tríceps + subescapular + supraespinal + abdominal +
     * muslo frontal + pantorrilla medial.
     */
    public double calcularSuma6Pliegues(Medicion m) {
        return safe(m.getPliegueTricipal())
             + safe(m.getPliegueSubescapular())
             + safe(m.getPliegueSupraespinal())
             + safe(m.getPliegueAbdominal())
             + safe(m.getPliegueMusloFrontal())
             + safe(m.getPlieguePantorrillaMed());
    }

    /**
     * Suma de 8 pliegues: suma6 + bíceps + cresta ilíaca.
     */
    public double calcularSuma8Pliegues(Medicion m) {
        return calcularSuma6Pliegues(m)
             + safe(m.getPliegueBicipal())
             + safe(m.getPliegueCrestaIliaca());
    }

    // =========================================================================
    // 5. Ratio Cintura-Cadera (RCC)
    // =========================================================================

    /**
     * Calcula el ratio cintura-cadera.
     * RCC = perimetroCintura (cm) / perimetroCadera (cm)
     *
     * Referencia: OMS, 2008. Waist circumference and waist-hip ratio.
     */
    public double calcularRatioCinturaCadera(double cinturaCm, double caderaCm) {
        return cinturaCm / caderaCm;
    }

    /**
     * Clasifica el riesgo metabólico según el RCC y el sexo.
     * OMS: Hombre normal < 0.90, riesgo alto ≥ 0.95
     *      Mujer  normal < 0.85, riesgo alto ≥ 0.90
     */
    public String clasificarRCC(double rcc, String sexo) {
        if ("M".equalsIgnoreCase(sexo)) {
            if (rcc < 0.90) return "Riesgo bajo";
            if (rcc < 0.95) return "Riesgo moderado";
            return "Riesgo alto";
        } else {
            if (rcc < 0.80) return "Riesgo bajo";
            if (rcc < 0.85) return "Riesgo moderado";
            return "Riesgo alto";
        }
    }

    // =========================================================================
    // 6. Somatotipo Heath-Carter (1967)
    // =========================================================================

    /**
     * Calcula la componente ENDOMORFIA del somatotipo.
     *
     * Fórmula Heath & Carter (1967):
     *   X = (tríceps + subescapular + supraespinal) × (170.18 / talla_cm)
     *   Endomorfia = -0.7182 + (0.1451 × X) - (0.00068 × X²) + (0.0000014 × X³)
     *
     * Referencia: Carter JEL, Heath BH. Somatotyping — Development and
     * Applications. Cambridge University Press, 1990.
     *
     * @param pliegueTricipal    mm
     * @param pliegueSubescapular mm
     * @param pliegueSupraespinal mm
     * @param tallaCm            talla en centímetros
     * @return componente endomorfia
     */
    public double calcularEndomorfia(double pliegueTricipal, double pliegueSubescapular,
                                     double pliegueSupraespinal, double tallaCm) {
        double X = (pliegueTricipal + pliegueSubescapular + pliegueSupraespinal)
                   * (170.18 / tallaCm);
        return -0.7182
             + (0.1451   * X)
             - (0.00068  * X * X)
             + (0.0000014 * X * X * X);
    }

    /**
     * Calcula la componente MESOMORFIA del somatotipo.
     *
     * Fórmula Heath & Carter (1967):
     *   BrazoCorregido  = perBrazoFlex  - (pliegueTricipal / 10)
     *   PiernaCorregida = perPantorrilla - (plieguePantorrilla / 10)
     *   Mesomorfia = 0.858×diametroCodo + 0.601×diametroRodilla
     *              + 0.188×BrazoCorregido + 0.161×PiernaCorregida
     *              - 0.131×talla + 4.50
     *
     * @param perBrazoFlexCm       perímetro brazo flexionado en tensión (cm)
     * @param perPantorrillaCm     perímetro pantorrilla máximo (cm)
     * @param pliegueTricipalMm    pliegue tricipital (mm)
     * @param plieguePantorrillaMm pliegue pantorrilla medial (mm)
     * @param diametroCodoCm       diámetro biepicondilar codo (cm)
     * @param diametroRodillaCm    diámetro bicondilar rodilla (cm)
     * @param tallaCm              talla en centímetros
     * @return componente mesomorfia
     */
    public double calcularMesomorfia(double perBrazoFlexCm, double perPantorrillaCm,
                                     double pliegueTricipalMm, double plieguePantorrillaMm,
                                     double diametroCodoCm, double diametroRodillaCm,
                                     double tallaCm) {
        double brazoCorregido  = perBrazoFlexCm  - (pliegueTricipalMm  / 10.0);
        double piernaCorregida = perPantorrillaCm - (plieguePantorrillaMm / 10.0);

        return (0.858 * diametroCodoCm)
             + (0.601 * diametroRodillaCm)
             + (0.188 * brazoCorregido)
             + (0.161 * piernaCorregida)
             - (0.131 * tallaCm)
             + 4.50;
    }

    /**
     * Calcula la componente ECTOMORFIA del somatotipo.
     *
     * Fórmula Heath & Carter (1967) usando el Height-Weight Ratio (HWR):
     *   HWR = talla_cm / (peso_kg ^ (1/3))
     *
     *   Si HWR ≥ 40.75: Ecto = (0.732 × HWR) - 28.58
     *   Si HWR ≥ 38.25: Ecto = (0.463 × HWR) - 17.63
     *   Si HWR <  38.25: Ecto = 0.1   (valor mínimo)
     *
     * @param pesoKg  peso en kilogramos
     * @param tallaCm talla en centímetros
     * @return componente ectomorfia
     */
    public double calcularEctomorfia(double pesoKg, double tallaCm) {
        double hwr = tallaCm / Math.pow(pesoKg, 1.0 / 3.0);

        if (hwr >= 40.75) return (0.732 * hwr) - 28.58;
        if (hwr >= 38.25) return (0.463 * hwr) - 17.63;
        return 0.1;
    }

    /**
     * Calcula la coordenada X de la somatocarta.
     * X = Ectomorfia - Endomorfia
     */
    public double calcularXSomatocarta(double ectomorfia, double endomorfia) {
        return ectomorfia - endomorfia;
    }

    /**
     * Calcula la coordenada Y de la somatocarta.
     * Y = (2 × Mesomorfia) - (Endomorfia + Ectomorfia)
     */
    public double calcularYSomatocarta(double mesomorfia, double endomorfia, double ectomorfia) {
        return (2.0 * mesomorfia) - (endomorfia + ectomorfia);
    }

    /**
     * Clasifica el tipo de somatotipo (nombre descriptivo) a partir de las
     * tres componentes del modelo Heath-Carter.
     */
    public String clasificarSomatotipo(double endomorfia, double mesomorfia, double ectomorfia) {
        boolean endoDom = endomorfia > mesomorfia && endomorfia > ectomorfia;
        boolean mesoDom = mesomorfia > endomorfia && mesomorfia > ectomorfia;
        boolean ectoDom = ectomorfia > endomorfia && ectomorfia > mesomorfia;

        double diff = 0.5;

        if (endoDom && mesomorfia >= endomorfia - diff) return "Endomorfo mesomorfo";
        if (endoDom && ectomorfia >= endomorfia - diff) return "Endomorfo ectomorfo";
        if (endoDom) return "Endomorfo puro";

        if (mesoDom && endomorfia >= mesomorfia - diff) return "Mesomorfo endomorfo";
        if (mesoDom && ectomorfia >= mesomorfia - diff) return "Mesomorfo ectomorfo";
        if (mesoDom) return "Mesomorfo puro";

        if (ectoDom && endomorfia >= ectomorfia - diff) return "Ectomorfo endomorfo";
        if (ectoDom && mesomorfia >= ectomorfia - diff) return "Ectomorfo mesomorfo";
        if (ectoDom) return "Ectomorfo puro";

        return "Central";
    }

    // =========================================================================
    // Método principal: calcula TODOS los indicadores de una medición
    // =========================================================================

    /**
     * Calcula todos los indicadores antropométricos de una medición y los
     * retorna listos para ser enviados al frontend y generar el PDF.
     */
    public com.nutriFitLife.dto.ResultadoAntropometricoDTO calcularTodo(
            Medicion m, com.nutriFitLife.model.Paciente p) {

        com.nutriFitLife.dto.ResultadoAntropometricoDTO r =
                new com.nutriFitLife.dto.ResultadoAntropometricoDTO();

        // Datos de identificación
        r.setMedicionId(m.getId());
        r.setPacienteId(p.getId());
        r.setNombreCompleto(p.getNombreCompleto());
        r.setSexo(p.getSexo());
        r.setFechaMedicion(m.getFechaMedicion().toString());

        // Edad decimal
        double edad = calcularEdadDecimal(p.getFechaNacimiento(), m.getFechaMedicion());
        r.setEdadDecimal(round2(edad));

        // Básicos
        r.setPeso(m.getPeso());
        r.setTalla(m.getTalla());
        double imc = calcularIMC(m.getPeso(), m.getTalla());
        r.setImc(round2(imc));
        r.setClasificacionIMC(clasificarIMC(imc));

        // Fraccionamiento 2 masas
        double densidad = calcularDensidadCorporal(
                safe(m.getPliegueBicipal()), safe(m.getPliegueTricipal()),
                safe(m.getPliegueSubescapular()), safe(m.getPliegueSupraespinal()),
                edad, p.getSexo());
        double pctGrasa = calcularPorcentajeGrasa(densidad);
        double masaGrasa = calcularMasaGrasa(m.getPeso(), pctGrasa);
        double masaMagra = calcularMasaMagra(m.getPeso(), masaGrasa);

        r.setPorcentajeGrasa(round2(pctGrasa));
        r.setMasaGrasaKg(round2(masaGrasa));
        r.setPorcentajeMasaMagra(round2(100.0 - pctGrasa));
        r.setMasaMagraKg(round2(masaMagra));

        // Pliegues
        r.setSuma6Pliegues(round2(calcularSuma6Pliegues(m)));
        r.setSuma8Pliegues(round2(calcularSuma8Pliegues(m)));
        r.setPerfilesDePliegues(new double[]{
            safe(m.getPliegueTricipal()), safe(m.getPliegueSubescapular()),
            safe(m.getPliegueSupraespinal()), safe(m.getPliegueAbdominal()),
            safe(m.getPliegueMusloFrontal()), safe(m.getPlieguePantorrillaMed()),
            safe(m.getPliegueBicipal()), safe(m.getPliegueCrestaIliaca())
        });
        r.setEtiquetasPliegues(new String[]{
            "Tríceps", "Subescapular", "Supraespinal", "Abdominal",
            "Muslo", "Pantorrilla", "Bíceps", "Cresta ilíaca"
        });

        // RCC
        double rcc = calcularRatioCinturaCadera(
                safe(m.getPerimetroCinturaMinima()), safe(m.getPerimetroCaderaMaximo()));
        r.setRatioCinturaCadera(round2(rcc));
        r.setClasificacionRCC(clasificarRCC(rcc, p.getSexo()));

        // Somatotipo
        double endo = calcularEndomorfia(
                safe(m.getPliegueTricipal()), safe(m.getPliegueSubescapular()),
                safe(m.getPliegueSupraespinal()), m.getTalla());
        double meso = calcularMesomorfia(
                safe(m.getPerimetroBrazoFlexTension()), safe(m.getPerimetroPantorrillaMax()),
                safe(m.getPliegueTricipal()), safe(m.getPlieguePantorrillaMed()),
                safe(m.getDiametroCodo()), safe(m.getDiametroRodilla()),
                m.getTalla());
        double ecto = calcularEctomorfia(m.getPeso(), m.getTalla());

        r.setEndomorfia(round2(endo));
        r.setMesomorfia(round2(meso));
        r.setEctomorfia(round2(ecto));
        r.setXSomatocarta(round2(calcularXSomatocarta(ecto, endo)));
        r.setYSomatocarta(round2(calcularYSomatocarta(meso, endo, ecto)));
        r.setClasificacionSomatotipo(clasificarSomatotipo(endo, meso, ecto));

        // Perímetros para gráfico de barras
        r.setPerimetrosCorregidos(new double[]{
            safe(m.getPerimetroBrazoRelajado()),
            safe(m.getPerimetroBrazoFlexTension()),
            safe(m.getPerimetroCinturaMinima()),
            safe(m.getPerimetroCaderaMaximo()),
            safe(m.getPerimetroMusloMedal()),
            safe(m.getPerimetroPantorrillaMax())
        });
        r.setEtiquetasPerimetros(new String[]{
            "Brazo relajado", "Brazo flex.", "Cintura",
            "Cadera", "Muslo", "Pantorrilla"
        });

        return r;
    }

    // -------------------------------------------------------------------------
    // Utilidades internas
    // -------------------------------------------------------------------------

    /** Convierte null a 0.0 para evitar NullPointerException en los cálculos */
    private double safe(Double valor) {
        return valor == null ? 0.0 : valor;
    }

    /** Redondea a 2 decimales */
    private double round2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
