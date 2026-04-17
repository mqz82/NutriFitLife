import { Injectable }               from '@angular/core';
import { HttpClient, HttpParams }   from '@angular/common/http';
import { Observable }               from 'rxjs';
import { Paciente }                 from '../../shared/models/paciente.model';

/**
 * Servicio Angular para comunicarse con los endpoints de pacientes del backend.
 * Todos los métodos retornan Observable para ser suscritos en los componentes.
 */
@Injectable({ providedIn: 'root' })
export class PacienteService {

  private readonly API_URL = 'http://localhost:8080/api/pacientes';

  constructor(private http: HttpClient) {}

  /** GET /api/pacientes */
  listar(): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(this.API_URL);
  }

  /** GET /api/pacientes/{id} */
  obtener(id: number): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.API_URL}/${id}`);
  }

  /** POST /api/pacientes */
  crear(paciente: Paciente): Observable<Paciente> {
    return this.http.post<Paciente>(this.API_URL, paciente);
  }

  /** PUT /api/pacientes/{id} */
  actualizar(id: number, paciente: Paciente): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.API_URL}/${id}`, paciente);
  }

  /** DELETE /api/pacientes/{id} */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  /** GET /api/pacientes/buscar?q= */
  buscar(q: string): Observable<Paciente[]> {
    const params = new HttpParams().set('q', q);
    return this.http.get<Paciente[]>(`${this.API_URL}/buscar`, { params });
  }
}
