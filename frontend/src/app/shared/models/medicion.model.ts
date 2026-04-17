/**
 * Modelo que representa una medición antropométrica.
 * Mapea directamente el MedicionDTO del backend.
 */
export interface Medicion {
  id?:                        number;
  pacienteId:                 number;
  fechaMedicion:              string;   // ISO 8601: YYYY-MM-DD

  // Básicos
  peso:                       number;   // kg
  talla:                      number;   // cm

  // Pliegues cutáneos (mm) — Protocolo ISAK 8 pliegues
  pliegueTricipal?:           number;
  pliegueSubescapular?:       number;
  pliegueSupraespinal?:       number;
  pliegueAbdominal?:          number;
  pliegueMusloFrontal?:       number;
  plieguePantorrillaMed?:     number;
  pliegueBicipal?:            number;
  pliegueCrestaIliaca?:       number;

  // Perímetros (cm)
  perimetroBrazoRelajado?:    number;
  perimetroBrazoFlexTension?: number;
  perimetroCinturaMinima?:    number;
  perimetroCaderaMaximo?:     number;
  perimetroMusloMedal?:       number;
  perimetroPantorrillaMax?:   number;

  // Diámetros óseos (cm)
  diametroCodo?:              number;
  diametroRodilla?:           number;
}
