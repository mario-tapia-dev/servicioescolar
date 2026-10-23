package com.api.servicioescolar.controllers;

import com.api.servicioescolar.models.Solicitud;
import com.api.servicioescolar.services.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // POST /solicitudes?idEstudiante=1&documento=CONSTANCIA_ESTUDIOS
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Solicitud crear(@RequestParam Integer idEstudiante,
                           @RequestParam Solicitud.Documento documento) {
        return solicitudService.crear(idEstudiante, documento);
    }

    @GetMapping("/{id}")
    public Solicitud buscarPorId(@PathVariable Integer id) {
        return solicitudService.buscarPorId(id);
    }

    // Genera el PDF y "envía" la clave por correo (logger)
    @PostMapping("/{id}/enviar-clave")
    public Solicitud enviarClave(@PathVariable Integer id) {
        return solicitudService.generarPdfYEnviarClave(id);
    }

    // POST /solicitudes/1/cerrar?clave=ABC12345
    @PostMapping("/{id}/cerrar")
    public String cerrar(@PathVariable Integer id, @RequestParam String clave) {
        Solicitud solicitud = solicitudService.cerrar(id, clave);
        return "Solicitud " + solicitud.getId() + " cerrada. PDF de "
                + solicitud.getDocumentoSolicitado() + " descargado (representativo).";
    }
}
