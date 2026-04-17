/**
 * Modelo con todos los resultados calculados de una evaluación antropométrica.
 * Mapea directamente el ResultadoAntropometricoDTO del backend.
 */
export interface ResultadoAntropometrico {
  medicionId:                number;
  pacienteId:                number;
  nombreCompleto:            string;
  edadDecimal:               number;
  sexo:                      string;
  fechaMedicion:             string;

  // Básicos
  peso:                      number;
  talla:                     number;
  imc:                       number;
  clasificacionIMC:          string;

  // Fraccionamiento 2 masas
  porcentajeGrasa:           number;
  masaGrasaKg:               number;
  porcentajeMasaMagra:       number;
  masaMagraKg:               number;

  // Pliegues
  suma6Pliegues:             number;
  suma8Pliegues:             number;
  perfilesDePliegues:        number[];
  etiquetasPliegues:         string[];

  // Ratio Cintura-Cadera
  ratioCinturaCadera:        number;
  clasificacionRCC:          string;

  // Somatotipo Heath-Carter
  endomorfia:                number;
  mesomorfia:                number;
  ectomorfia:                number;
  xSomatocarta:              number;
  ySomatocarta:              number;
  clasificacionSomatotipo:   string;

  // Perímetros para gráfico
  perimetrosCorregidos:      number[];
  etiquetasPerimetros:       string[];
}
