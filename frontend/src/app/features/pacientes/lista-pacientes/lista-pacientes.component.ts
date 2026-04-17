import { Component, OnInit }            from '@angular/core';
import { CommonModule }                  from '@angular/common';
import { RouterLink }                    from '@angular/router';
import { FormsModule }                   from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject, switchMap } from 'rxjs';
import { Paciente }                      from '../../../shared/models/paciente.model';
import { PacienteService }               from '../../../core/services/paciente.service';

/**
 * Pantalla principal: lista de pacientes con buscador en tiempo real.
 * Ruta: /pacientes
 */
@Component({
  selector: 'app-lista-pacientes',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="row mb-3 align-items-center">
      <div class="col">
        <h2 class="fw-bold mb-0">
          <i class="bi bi-people-fill text-primary me-2"></i>Pacientes
        </h2>
      </div>
      <div class="col-auto">
        <a routerLink="/pacientes/nuevo" class="btn btn-primary">
          <i class="bi bi-person-plus-fill me-1"></i>Nuevo paciente
        </a>
      </div>
    </div>

    <!-- Buscador -->
    <div class="mb-3">
      <div class="input-group">
        <span class="input-group-text"><i class="bi bi-search"></i></span>
        <input
          type="text"
          class="form-control"
          placeholder="Buscar por nombre o RUT..."
          [(ngModel)]="terminoBusqueda"
          (ngModelChange)="onBuscar($event)">
      </div>
    </div>

    <!-- Spinner de carga -->
    <div *ngIf="cargando" class="spinner-overlay">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

    <!-- Mensaje de error -->
    <div *ngIf="error" class="alert alert-danger">
      <i class="bi bi-exclamation-triangle me-2"></i>{{ error }}
    </div>

    <!-- Tabla de pacientes -->
    <div *ngIf="!cargando && !error" class="card shadow-sm">
      <div class="table-responsive">
        <table class="table table-hover mb-0">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>RUT / DNI</th>
              <th>Edad</th>
              <th>Sexo</th>
              <th>Email</th>
              <th class="text-center">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let paciente of pacientes">
              <td class="fw-semibold">{{ paciente.nombres }} {{ paciente.apellidos }}</td>
              <td>{{ paciente.rut }}</td>
              <td>{{ calcularEdad(paciente.fechaNacimiento) }} años</td>
              <td>
                <span class="badge" [class.bg-primary]="paciente.sexo === 'M'"
                                    [class.bg-danger]="paciente.sexo === 'F'">
                  {{ paciente.sexo === 'M' ? 'Masculino' : 'Femenino' }}
                </span>
              </td>
              <td>{{ paciente.email || '—' }}</td>
              <td class="text-center">
                <div class="btn-group btn-group-sm">
                  <a [routerLink]="['/pacientes', paciente.id]"
                     class="btn btn-outline-primary" title="Ver detalle">
                    <i class="bi bi-eye"></i>
                  </a>
                  <a [routerLink]="['/pacientes', paciente.id, 'editar']"
                     class="btn btn-outline-secondary" title="Editar">
                    <i class="bi bi-pencil"></i>
                  </a>
                  <a [routerLink]="'/mediciones/nueva'"
                     [queryParams]="{ pacienteId: paciente.id }"
                     class="btn btn-outline-success" title="Nueva medición">
                    <i class="bi bi-clipboard2-plus"></i>
                  </a>
                  <button (click)="eliminar(paciente)"
                          class="btn btn-outline-danger" title="Eliminar">
                    <i class="bi bi-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
            <tr *ngIf="pacientes.length === 0">
              <td colspan="6" class="text-center text-muted py-4">
                <i class="bi bi-search me-2"></i>
                No se encontraron pacientes.
                <a routerLink="/pacientes/nuevo">Crear el primero</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class ListaPacientesComponent implements OnInit {

  pacientes:        Paciente[] = [];
  cargando          = false;
  error:            string | null = null;
  terminoBusqueda   = '';

  private busqueda$ = new Subject<string>();

  constructor(private pacienteService: PacienteService) {}

  ngOnInit(): void {
    this.cargarTodos();

    // Búsqueda reactiva con debounce de 300ms
    this.busqueda$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(termino =>
        termino.trim().length > 0
          ? this.pacienteService.buscar(termino)
          : this.pacienteService.listar()
      )
    ).subscribe({
      next:  pacientes => this.pacientes = pacientes,
      error: err       => this.error = 'Error al buscar pacientes'
    });
  }

  cargarTodos(): void {
    this.cargando = true;
    this.error = null;
    this.pacienteService.listar().subscribe({
      next:  pacientes => { this.pacientes = pacientes; this.cargando = false; },
      error: err       => { this.error = 'Error al cargar los pacientes'; this.cargando = false; }
    });
  }

  onBuscar(termino: string): void {
    this.busqueda$.next(termino);
  }

  calcularEdad(fechaNacimiento: string): number {
    const hoy  = new Date();
    const nac  = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nac.getFullYear();
    const mes  = hoy.getMonth() - nac.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < nac.getDate())) edad--;
    return edad;
  }

  eliminar(paciente: Paciente): void {
    if (!confirm(`¿Eliminar a ${paciente.nombres} ${paciente.apellidos}? Esta acción eliminará también todas sus mediciones.`)) {
      return;
    }
    this.pacienteService.eliminar(paciente.id!).subscribe({
      next:  () => this.cargarTodos(),
      error: () => this.error = 'Error al eliminar el paciente'
    });
  }
}
