import { Component, OnInit }   from '@angular/core';
import { CommonModule }         from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Paciente }             from '../../../shared/models/paciente.model';
import { Medicion }             from '../../../shared/models/medicion.model';
import { PacienteService }      from '../../../core/services/paciente.service';
import { MedicionService }      from '../../../core/services/medicion.service';

/**
 * Pantalla de detalle del paciente: datos personales + historial de mediciones.
 * Ruta: /pacientes/:id
 */
@Component({
  selector: 'app-detalle-paciente',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <!-- Spinner de carga -->
    <div *ngIf="cargando" class="spinner-overlay">
      <div class="spinner-border text-primary" role="status"></div>
    </div>

    <ng-container *ngIf="!cargando && paciente">

      <!-- Encabezado -->
      <div class="d-flex align-items-center justify-content-between mb-4">
        <div>
          <a routerLink="/pacientes" class="btn btn-sm btn-outline-secondary me-2">
            <i class="bi bi-arrow-left"></i>
          </a>
          <h2 class="d-inline fw-bold">
            {{ paciente.nombres }} {{ paciente.apellidos }}
          </h2>
        </div>
        <div class="d-flex gap-2">
          <a [routerLink]="['/pacientes', paciente.id, 'editar']"
             class="btn btn-outline-secondary">
            <i class="bi bi-pencil me-1"></i>Editar
          </a>
          <a routerLink="/mediciones/nueva"
             [queryParams]="{ pacienteId: paciente.id }"
             class="btn btn-primary">
            <i class="bi bi-clipboard2-plus me-1"></i>Nueva medición
          </a>
        </div>
      </div>

      <!-- Datos del paciente -->
      <div class="card shadow-sm mb-4">
        <div class="card-header">
          <h6 class="mb-0 fw-bold text-primary">
            <i class="bi bi-person-vcard me-2"></i>Datos personales
          </h6>
        </div>
        <div class="card-body">
          <div class="row g-3">
            <div class="col-md-3">
              <small class="text-muted d-block">RUT / DNI</small>
              <strong>{{ paciente.rut }}</strong>
            </div>
            <div class="col-md-3">
              <small class="text-muted d-block">Fecha de nacimiento</small>
              <strong>{{ paciente.fechaNacimiento | date:'dd/MM/yyyy' }}</strong>
            </div>
            <div class="col-md-3">
              <small class="text-muted d-block">Sexo</small>
              <strong>{{ paciente.sexo === 'M' ? 'Masculino' : 'Femenino' }}</strong>
            </div>
            <div class="col-md-3">
              <small class="text-muted d-block">Edad actual</small>
              <strong>{{ calcularEdad(paciente.fechaNacimiento) }} años</strong>
            </div>
            <div class="col-md-6" *ngIf="paciente.email">
              <small class="text-muted d-block">Email</small>
              <strong>{{ paciente.email }}</strong>
            </div>
            <div class="col-md-6" *ngIf="paciente.telefono">
              <small class="text-muted d-block">Teléfono</small>
              <strong>{{ paciente.telefono }}</strong>
            </div>
          </div>
        </div>
      </div>

      <!-- Historial de mediciones -->
      <div class="card shadow-sm">
        <div class="card-header d-flex align-items-center justify-content-between">
          <h6 class="mb-0 fw-bold text-primary">
            <i class="bi bi-clipboard2-data me-2"></i>
            Historial de mediciones ({{ mediciones.length }})
          </h6>
        </div>
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Peso</th>
                <th>Talla</th>
                <th class="text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let medicion of mediciones">
                <td>{{ medicion.fechaMedicion | date:'dd/MM/yyyy' }}</td>
                <td>{{ medicion.peso }} kg</td>
                <td>{{ medicion.talla }} cm</td>
                <td class="text-center">
                  <div class="btn-group btn-group-sm">
                    <a [routerLink]="['/reportes', medicion.id]"
                       class="btn btn-outline-primary" title="Ver informe">
                      <i class="bi bi-bar-chart-fill"></i>
                    </a>
                    <a [href]="urlPDF(medicion.id!)" target="_blank"
                       class="btn btn-outline-danger" title="Descargar PDF">
                      <i class="bi bi-file-pdf"></i>
                    </a>
                    <button (click)="eliminarMedicion(medicion)"
                            class="btn btn-outline-secondary" title="Eliminar">
                      <i class="bi bi-trash"></i>
                    </button>
                  </div>
                </td>
              </tr>
              <tr *ngIf="mediciones.length === 0">
                <td colspan="4" class="text-center text-muted py-3">
                  Sin mediciones aún.
                  <a routerLink="/mediciones/nueva"
                     [queryParams]="{ pacienteId: paciente.id }">
                    Registrar primera medición
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </ng-container>
  `
})
export class DetallePacienteComponent implements OnInit {

  paciente?:  Paciente;
  mediciones: Medicion[] = [];
  cargando  = true;
  error:      string | null = null;

  constructor(
    private route:           ActivatedRoute,
    private pacienteService: PacienteService,
    private medicionService: MedicionService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.pacienteService.obtener(id).subscribe({
      next: p => {
        this.paciente = p;
        this.cargarMediciones(id);
      },
      error: () => { this.error = 'Error al cargar el paciente'; this.cargando = false; }
    });
  }

  cargarMediciones(pacienteId: number): void {
    this.medicionService.listarPorPaciente(pacienteId).subscribe({
      next: ms => { this.mediciones = ms; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  eliminarMedicion(medicion: Medicion): void {
    if (!confirm('¿Eliminar esta medición?')) return;
    this.medicionService.eliminar(medicion.id!).subscribe({
      next: () => this.cargarMediciones(this.paciente!.id!)
    });
  }

  calcularEdad(fechaNacimiento: string): number {
    const hoy = new Date();
    const nac = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nac.getFullYear();
    const mes = hoy.getMonth() - nac.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < nac.getDate())) edad--;
    return edad;
  }

  urlPDF(medicionId: number): string {
    return this.medicionService.urlPDF(medicionId);
  }
}
