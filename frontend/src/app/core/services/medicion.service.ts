import { Injectable }             from '@angular/core';
import { HttpClient }             from '@angular/common/http';
import { Observable }             from 'rxjs';
import { Medicion }               from '../../shared/models/medicion.model';
import { ResultadoAntropometrico } from '../../shared/models/resultado-antropometrico.model';

/**
 * Servicio Angular para comunicarse con los endpoints de mediciones del backend.
 */
@Injectable({ providedIn: 'root' })
export class MedicionService {

  private readonly API_URL      = 'http://localhost:8080/api/mediciones';
  private readonly REPORTE_URL  = 'http://localhost:8080/api/reportes';

  constructor(private http: HttpClient) {}

  /** POST /api/mediciones */
  registrar(medicion: Medicion): Observable<Medicion> {
    return this.http.post<Medicion>(this.API_URL, medicion);
  }

  /** GET /api/mediciones/paciente/{pacienteId} */
  listarPorPaciente(pacienteId: number): Observable<Medicion[]> {
    return this.http.get<Medicion[]>(`${this.API_URL}/paciente/${pacienteId}`);
  }

  /** GET /api/mediciones/{id} */
  obtener(id: number): Observable<Medicion> {
    return this.http.get<Medicion>(`${this.API_URL}/${id}`);
  }

  /** GET /api/mediciones/{id}/resultado */
  calcularResultado(id: number): Observable<ResultadoAntropometrico> {
    return this.http.get<ResultadoAntropometrico>(`${this.API_URL}/${id}/resultado`);
  }

  /** DELETE /api/mediciones/{id} */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  /** Retorna la URL de descarga del PDF para usar en un enlace directo */
  urlPDF(medicionId: number): string {
    return `${this.REPORTE_URL}/pdf/${medicionId}`;
  }
}
