# NutriFitLife — Sistema de Evaluación Antropométrica

Aplicación web para nutricionistas que permite registrar pacientes, ingresar
mediciones antropométricas, calcular automáticamente indicadores de composición
corporal y generar informes PDF profesionales.

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 17 + Spring Boot 3.x |
| Frontend | Angular 17+ (standalone components) |
| Base de datos | H2 en memoria (desarrollo) |
| ORM | Spring Data JPA + Hibernate |
| PDF | iText 7 + JFreeChart |
| Build backend | Maven |
| Build frontend | Angular CLI |

---

## Requisitos previos

- **Java 17+** — `java -version`
- **Maven 3.8+** — `mvn -version`
- **Node.js 18+** — `node -v`
- **Angular CLI 17+** — `npm install -g @angular/cli`

---

## Instalación y ejecución

### 1. Backend (Spring Boot)

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

El servidor arranca en **http://localhost:8080**

- Consola H2: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:NutriFitLife`
  - Usuario: `sa` / Contraseña: *(vacía)*

### 2. Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

La aplicación arranca en **http://localhost:4200**

---

## Datos de prueba (cargados automáticamente)

Al iniciar el backend se cargan automáticamente 3 pacientes con 2 mediciones
cada uno desde `data.sql`.

**Paciente de referencia: Sergio Marquez**
- Medición del 11/08/2025 con todos los campos completos
- Resultados esperados:
  - IMC: **24.6** (Normal)
  - % Grasa: **~27.3%** → 21.3 kg
  - % Masa Magra: **~72.7%** → 56.7 kg
  - Suma 6 pliegues: **112 mm**
  - Suma 8 pliegues: **140 mm** (con bíceps=12 y crestaIlíaca=22)
  - Ratio C-C: **0.77** (Riesgo bajo)
  - Somatotipo: Endo=5.1 / Meso=2.8 / Ecto=1.9
  - Somatocarta: X=−3.2 / Y=−1.4

---

## API REST

### Pacientes

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/pacientes` | Lista todos |
| GET | `/api/pacientes/{id}` | Obtiene por ID |
| POST | `/api/pacientes` | Crea nuevo |
| PUT | `/api/pacientes/{id}` | Actualiza |
| DELETE | `/api/pacientes/{id}` | Elimina |
| GET | `/api/pacientes/buscar?q=` | Busca por nombre o RUT |

### Mediciones

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/mediciones` | Registra medición |
| GET | `/api/mediciones/paciente/{pacienteId}` | Historial del paciente |
| GET | `/api/mediciones/{id}` | Obtiene medición |
| GET | `/api/mediciones/{id}/resultado` | Calcula y retorna resultados |
| DELETE | `/api/mediciones/{id}` | Elimina medición |

### Reportes

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/reportes/pdf/{medicionId}` | Descarga informe PDF |

---

## Fórmulas implementadas

### IMC
```
IMC = peso(kg) / talla(m)²
```
Clasificación OMS: Bajo peso < 18.5 | Normal 18.5–24.9 | Sobrepeso 25–29.9 | Obesidad ≥ 30

### Fraccionamiento en 2 masas (Durnin & Womersley, 1974 + Siri, 1956)
```
log_suma4 = log10(bíceps + tríceps + subescapular + supraespinal)
Densidad  = C − (M × log_suma4)       ← coeficientes según sexo y edad
%Grasa    = ((4.95 / Densidad) − 4.50) × 100
```

### Somatotipo Heath-Carter (1967)
- **Endomorfia**: ecuación cúbica con suma de 3 pliegues corregida por talla
- **Mesomorfia**: basada en diámetros óseos, perímetros corregidos y talla
- **Ectomorfia**: basada en el Height-Weight Ratio (HWR)
- **Somatocarta**: X = Ecto − Endo | Y = 2×Meso − (Endo + Ecto)

---

## Pruebas unitarias

```bash
cd backend
mvn test
```

Los tests validan todas las fórmulas contra los datos conocidos de Sergio Marquez.

---

## Estructura del proyecto

```
nutriFitLife/
├── backend/                    ← Spring Boot
│   └── src/main/java/com/nutriFitLife/
│       ├── NutriFitLifeApplication.java
│       ├── config/CorsConfig.java
│       ├── controller/         ← REST Controllers
│       ├── service/            ← Lógica de negocio + cálculos + PDF
│       ├── model/              ← Entidades JPA
│       ├── dto/                ← Data Transfer Objects
│       └── repository/         ← Spring Data JPA
└── frontend/                   ← Angular 17
    └── src/app/
        ├── core/services/      ← Servicios HTTP
        ├── shared/models/      ← Interfaces TypeScript
        └── features/           ← Componentes por pantalla
            ├── pacientes/
            ├── mediciones/
            └── reportes/
```

---

## Navegación del frontend

| Ruta | Pantalla |
|---|---|
| `/pacientes` | Lista de pacientes con buscador |
| `/pacientes/nuevo` | Formulario de nuevo paciente |
| `/pacientes/:id` | Detalle del paciente + historial |
| `/pacientes/:id/editar` | Editar paciente |
| `/mediciones/nueva?pacienteId=` | Formulario de medición (4 tabs) |
| `/reportes/:medicionId` | Informe con gráficos + descarga PDF |
