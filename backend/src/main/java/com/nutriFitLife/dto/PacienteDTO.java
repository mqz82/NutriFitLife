package com.nutriFitLife.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Paciente entre el frontend y el backend.
 * Evita exponer directamente la entidad JPA en la API REST.
 */
public class PacienteDTO {

    private Long id;
    private String nombres;
    private String apellidos;
    private String rut;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String email;
    private String telefono;
    private LocalDateTime creadoEn;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public PacienteDTO() {}

    public PacienteDTO(Long id, String nombres, String apellidos, String rut,
                       LocalDate fechaNacimiento, String sexo, String email,
                       String telefono, LocalDateTime creadoEn) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.rut = rut;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.email = email;
        this.telefono = telefono;
        this.creadoEn = creadoEn;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
