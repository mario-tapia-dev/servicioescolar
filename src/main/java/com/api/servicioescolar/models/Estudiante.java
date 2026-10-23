package com.api.servicioescolar.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer matricula;

    @Column(nullable = false)
    private Boolean inscripcion;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    public Estudiante() {
    }

    public Estudiante(Integer matricula, Boolean inscripcion, LocalDate fechaInscripcion) {
        this.matricula = matricula;
        this.inscripcion = inscripcion;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public Boolean getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Boolean inscripcion) {
        this.inscripcion = inscripcion;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}
