import { Component, OnInit }         from '@angular/core';
import { CommonModule }              from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { Paciente }                  from '../../../shared/models/paciente.model';
import { PacienteService }           from '../../../core/services/paciente.service';

/**
 * Formulario reactivo para crear o editar un paciente.
 * Ruta: /pacientes/nuevo  |  /pacientes/:id/editar
 */
@Component({
  selector: 'app-form-paciente',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="row justify-content-center">
      <div class="col-md-8 col-lg-6">
        <div class="card shadow-sm">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
              <i class="bi bi-person-{{ esEdicion ? 'gear' : 'plus' }}-fill me-2"></i>
              {{ esEdicion ? 'Editar paciente' : 'Nuevo paciente' }}
            </h5>
          </div>
          <div class="card-body">

            <div *ngIf="error" class="alert alert-danger">{{ error }}</div>

            <form [formGroup]="form" (ngSubmit)="guardar()">

              <div class="row g-3">

                <!-- Nombres -->
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Nombres *</label>
                  <input type="text" class="form-control"
                         [class.is-invalid]="campoInvalido('nombres')"
                         formControlName="nombres" placeholder="Ej: Sergio Antonio">
                  <div class="invalid-feedback">Los nombres son obligatorios</div>
                </div>

                <!-- Apellidos -->
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Apellidos *</label>
                  <input type="text" class="form-control"
                         [class.is-invalid]="campoInvalido('apellidos')"
                         formControlName="apellidos" placeholder="Ej: Marquez González">
                  <div class="invalid-feedback">Los apellidos son obligatorios</div>
                </div>

                <!-- RUT -->
                <div class="col-md-6">
                  <label class="form-label fw-semibold">RUT / DNI *</label>
                  <input type="text" class="form-control"
                         [class.is-invalid]="campoInvalido('rut')"
                         formControlName="rut" placeholder="Ej: 12.345.678-9">
                  <div class="invalid-feedback">El RUT/DNI es obligatorio</div>
                </div>

                <!-- Fecha de nacimiento -->
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Fecha de nacimiento *</label>
                  <input type="date" class="form-control"
                         [class.is-invalid]="campoInvalido('fechaNacimiento')"
                         formControlName="fechaNacimiento">
                  <div class="invalid-feedback">La fecha de nacimiento es obligatoria</div>
                </div>

                <!-- Sexo -->
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Sexo *</label>
                  <select class="form-select"
                          [class.is-invalid]="campoInvalido('sexo')"
                          formControlName="sexo">
                    <option value="">Seleccionar...</option>
                    <option value="M">Masculino</option>
                    <option value="F">Femenino</option>
                  </select>
                  <div class="invalid-feedback">Selecciona el sexo</div>
                </div>

                <!-- Teléfono -->
                <div class="col-md-6">
                  <label class="form-label">Teléfono</label>
                  <input type="tel" class="form-control"
                         formControlName="telefono" placeholder="+56 9 1234 5678">
                </div>

                <!-- Email -->
                <div class="col-12">
                  <label class="form-label">Email</label>
                  <input type="email" class="form-control"
                         [class.is-invalid]="campoInvalido('email')"
                         formControlName="email" placeholder="correo@ejemplo.com">
                  <div class="invalid-feedback">El email no tiene formato válido</div>
                </div>

              </div><!-- /row -->

              <div class="d-flex gap-2 justify-content-end mt-4">
                <a routerLink="/pacientes" class="btn btn-outline-secondary">
                  <i class="bi bi-x-circle me-1"></i>Cancelar
                </a>
                <button type="submit" class="btn btn-primary"
                        [disabled]="guardando">
                  <span *ngIf="guardando" class="spinner-border spinner-border-sm me-1"></span>
                  <i *ngIf="!guardando" class="bi bi-check-circle me-1"></i>
                  {{ guardando ? 'Guardando...' : 'Guardar' }}
                </button>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>
  `
})
export class FormPacienteComponent implements OnInit {

  form!:       FormGroup;
  esEdicion  = false;
  pacienteId?: number;
  guardando  = false;
  error:       string | null = null;

  constructor(
    private fb:             FormBuilder,
    private pacienteService: PacienteService,
    private router:         Router,
    private route:          ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombres:          ['', Validators.required],
      apellidos:        ['', Validators.required],
      rut:              ['', Validators.required],
      fechaNacimiento:  ['', Validators.required],
      sexo:             ['', Validators.required],
      email:            ['', Validators.email],
      telefono:         ['']
    });

    // Si hay :id en la ruta, estamos en modo edición
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.esEdicion = true;
      this.pacienteId = Number(id);
      this.cargarPaciente(this.pacienteId);
    }
  }

  cargarPaciente(id: number): void {
    this.pacienteService.obtener(id).subscribe({
      next: paciente => this.form.patchValue(paciente),
      error: () => this.error = 'Error al cargar el paciente'
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando = true;
    this.error = null;
    const datos: Paciente = this.form.value;

    const operacion = this.esEdicion
      ? this.pacienteService.actualizar(this.pacienteId!, datos)
      : this.pacienteService.crear(datos);

    operacion.subscribe({
      next: paciente => {
        this.guardando = false;
        this.router.navigate(['/pacientes', paciente.id]);
      },
      error: err => {
        this.guardando = false;
        this.error = err.error?.message || 'Error al guardar el paciente';
      }
    });
  }

  campoInvalido(campo: string): boolean {
    const control = this.form.get(campo);
    return !!(control && control.invalid && control.touched);
  }
}
