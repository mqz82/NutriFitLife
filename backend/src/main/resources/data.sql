-- =====================================================================
-- NutriFitLife — Datos de prueba iniciales
-- Se ejecuta automáticamente al arrancar con H2 create-drop
-- =====================================================================

-- PACIENTE 1: Sergio Marquez (datos del informe de referencia)
-- Fecha nac: 20/02/1982 → en la medición de 11/08/2025 tiene ~43.5 años
INSERT INTO pacientes (nombres, apellidos, rut, fecha_nacimiento, sexo, email, telefono, creado_en)
VALUES ('Sergio', 'Marquez', '12.345.678-9', '1982-02-20', 'M',
        'sergio.marquez@email.com', '+56912345678', CURRENT_TIMESTAMP);

-- PACIENTE 2: Ana González
INSERT INTO pacientes (nombres, apellidos, rut, fecha_nacimiento, sexo, email, telefono, creado_en)
VALUES ('Ana', 'González', '15.678.901-2', '1990-07-15', 'F',
        'ana.gonzalez@email.com', '+56987654321', CURRENT_TIMESTAMP);

-- PACIENTE 3: Carlos Rojas
INSERT INTO pacientes (nombres, apellidos, rut, fecha_nacimiento, sexo, email, telefono, creado_en)
VALUES ('Carlos', 'Rojas', '18.234.567-K', '1975-11-03', 'M',
        'carlos.rojas@email.com', '+56911223344', CURRENT_TIMESTAMP);

-- =====================================================================
-- MEDICIONES DE SERGIO MARQUEZ (paciente_id = 1)
-- =====================================================================

-- Medición 1: 11/08/2025 — datos del informe de referencia
INSERT INTO mediciones (
    paciente_id, fecha_medicion,
    peso, talla,
    pliegue_tricipal, pliegue_subescapular, pliegue_supraespinal, pliegue_abdominal,
    pliegue_muslo_frontal, pliegue_pantorrilla_med, pliegue_bicipal, pliegue_cresta_iliaca,
    perimetro_brazo_relajado, perimetro_brazo_flex_tension, perimetro_cintura_minima,
    perimetro_cadera_maximo, perimetro_muslo_medal, perimetro_pantorrilla_max,
    diametro_codo, diametro_rodilla
) VALUES (
    1, '2025-08-11',
    78.0, 178.0,
    21.0, 20.0, 21.0, 24.0,
    21.0, 6.0, 12.0, 22.0,
    29.6, 30.2, 77.0,
    99.5, 45.4, 37.5,
    7.0, 9.5
);

-- Medición 2: 01/03/2025 — medición anterior de Sergio
INSERT INTO mediciones (
    paciente_id, fecha_medicion,
    peso, talla,
    pliegue_tricipal, pliegue_subescapular, pliegue_supraespinal, pliegue_abdominal,
    pliegue_muslo_frontal, pliegue_pantorrilla_med, pliegue_bicipal, pliegue_cresta_iliaca,
    perimetro_brazo_relajado, perimetro_brazo_flex_tension, perimetro_cintura_minima,
    perimetro_cadera_maximo, perimetro_muslo_medal, perimetro_pantorrilla_max,
    diametro_codo, diametro_rodilla
) VALUES (
    1, '2025-03-01',
    80.0, 178.0,
    23.0, 22.0, 23.0, 26.0,
    23.0, 7.0, 13.0, 24.0,
    30.0, 30.5, 79.0,
    100.0, 46.0, 37.8,
    7.0, 9.5
);

-- =====================================================================
-- MEDICIONES DE ANA GONZÁLEZ (paciente_id = 2)
-- =====================================================================

INSERT INTO mediciones (
    paciente_id, fecha_medicion,
    peso, talla,
    pliegue_tricipal, pliegue_subescapular, pliegue_supraespinal, pliegue_abdominal,
    pliegue_muslo_frontal, pliegue_pantorrilla_med, pliegue_bicipal, pliegue_cresta_iliaca,
    perimetro_brazo_relajado, perimetro_brazo_flex_tension, perimetro_cintura_minima,
    perimetro_cadera_maximo, perimetro_muslo_medal, perimetro_pantorrilla_max,
    diametro_codo, diametro_rodilla
) VALUES (
    2, '2025-08-05',
    62.0, 165.0,
    18.0, 14.0, 15.0, 20.0,
    28.0, 12.0, 9.0, 16.0,
    26.5, 27.0, 68.0,
    94.0, 52.0, 35.0,
    5.8, 8.5
);

INSERT INTO mediciones (
    paciente_id, fecha_medicion,
    peso, talla,
    pliegue_tricipal, pliegue_subescapular, pliegue_supraespinal, pliegue_abdominal,
    pliegue_muslo_frontal, pliegue_pantorrilla_med, pliegue_bicipal, pliegue_cresta_iliaca,
    perimetro_brazo_relajado, perimetro_brazo_flex_tension, perimetro_cintura_minima,
    perimetro_cadera_maximo, perimetro_muslo_medal, perimetro_pantorrilla_max,
    diametro_codo, diametro_rodilla
) VALUES (
    2, '2025-02-10',
    64.0, 165.0,
    20.0, 16.0, 17.0, 22.0,
    30.0, 13.0, 10.0, 18.0,
    27.0, 27.5, 70.0,
    95.5, 53.0, 35.5,
    5.8, 8.5
);

-- =====================================================================
-- MEDICIONES DE CARLOS ROJAS (paciente_id = 3)
-- =====================================================================

INSERT INTO mediciones (
    paciente_id, fecha_medicion,
    peso, talla,
    pliegue_tricipal, pliegue_subescapular, pliegue_supraespinal, pliegue_abdominal,
    pliegue_muslo_frontal, pliegue_pantorrilla_med, pliegue_bicipal, pliegue_cresta_iliaca,
    perimetro_brazo_relajado, perimetro_brazo_flex_tension, perimetro_cintura_minima,
    perimetro_cadera_maximo, perimetro_muslo_medal, perimetro_pantorrilla_max,
    diametro_codo, diametro_rodilla
) VALUES (
    3, '2025-07-20',
    90.0, 175.0,
    28.0, 25.0, 26.0, 32.0,
    24.0, 10.0, 16.0, 28.0,
    33.0, 33.5, 92.0,
    102.0, 54.0, 39.0,
    7.5, 10.0
);

INSERT INTO mediciones (
    paciente_id, fecha_medicion,
    peso, talla,
    pliegue_tricipal, pliegue_subescapular, pliegue_supraespinal, pliegue_abdominal,
    pliegue_muslo_frontal, pliegue_pantorrilla_med, pliegue_bicipal, pliegue_cresta_iliaca,
    perimetro_brazo_relajado, perimetro_brazo_flex_tension, perimetro_cintura_minima,
    perimetro_cadera_maximo, perimetro_muslo_medal, perimetro_pantorrilla_max,
    diametro_codo, diametro_rodilla
) VALUES (
    3, '2025-01-15',
    93.0, 175.0,
    30.0, 27.0, 28.0, 35.0,
    26.0, 11.0, 18.0, 30.0,
    34.0, 34.5, 95.0,
    103.5, 55.0, 39.5,
    7.5, 10.0
);
