package com.nutriFitLife.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un paciente en el sistema NutriFitLife.
 * Almacena los datos personales utilizados para identificar al paciente
 * y calcular la edad en cada evaluación.
 */
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(nullable = false)
    private String apellidos;

    /** RUT o DNI — identificador único del paciente */
    @NotBlank(message = "El RUT/DNI es obligatorio")
    @Column(nullable = false, unique = true)
    private String rut;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    /** "M" para masculino, "F" para femenino */
    @NotBlank(message = "El sexo es obligatorio")
    @Column(nullable = false, length = 1)
    private String sexo;

    @Email(message = "El email no tiene formato válido")
    private String email;

    private String telefono;

    /** Fecha de creación del registro — se asigna automáticamente */
    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    /** Lista de mediciones asociadas al paciente */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medicion> mediciones = new ArrayList<>();

    @PrePersist
    protected void onCrear() {
        this.creadoEn = LocalDateTime.now();
    }

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public Paciente() {}

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

    public List<Medicion> getMediciones() { return mediciones; }
    public void setMediciones(List<Medicion> mediciones) { this.mediciones = mediciones; }

    /** Nombre completo calculado (nombres + apellidos) */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
