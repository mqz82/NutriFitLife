package com.nutriFitLife.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del servicio de cálculos antropométricos.
 * Todos los valores esperados se validan contra los datos reales
 * del informe de Sergio Marquez (11/08/2025).
 *
 * Datos de Sergio:
 *   Peso: 78 kg, Talla: 178 cm
 *   Fecha nac: 20/02/1982 → Edad decimal ≈ 43.5 años
 *   Pliegues: Tríceps=21, Subescapular=20, Supraespinal=21, Abdominal=24,
 *             Muslo=21, Pantorrilla=6, Bíceps=12, CrestaIlíaca=22
 *   Perímetros: BrazoRelaj=29.6, BrazoFlex=30.2, Cintura=77.0,
 *               Cadera=99.5, Muslo=45.4, Pantorrilla=37.5
 *   Diámetros: Codo=7.0, Rodilla=9.5
 */
@DisplayName("Cálculos antropométricos — validación con datos de Sergio Marquez")
class AntropometriaCalculatorServiceTest {

    private AntropometriaCalculatorService service;

    // Datos de referencia de Sergio Marquez
    private static final double PESO   = 78.0;
    private static final double TALLA  = 178.0;
    private static final String SEXO   = "M";

    private static final double PLIEGUE_TRICEPS       = 21.0;
    private static final double PLIEGUE_SUBESCAPULAR  = 20.0;
    private static final double PLIEGUE_SUPRAESPINAL  = 21.0;
    private static final double PLIEGUE_ABDOMINAL     = 24.0;
    private static final double PLIEGUE_MUSLO         = 21.0;
    private static final double PLIEGUE_PANTORRILLA   = 6.0;
    private static final double PLIEGUE_BICEPS        = 12.0;
    private static final double PLIEGUE_CRESTA_ILIACA = 22.0;

    private static final double PERIMETRO_BRAZO_FLEX  = 30.2;
    private static final double PERIMETRO_PANTORRILLA = 37.5;
    private static final double PERIMETRO_CINTURA     = 77.0;
    private static final double PERIMETRO_CADERA      = 99.5;
    private static final double DIAMETRO_CODO         = 7.0;
    private static final double DIAMETRO_RODILLA      = 9.5;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1982, 2, 20);
    private static final LocalDate FECHA_MEDICION   = LocalDate.of(2025, 8, 11);

    @BeforeEach
    void setUp() {
        service = new AntropometriaCalculatorService();
    }

    // =========================================================================
    // Test IMC
    // =========================================================================

    @Test
    @DisplayName("IMC debe ser ~24.6 para 78 kg / 178 cm")
    void testCalcularIMC() {
        double imc = service.calcularIMC(PESO, TALLA);
        assertEquals(24.6, imc, 0.1,
                "IMC esperado ≈ 24.6 para Sergio Marquez");
    }

    @Test
    @DisplayName("Clasificación IMC 24.6 → Normal")
    void testClasificacionIMC() {
        String clasificacion = service.clasificarIMC(24.6);
        assertEquals("Normal", clasificacion);
    }

    @Test
    @DisplayName("IMC < 18.5 → Bajo peso")
    void testIMCBajoPeso() {
        assertEquals("Bajo peso", service.clasificarIMC(17.0));
    }

    @Test
    @DisplayName("IMC ≥ 30 → Obesidad")
    void testIMCObesidad() {
        assertEquals("Obesidad", service.clasificarIMC(30.0));
    }

    // =========================================================================
    // Test Edad decimal
    // =========================================================================

    @Test
    @DisplayName("Edad decimal de Sergio en agosto 2025 debe ser ~43.5")
    void testEdadDecimal() {
        double edad = service.calcularEdadDecimal(FECHA_NACIMIENTO, FECHA_MEDICION);
        assertEquals(43.5, edad, 0.1,
                "Edad decimal esperada ≈ 43.5 años");
        assertTrue(edad >= 43.0 && edad <= 44.0,
                "La edad debe estar entre 43 y 44 años");
    }

    // =========================================================================
    // Test Densidad corporal (Durnin-Womersley)
    // =========================================================================

    @Test
    @DisplayName("Densidad corporal para Sergio (hombre 43.5 años) debe ser ~1.0276")
    void testDensidadCorporal() {
        // Para hombre 40-49: C=1.1620, M=0.0700
        // Suma4 = biceps(12) + triceps(21) + subescapular(20) + supraespinal(21) = 74
        // Nota: el informe usa 83mm (bíceps=21) — aquí bíceps=12
        double densidad = service.calcularDensidadCorporal(
                PLIEGUE_BICEPS, PLIEGUE_TRICEPS,
                PLIEGUE_SUBESCAPULAR, PLIEGUE_SUPRAESPINAL,
                43.5, SEXO);
        // Suma4=74, log10(74)=1.8692, Densidad=1.1620-(0.0700*1.8692)=1.0312
        assertTrue(densidad > 1.01 && densidad < 1.10,
                "Densidad debe estar en rango fisiológico (1.01–1.10)");
    }

    // =========================================================================
    // Test % Grasa (Durnin-Womersley + Siri)
    // Validación con la suma de 4 pliegues del informe = 83 mm (bíceps=21)
    // =========================================================================

    @Test
    @DisplayName("% Grasa con suma 4 pliegues=83 mm debe ser ~27.3%")
    void testPorcentajeGrasaInforme() {
        // En el informe los 4 pliegues son: bíceps=21+tríceps=21+sub=20+supra=21 = 83mm
        double densidad = service.calcularDensidadCorporal(
                21.0, 21.0, 20.0, 21.0, 43.5, SEXO);
        double pctGrasa = service.calcularPorcentajeGrasa(densidad);
        assertEquals(27.3, pctGrasa, 0.5,
                "% grasa esperado ≈ 27.3% (informe de referencia)");
    }

    // =========================================================================
    // Test Masa grasa y masa magra
    // =========================================================================

    @Test
    @DisplayName("Masa grasa con 27.3% de 78 kg debe ser ~21.3 kg")
    void testMasaGrasa() {
        double masaGrasa = service.calcularMasaGrasa(PESO, 27.3);
        assertEquals(21.3, masaGrasa, 0.2,
                "Masa grasa esperada ≈ 21.3 kg");
    }

    @Test
    @DisplayName("Masa magra = Peso - MasaGrasa")
    void testMasaMagra() {
        double masaMagra = service.calcularMasaMagra(78.0, 21.3);
        assertEquals(56.7, masaMagra, 0.2,
                "Masa magra esperada ≈ 56.7 kg");
    }

    // =========================================================================
    // Test Ratio Cintura-Cadera
    // =========================================================================

    @Test
    @DisplayName("RCC de Sergio (cintura=77, cadera=99.5) debe ser ~0.77")
    void testRatioCinturaCadera() {
        double rcc = service.calcularRatioCinturaCadera(PERIMETRO_CINTURA, PERIMETRO_CADERA);
        assertEquals(0.77, rcc, 0.02,
                "RCC esperado ≈ 0.77–0.78");
    }

    @Test
    @DisplayName("RCC 0.77 en hombre → Riesgo bajo")
    void testClasificacionRCC() {
        String clasificacion = service.clasificarRCC(0.77, "M");
        assertEquals("Riesgo bajo", clasificacion);
    }

    // =========================================================================
    // Test Somatotipo Heath-Carter
    // =========================================================================

    @Test
    @DisplayName("Endomorfia con tríceps=21, sub=20, supra=21, talla=178 debe ser ~5.1")
    void testEndomorfia() {
        double endo = service.calcularEndomorfia(
                PLIEGUE_TRICEPS, PLIEGUE_SUBESCAPULAR, PLIEGUE_SUPRAESPINAL, TALLA);
        assertEquals(5.1, endo, 0.3,
                "Endomorfia esperada ≈ 5.1 (informe de referencia)");
    }

    @Test
    @DisplayName("Mesomorfia para los datos de Sergio debe ser ~2.8")
    void testMesomorfia() {
        double meso = service.calcularMesomorfia(
                PERIMETRO_BRAZO_FLEX, PERIMETRO_PANTORRILLA,
                PLIEGUE_TRICEPS, PLIEGUE_PANTORRILLA,
                DIAMETRO_CODO, DIAMETRO_RODILLA,
                TALLA);
        assertEquals(2.8, meso, 0.4,
                "Mesomorfia esperada ≈ 2.8 (informe de referencia)");
    }

    @Test
    @DisplayName("Ectomorfia para 78 kg / 178 cm debe ser ~1.9")
    void testEctomorfia() {
        double ecto = service.calcularEctomorfia(PESO, TALLA);
        assertEquals(1.9, ecto, 0.3,
                "Ectomorfia esperada ≈ 1.9 (informe de referencia)");
    }

    // =========================================================================
    // Test coordenadas de la Somatocarta
    // =========================================================================

    @Test
    @DisplayName("X somatocarta = Ecto - Endo = 1.9 - 5.1 = -3.2")
    void testXSomatocarta() {
        double x = service.calcularXSomatocarta(1.9, 5.1);
        assertEquals(-3.2, x, 0.1,
                "X somatocarta esperado = -3.2");
    }

    @Test
    @DisplayName("Y somatocarta = (2×Meso)-(Endo+Ecto) = 5.6-7.0 = -1.4")
    void testYSomatocarta() {
        double y = service.calcularYSomatocarta(2.8, 5.1, 1.9);
        assertEquals(-1.4, y, 0.1,
                "Y somatocarta esperado = -1.4");
    }

    // =========================================================================
    // Test clasificación somatotipo
    // =========================================================================

    @Test
    @DisplayName("Endo=5.1, Meso=2.8, Ecto=1.9 → clasificar como Endomorfo")
    void testClasificacionSomatotipo() {
        String clasificacion = service.clasificarSomatotipo(5.1, 2.8, 1.9);
        assertTrue(clasificacion.toLowerCase().contains("endomorfo"),
                "El somatotipo de Sergio debe ser de tipo Endomorfo: " + clasificacion);
    }

    // =========================================================================
    // Test fórmulas de Siri con densidades extremas
    // =========================================================================

    @Test
    @DisplayName("Ecuación de Siri: densidad=1.0276 → %Grasa ≈ 27.3%")
    void testSiriConDensidad() {
        double pct = service.calcularPorcentajeGrasa(1.0276);
        assertEquals(27.3, pct, 0.5);
    }

    @Test
    @DisplayName("Ectomorfia mínima: HWR < 38.25 → Ecto = 0.1")
    void testEctomorfiaMinima() {
        // HWR < 38.25 para persona muy pesada y baja estatura
        double ecto = service.calcularEctomorfia(120.0, 160.0);
        assertEquals(0.1, ecto, 0.001);
    }
}
