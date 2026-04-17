import { ApplicationConfig } from '@angular/core';
import { provideRouter }     from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes }            from './app.routes';

/**
 * Configuración raíz de la aplicación Angular (standalone, sin NgModule).
 * Registra el router y el cliente HTTP como proveedores globales.
 */
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
};
