package com.api.servicioescolar.controllers;

import com.api.servicioescolar.models.Cobro;
import com.api.servicioescolar.services.CobroService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cobros")
public class CobroController {

    private final CobroService cobroService;

    public CobroController(CobroService cobroService) {
        this.cobroService = cobroService;
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public Cobro buscarPorSolicitud(@PathVariable Integer idSolicitud) {
        return cobroService.buscarPorSolicitud(idSolicitud);
    }

    // Simula el pago del trámite
    @PostMapping("/solicitud/{idSolicitud}/pagar")
    public Cobro pagar(@PathVariable Integer idSolicitud) {
        return cobroService.pagar(idSolicitud);
    }
}
