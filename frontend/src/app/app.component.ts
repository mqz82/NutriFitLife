import { Component }       from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

/**
 * Componente raíz de la aplicación.
 * Contiene la barra de navegación y el <router-outlet> para
 * renderizar las vistas activas según la ruta.
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <!-- Barra de navegación principal -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
      <div class="container-fluid">
        <a class="navbar-brand" routerLink="/">
          <i class="bi bi-heart-pulse-fill me-2"></i>NutriFitLife
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" routerLink="/pacientes" routerLinkActive="active">
                <i class="bi bi-people-fill me-1"></i>Pacientes
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/mediciones/nueva" routerLinkActive="active">
                <i class="bi bi-clipboard2-pulse-fill me-1"></i>Nueva Medición
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Contenido principal -->
    <main class="container-fluid py-4">
      <router-outlet></router-outlet>
    </main>
  `
})
export class AppComponent {}
