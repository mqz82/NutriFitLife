import { Component, OnInit }           from '@angular/core';
import { CommonModule }                 from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { MedicionService }              from '../../../core/services/medicion.service';
import { PacienteService }              from '../../../core/services/paciente.service';
import { Paciente }                     from '../../../shared/models/paciente.model';

/**
 * Formulario de medición antropométrica organizado en 4 tabs.
 * Ruta: /mediciones/nueva?pacienteId=
 */
@Component({
  selector: 'app-form-medicion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="row justify-content-center">
      <div class="col-lg-10">

        <!-- Encabezado -->
        <div class="d-flex align-items-center mb-3">
          <a [routerLink]="pacienteId ? ['/pacientes', pacienteId] : '/pacientes'"
             class="btn btn-sm btn-outline-secondary me-2">
            <i class="bi bi-arrow-left"></i>
          </a>
          <h4 class="fw-bold mb-0">
            <i class="bi bi-clipboard2-pulse text-primary me-2"></i>
            Nueva medición
            <span *ngIf="paciente" class="text-muted fs-6 ms-2">
              — {{ paciente.nombres }} {{ paciente.apellidos }}
            </span>
          </h4>
        </div>

        <div *ngIf="error" class="alert alert-danger">{{ error }}</div>

        <div class="card shadow-sm">
          <div class="card-body">
            <form [formGroup]="form" (ngSubmit)="guardar()">

              <!-- Tabs de navegación -->
              <ul class="nav nav-tabs mb-4" id="medicionTabs">
                <li class="nav-item" *ngFor="let tab of tabs; let i = index">
                  <button type="button" class="nav-link"
                          [class.active]="tabActivo === i"
                          (click)="tabActivo = i">
                    <i class="bi bi-{{ tab.icono }} me-1"></i>{{ tab.label }}
                  </button>
                </li>
              </ul>

              <!-- TAB 1: Básico -->
              <div [hidden]="tabActivo !== 0">
                <div class="row g-3">
                  <div class="col-md-4">
                    <label class="form-label fw-semibold">Fecha medición *</label>
                    <input type="date" class="form-control"
                           [class.is-invalid]="inv('fechaMedicion')"
                           formControlName="fechaMedicion">
                    <div class="invalid-feedback">Fecha obligatoria</div>
                  </div>
                  <div class="col-md-4">
                    <label class="form-label fw-semibold">Peso (kg) *</label>
                    <input type="number" step="0.1" min="20" max="300" class="form-control"
                           [class.is-invalid]="inv('peso')"
                           formControlName="peso" placeholder="Ej: 78.5">
                    <div class="invalid-feedback">Peso obligatorio (20–300 kg)</div>
                  </div>
                  <div class="col-md-4">
                    <label class="form-label fw-semibold">Talla (cm) *</label>
                    <input type="number" step="0.1" min="100" max="250" class="form-control"
                           [class.is-invalid]="inv('talla')"
                           formControlName="talla" placeholder="Ej: 178.0">
                    <div class="invalid-feedback">Talla obligatoria (100–250 cm)</div>
                  </div>
                </div>
              </div>

              <!-- TAB 2: Pliegues cutáneos -->
              <div [hidden]="tabActivo !== 1">
                <p class="text-muted small">
                  <i class="bi bi-info-circle me-1"></i>
                  Protocolo ISAK — 8 pliegues cutáneos en milímetros (mm)
                </p>
                <div class="row g-3">
                  <ng-container *ngFor="let campo of camposPliegues">
                    <div class="col-md-3">
                      <label class="form-label">{{ campo.label }}</label>
                      <div class="input-group input-group-sm">
                        <input type="number" step="0.5" min="1" max="100"
                               class="form-control"
                               [formControlName]="campo.control"
                               placeholder="mm">
                        <span class="input-group-text">mm</span>
                      </div>
                    </div>
                  </ng-container>
                </div>
              </div>

              <!-- TAB 3: Perímetros -->
              <div [hidden]="tabActivo !== 2">
                <p class="text-muted small">
                  <i class="bi bi-info-circle me-1"></i>
                  Perímetros corporales en centímetros (cm)
                </p>
                <div class="row g-3">
                  <ng-container *ngFor="let campo of camposPerimetros">
                    <div class="col-md-4">
                      <label class="form-label">{{ campo.label }}</label>
                      <div class="input-group input-group-sm">
                        <input type="number" step="0.1" min="10" max="200"
                               class="form-control"
                               [formControlName]="campo.control"
                               placeholder="cm">
                        <span class="input-group-text">cm</span>
                      </div>
                    </div>
                  </ng-container>
                </div>
              </div>

              <!-- TAB 4: Diámetros óseos -->
              <div [hidden]="tabActivo !== 3">
                <p class="text-muted small">
                  <i class="bi bi-info-circle me-1"></i>
                  Diámetros óseos en centímetros (cm) — necesarios para el somatotipo
                </p>
                <div class="row g-3">
                  <div class="col-md-4">
                    <label class="form-label">Codo (biepicondilar húmero)</label>
                    <div class="input-group input-group-sm">
                      <input type="number" step="0.1" min="3" max="15"
                             class="form-control" formControlName="diametroCodo"
                             placeholder="Ej: 7.0">
                      <span class="input-group-text">cm</span>
                    </div>
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Rodilla (bicondilar fémur)</label>
                    <div class="input-group input-group-sm">
                      <input type="number" step="0.1" min="5" max="20"
                             class="form-control" formControlName="diametroRodilla"
                             placeholder="Ej: 9.5">
                      <span class="input-group-text">cm</span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Botones de acción -->
              <hr class="mt-4">
              <div class="d-flex justify-content-between align-items-center">
                <div class="d-flex gap-2">
                  <button type="button" class="btn btn-outline-secondary btn-sm"
                          [disabled]="tabActivo === 0"
                          (click)="tabActivo = tabActivo - 1">
                    <i class="bi bi-chevron-left"></i> Anterior
                  </button>
                  <button type="button" class="btn btn-outline-primary btn-sm"
                          [disabled]="tabActivo === tabs.length - 1"
                          (click)="tabActivo = tabActivo + 1">
                    Siguiente <i class="bi bi-chevron-right"></i>
                  </button>
                </div>
                <button type="submit" class="btn btn-success"
                        [disabled]="guardando">
                  <span *ngIf="guardando" class="spinner-border spinner-border-sm me-1"></span>
                  <i *ngIf="!guardando" class="bi bi-check-circle me-1"></i>
                  {{ guardando ? 'Guardando...' : 'Guardar y ver informe' }}
                </button>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>
  `
})
export class FormMedicionComponent implements OnInit {

  form!:      FormGroup;
  pacienteId?: number;
  paciente?:  Paciente;
  guardando = false;
  error:      string | null = null;
  tabActivo = 0;

  readonly tabs = [
    { label: 'Básico',        icono: 'rulers' },
    { label: 'Pliegues',      icono: 'layers' },
    { label: 'Perímetros',    icono: 'arrows-collapse' },
    { label: 'Diámetros',     icono: 'distribute-vertical' }
  ];

  readonly camposPliegues = [
    { label: 'Tricipital',       control: 'pliegueTricipal' },
    { label: 'Subescapular',     control: 'pliegueSubescapular' },
    { label: 'Supraespinal',     control: 'pliegueSupraespinal' },
    { label: 'Abdominal',        control: 'pliegueAbdominal' },
    { label: 'Muslo frontal',    control: 'pliegueMusloFrontal' },
    { label: 'Pantorrilla med.', control: 'plieguePantorrillaMed' },
    { label: 'Bicipital',        control: 'pliegueBicipal' },
    { label: 'Cresta ilíaca',    control: 'pliegueCrestaIliaca' }
  ];

  readonly camposPerimetros = [
    { label: 'Brazo relajado',      control: 'perimetroBrazoRelajado' },
    { label: 'Brazo flex./tensión', control: 'perimetroBrazoFlexTension' },
    { label: 'Cintura mínima',      control: 'perimetroCinturaMinima' },
    { label: 'Cadera máximo',       control: 'perimetroCaderaMaximo' },
    { label: 'Muslo medial',        control: 'perimetroMusloMedal' },
    { label: 'Pantorrilla máx.',    control: 'perimetroPantorrillaMax' }
  ];

  constructor(
    private fb:              FormBuilder,
    private medicionService: MedicionService,
    private pacienteService: PacienteService,
    private router:          Router,
    private route:           ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Leer pacienteId desde query params
    const pid = this.route.snapshot.queryParamMap.get('pacienteId');
    if (pid) {
      this.pacienteId = Number(pid);
      this.pacienteService.obtener(this.pacienteId).subscribe({
        next: p => this.paciente = p
      });
    }

    this.form = this.fb.group({
      fechaMedicion:             [new Date().toISOString().slice(0, 10), Validators.required],
      peso:                      ['', [Validators.required, Validators.min(20), Validators.max(300)]],
      talla:                     ['', [Validators.required, Validators.min(100), Validators.max(250)]],
      pliegueTricipal:           [''],
      pliegueSubescapular:       [''],
      pliegueSupraespinal:       [''],
      pliegueAbdominal:          [''],
      pliegueMusloFrontal:       [''],
      plieguePantorrillaMed:     [''],
      pliegueBicipal:            [''],
      pliegueCrestaIliaca:       [''],
      perimetroBrazoRelajado:    [''],
      perimetroBrazoFlexTension: [''],
      perimetroCinturaMinima:    [''],
      perimetroCaderaMaximo:     [''],
      perimetroMusloMedal:       [''],
      perimetroPantorrillaMax:   [''],
      diametroCodo:              [''],
      diametroRodilla:           ['']
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.tabActivo = 0;
      return;
    }
    if (!this.pacienteId) {
      this.error = 'No se especificó el paciente. Use el enlace desde el perfil del paciente.';
      return;
    }

    this.guardando = true;
    this.error = null;
    const medicion = { ...this.form.value, pacienteId: this.pacienteId };

    this.medicionService.registrar(medicion).subscribe({
      next: m => {
        this.guardando = false;
        // Redirige directamente al informe calculado
        this.router.navigate(['/reportes', m.id]);
      },
      error: err => {
        this.guardando = false;
        this.error = err.error?.message || 'Error al guardar la medición';
      }
    });
  }

  inv(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c && c.invalid && c.touched);
  }
}
