package com.api.servicioescolar.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "solicitud")
public class Solicitud {

    public enum Documento {
        // Cada tipo de documento define su propio precio.
        // Para expandir, solo agrega más valores: CALIFICACIONES(new BigDecimal("80.00")), etc.
        CONSTANCIA_ESTUDIOS(new BigDecimal("50.00"));

        private final BigDecimal precio;

        Documento(BigDecimal precio) {
            this.precio = precio;
        }

        public BigDecimal getPrecio() {
            return precio;
        }
    }

    public enum Estado {
        CREADA,
        COBRO_APROBADO,
        CORREO_ENVIADO,
        SOLICITUD_CERRADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "documento_solicitado", nullable = false)
    private Documento documentoSolicitado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    // La clave nunca se devuelve en el JSON: solo le "llega" al estudiante por correo (logger).
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(unique = true)
    private String clave;

    public Solicitud() {
    }

    public Solicitud(Documento documentoSolicitado, LocalDate fechaSolicitud, Estado estado, Estudiante estudiante) {
        this.documentoSolicitado = documentoSolicitado;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.estudiante = estudiante;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Documento getDocumentoSolicitado() {
        return documentoSolicitado;
    }

    public void setDocumentoSolicitado(Documento documentoSolicitado) {
        this.documentoSolicitado = documentoSolicitado;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
