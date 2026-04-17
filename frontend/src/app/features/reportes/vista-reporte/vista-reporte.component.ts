import { Component, OnInit, OnDestroy, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { CommonModule }              from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import {
  Chart,
  ArcElement, Tooltip, Legend, DoughnutController,
  BarElement, BarController, CategoryScale, LinearScale,
  LineElement, LineController, PointElement,
  ScatterController, Filler
} from 'chart.js';
import { ResultadoAntropometrico }   from '../../../shared/models/resultado-antropometrico.model';
import { MedicionService }           from '../../../core/services/medicion.service';

Chart.register(
  ArcElement, Tooltip, Legend, DoughnutController,
  BarElement, BarController, CategoryScale, LinearScale,
  LineElement, LineController, PointElement,
  ScatterController, Filler
);

/**
 * Vista del informe antropométrico con gráficos interactivos (Chart.js).
 * Ruta: /reportes/:medicionId
 */
@Component({
  selector: 'app-vista-reporte',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div *ngIf="cargando" class="spinner-overlay">
      <div class="spinner-border text-primary" role="status"></div>
    </div>

    <div *ngIf="error" class="alert alert-danger">{{ error }}</div>

    <ng-container *ngIf="resultado && !cargando">

      <!-- Encabezado -->
      <div class="card bg-primary text-white shadow mb-4">
        <div class="card-body d-flex justify-content-between align-items-center flex-wrap gap-2">
          <div>
            <h4 class="fw-bold mb-1">
              <i class="bi bi-person-circle me-2"></i>{{ resultado.nombreCompleto }}
            </h4>
            <div class="d-flex gap-3 flex-wrap small">
              <span><i class="bi bi-cake2 me-1"></i>{{ resultado.edadDecimal | number:'1.1-1' }} años</span>
              <span><i class="bi bi-gender-ambiguous me-1"></i>
                {{ resultado.sexo === 'M' ? 'Masculino' : 'Femenino' }}
              </span>
              <span><i class="bi bi-calendar3 me-1"></i>{{ resultado.fechaMedicion }}</span>
            </div>
          </div>
          <div class="d-flex gap-2">
            <a [routerLink]="['/pacientes', resultado.pacienteId]" class="btn btn-light btn-sm">
              <i class="bi bi-arrow-left me-1"></i>Volver
            </a>
            <a [href]="urlPDF" target="_blank" class="btn btn-pdf btn-sm">
              <i class="bi bi-file-pdf me-1"></i>Descargar PDF
            </a>
          </div>
        </div>
      </div>

      <!-- Métricas principales -->
      <div class="row g-3 mb-4">
        <div class="col-md-3 col-6">
          <div class="card card-metrica text-center p-3">
            <div class="text-muted small">Peso</div>
            <div class="valor text-primary">{{ resultado.peso }} kg</div>
          </div>
        </div>
        <div class="col-md-3 col-6">
          <div class="card card-metrica text-center p-3">
            <div class="text-muted small">Talla</div>
            <div class="valor text-primary">{{ resultado.talla }} cm</div>
          </div>
        </div>
        <div class="col-md-3 col-6">
          <div class="card card-metrica text-center p-3">
            <div class="text-muted small">IMC</div>
            <div class="valor"
                 [class.text-success]="resultado.clasificacionIMC === 'Normal'"
                 [class.text-warning]="resultado.clasificacionIMC === 'Sobrepeso'"
                 [class.text-danger]="resultado.clasificacionIMC === 'Obesidad'">
              {{ resultado.imc | number:'1.1-1' }}
            </div>
            <div class="badge bg-{{ badgeIMC }}">{{ resultado.clasificacionIMC }}</div>
          </div>
        </div>
        <div class="col-md-3 col-6">
          <div class="card card-metrica text-center p-3">
            <div class="text-muted small">Ratio C-C</div>
            <div class="valor text-primary">{{ resultado.ratioCinturaCadera | number:'1.2-2' }}</div>
            <div class="badge bg-{{ badgeRCC }}">{{ resultado.clasificacionRCC }}</div>
          </div>
        </div>
      </div>

      <!-- Fraccionamiento + Somatotipo -->
      <div class="row g-3 mb-4">
        <div class="col-md-5">
          <div class="card shadow-sm h-100">
            <div class="card-header"><span class="seccion-titulo">FRACCIONAMIENTO 2 MASAS</span></div>
            <div class="card-body d-flex align-items-center justify-content-center">
              <canvas #chartDonut style="max-height:220px;"></canvas>
            </div>
          </div>
        </div>
        <div class="col-md-7">
          <div class="card shadow-sm h-100">
            <div class="card-header"><span class="seccion-titulo">SOMATOTIPO HEATH-CARTER</span></div>
            <div class="card-body">
              <div class="row g-3 mb-3">
                <div class="col-4 text-center">
                  <div class="fw-bold fs-3 text-warning">{{ resultado.endomorfia | number:'1.1-1' }}</div>
                  <small class="text-muted">Endomorfia</small>
                </div>
                <div class="col-4 text-center">
                  <div class="fw-bold fs-3 text-success">{{ resultado.mesomorfia | number:'1.1-1' }}</div>
                  <small class="text-muted">Mesomorfia</small>
                </div>
                <div class="col-4 text-center">
                  <div class="fw-bold fs-3 text-primary">{{ resultado.ectomorfia | number:'1.1-1' }}</div>
                  <small class="text-muted">Ectomorfia</small>
                </div>
              </div>
              <div class="text-center mb-3">
                <span class="badge bg-secondary fs-6">{{ resultado.clasificacionSomatotipo }}</span>
              </div>
              <canvas #chartSoma style="max-height:180px;"></canvas>
            </div>
          </div>
        </div>
      </div>

      <!-- Barras composición -->
      <div class="row g-3 mb-4">
        <div class="col-md-6">
          <div class="card shadow-sm">
            <div class="card-header"><span class="seccion-titulo">COMPOSICIÓN CORPORAL (%)</span></div>
            <div class="card-body"><canvas #chartBarraPct style="max-height:200px;"></canvas></div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card shadow-sm">
            <div class="card-header"><span class="seccion-titulo">COMPOSICIÓN CORPORAL (kg)</span></div>
            <div class="card-body"><canvas #chartBarraKg style="max-height:200px;"></canvas></div>
          </div>
        </div>
      </div>

      <!-- Pliegues + Perímetros -->
      <div class="row g-3 mb-4">
        <div class="col-md-7">
          <div class="card shadow-sm">
            <div class="card-header">
              <span class="seccion-titulo">PERFIL DE PLIEGUES (mm)</span>
              <span class="ms-3 text-muted small">
                Σ6={{ resultado.suma6Pliegues }} mm &nbsp; Σ8={{ resultado.suma8Pliegues }} mm
              </span>
            </div>
            <div class="card-body"><canvas #chartPliegues style="max-height:200px;"></canvas></div>
          </div>
        </div>
        <div class="col-md-5">
          <div class="card shadow-sm">
            <div class="card-header"><span class="seccion-titulo">PERÍMETROS (cm)</span></div>
            <div class="card-body"><canvas #chartPerimetros style="max-height:200px;"></canvas></div>
          </div>
        </div>
      </div>

      <!-- Tabla resumen -->
      <div class="card shadow-sm mb-4">
        <div class="card-header"><span class="seccion-titulo">TABLA DE MEDICIONES</span></div>
        <div class="card-body">
          <div class="row g-2">
            <ng-container *ngFor="let item of tablaMediciones">
              <div class="col-md-3 col-6">
                <div class="d-flex justify-content-between border-bottom py-1">
                  <small class="text-muted">{{ item.label }}</small>
                  <strong class="small">{{ item.valor }}</strong>
                </div>
              </div>
            </ng-container>
          </div>
        </div>
      </div>

    </ng-container>
  `
})
export class VistaReporteComponent implements OnInit, OnDestroy {

  @ViewChild('chartDonut')      canvasDonut!:      ElementRef<HTMLCanvasElement>;
  @ViewChild('chartSoma')       canvasSoma!:       ElementRef<HTMLCanvasElement>;
  @ViewChild('chartBarraPct')   canvasBarraPct!:   ElementRef<HTMLCanvasElement>;
  @ViewChild('chartBarraKg')    canvasBarraKg!:    ElementRef<HTMLCanvasElement>;
  @ViewChild('chartPliegues')   canvasPliegues!:   ElementRef<HTMLCanvasElement>;
  @ViewChild('chartPerimetros') canvasPerimetros!: ElementRef<HTMLCanvasElement>;

  resultado?: ResultadoAntropometrico;
  cargando  = true;
  error:      string | null = null;
  urlPDF    = '';

  private charts: Chart[] = [];

  constructor(
    private route:           ActivatedRoute,
    private medicionService: MedicionService,
    private cdr:             ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('medicionId'));
    this.urlPDF = this.medicionService.urlPDF(id);

    this.medicionService.calcularResultado(id).subscribe({
      next: r => {
        this.resultado = r;
        this.cargando  = false;
        // 1. Forzar que Angular renderice el *ngIf con los <canvas>
        this.cdr.detectChanges();
        // 2. Diferir al siguiente tick para que el DOM esté completamente listo
        setTimeout(() => this.crearGraficos(), 0);
      },
      error: () => {
        this.error    = 'Error al calcular el resultado. Verifica que el backend esté corriendo.';
        this.cargando = false;
      }
    });
  }

  ngOnDestroy(): void {
    this.charts.forEach(c => c.destroy());
  }

  // ── Gráficos ───────────────────────────────────────────────────────

  private crearGraficos(): void {
    const r = this.resultado!;
    try {
      this.crearDonut(r);
      this.crearBarraPct(r);
      this.crearBarraKg(r);
      this.crearSomatocarta(r);
      this.crearPerfilPliegues(r);
      this.crearPerimetros(r);
    } catch (e) {
      console.error('Error al crear gráficos:', e);
    }
  }

  private crearDonut(r: ResultadoAntropometrico): void {
    const pctGrasa = Math.max(0, r.porcentajeGrasa);
    const pctMagra = Math.max(0, r.porcentajeMasaMagra);
    this.charts.push(new Chart(this.canvasDonut.nativeElement, {
      type: 'doughnut',
      data: {
        labels: [`Masa Grasa ${pctGrasa}%`, `Masa Magra ${pctMagra}%`],
        datasets: [{ data: [pctGrasa, pctMagra], backgroundColor: ['#ff8800', '#007bff'], borderWidth: 0 }]
      },
      options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
    }));
  }

  private crearBarraPct(r: ResultadoAntropometrico): void {
    const pctGrasa = Math.max(0, r.porcentajeGrasa);
    const pctMagra = Math.max(0, r.porcentajeMasaMagra);
    this.charts.push(new Chart(this.canvasBarraPct.nativeElement, {
      type: 'bar',
      data: {
        labels: ['% Masa Grasa', '% Masa Magra'],
        datasets: [{ data: [pctGrasa, pctMagra], backgroundColor: ['#ff8800', '#007bff'], borderRadius: 6 }]
      },
      options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, max: 100 } } }
    }));
  }

  private crearBarraKg(r: ResultadoAntropometrico): void {
    const masaGrasa = Math.max(0, r.masaGrasaKg);
    const masaMagra = Math.max(0, r.masaMagraKg);
    this.charts.push(new Chart(this.canvasBarraKg.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Masa Grasa (kg)', 'Masa Magra (kg)'],
        datasets: [{ data: [masaGrasa, masaMagra], backgroundColor: ['#ff8800', '#007bff'], borderRadius: 6 }]
      },
      options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
    }));
  }

  private crearSomatocarta(r: ResultadoAntropometrico): void {
    this.charts.push(new Chart(this.canvasSoma.nativeElement, {
      type: 'scatter',
      data: {
        datasets: [{
          label: r.nombreCompleto,
          data: [{ x: r.xSomatocarta, y: r.ySomatocarta }],
          backgroundColor: '#dc3545',
          pointRadius: 10,
          pointHoverRadius: 12
        }]
      },
      options: {
        scales: {
          x: { min: -9, max: 9, title: { display: true, text: 'X (Ecto − Endo)' }, grid: { color: '#e9ecef' } },
          y: { min: -9, max: 9, title: { display: true, text: 'Y (2×Meso − Endo − Ecto)' }, grid: { color: '#e9ecef' } }
        },
        plugins: { legend: { display: false } }
      }
    }));
  }

  private crearPerfilPliegues(r: ResultadoAntropometrico): void {
    this.charts.push(new Chart(this.canvasPliegues.nativeElement, {
      type: 'line',
      data: {
        labels: r.etiquetasPliegues,
        datasets: [{
          label: 'Pliegues (mm)',
          data: r.perfilesDePliegues,
          borderColor: '#007bff',
          backgroundColor: 'rgba(0,123,255,.1)',
          fill: true,
          tension: 0.3,
          pointRadius: 5
        }]
      },
      options: { responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
    }));
  }

  private crearPerimetros(r: ResultadoAntropometrico): void {
    this.charts.push(new Chart(this.canvasPerimetros.nativeElement, {
      type: 'bar',
      data: {
        labels: r.etiquetasPerimetros,
        datasets: [{ data: r.perimetrosCorregidos, backgroundColor: '#28a745', borderRadius: 4 }]
      },
      options: { responsive: true, indexAxis: 'y' as const, plugins: { legend: { display: false } }, scales: { x: { beginAtZero: true } } }
    }));
  }

  // ── Helpers ────────────────────────────────────────────────────────

  get badgeIMC(): string {
    switch (this.resultado?.clasificacionIMC) {
      case 'Normal':    return 'success';
      case 'Sobrepeso': return 'warning';
      case 'Obesidad':  return 'danger';
      default:          return 'secondary';
    }
  }

  get badgeRCC(): string {
    const rcc = this.resultado?.clasificacionRCC || '';
    if (rcc.toLowerCase().includes('bajo'))  return 'success';
    if (rcc.toLowerCase().includes('alto'))  return 'danger';
    if (rcc.toLowerCase().includes('dato'))  return 'secondary';
    return 'warning';
  }

  get tablaMediciones(): { label: string; valor: string }[] {
    if (!this.resultado) return [];
    const r = this.resultado;
    return [
      { label: 'Peso',         valor: `${r.peso} kg` },
      { label: 'Talla',        valor: `${r.talla} cm` },
      { label: 'IMC',          valor: `${r.imc} (${r.clasificacionIMC})` },
      { label: '% Grasa',      valor: `${r.porcentajeGrasa}%` },
      { label: 'Masa grasa',   valor: `${r.masaGrasaKg} kg` },
      { label: '% Magra',      valor: `${r.porcentajeMasaMagra}%` },
      { label: 'Masa magra',   valor: `${r.masaMagraKg} kg` },
      { label: 'Ratio C-C',    valor: `${r.ratioCinturaCadera}` },
      { label: 'Endomorfia',   valor: `${r.endomorfia}` },
      { label: 'Mesomorfia',   valor: `${r.mesomorfia}` },
      { label: 'Ectomorfia',   valor: `${r.ectomorfia}` },
      { label: 'Σ 6 pliegues', valor: `${r.suma6Pliegues} mm` },
      { label: 'Σ 8 pliegues', valor: `${r.suma8Pliegues} mm` },
      ...r.etiquetasPliegues.map((e, i) => ({ label: e, valor: `${r.perfilesDePliegues[i]} mm` })),
      ...r.etiquetasPerimetros.map((e, i) => ({ label: e, valor: `${r.perimetrosCorregidos[i]} cm` }))
    ];
  }
}
