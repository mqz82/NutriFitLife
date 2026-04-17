/**
 * Modelo que representa un paciente del sistema.
 * Mapea directamente el PacienteDTO del backend.
 */
export interface Paciente {
  id?:               number;
  nombres:           string;
  apellidos:         string;
  rut:               string;
  fechaNacimiento:   string;   // ISO 8601: YYYY-MM-DD
  sexo:              'M' | 'F';
  email?:            string;
  telefono?:         string;
  creadoEn?:         string;
}
