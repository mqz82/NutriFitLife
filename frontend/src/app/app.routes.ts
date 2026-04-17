import { Routes } from '@angular/router';

/**
 * Definición de rutas de la aplicación.
 * Usa lazy loading para cargar cada pantalla solo cuando se necesita.
 */
export const routes: Routes = [
  // Redirige la raíz a la lista de pacientes
  { path: '', redirectTo: 'pacientes', pathMatch: 'full' },

  // Lista de pacientes
  {
    path: 'pacientes',
    loadComponent: () =>
      import('./features/pacientes/lista-pacientes/lista-pacientes.component')
        .then(m => m.ListaPacientesComponent)
  },

  // Crear nuevo paciente
  {
    path: 'pacientes/nuevo',
    loadComponent: () =>
      import('./features/pacientes/form-paciente/form-paciente.component')
        .then(m => m.FormPacienteComponent)
  },

  // Editar paciente existente
  {
    path: 'pacientes/:id/editar',
    loadComponent: () =>
      import('./features/pacientes/form-paciente/form-paciente.component')
        .then(m => m.FormPacienteComponent)
  },

  // Detalle del paciente con historial de mediciones
  {
    path: 'pacientes/:id',
    loadComponent: () =>
      import('./features/pacientes/detalle-paciente/detalle-paciente.component')
        .then(m => m.DetallePacienteComponent)
  },

  // Nueva medición para un paciente (recibe ?pacienteId=)
  {
    path: 'mediciones/nueva',
    loadComponent: () =>
      import('./features/mediciones/form-medicion/form-medicion.component')
        .then(m => m.FormMedicionComponent)
  },

  // Vista del informe de una medición
  {
    path: 'reportes/:medicionId',
    loadComponent: () =>
      import('./features/reportes/vista-reporte/vista-reporte.component')
        .then(m => m.VistaReporteComponent)
  },

  // Ruta de fallback
  { path: '**', redirectTo: 'pacientes' }
];
