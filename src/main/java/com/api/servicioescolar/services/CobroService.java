package com.api.servicioescolar.services;

import com.api.servicioescolar.models.Cobro;
import com.api.servicioescolar.models.Solicitud;
import com.api.servicioescolar.repositories.CobroRepository;
import com.api.servicioescolar.repositories.SolicitudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CobroService {

    private static final Logger log = LoggerFactory.getLogger(CobroService.class);

    private final CobroRepository cobroRepository;
    private final SolicitudRepository solicitudRepository;

    public CobroService(CobroRepository cobroRepository, SolicitudRepository solicitudRepository) {
        this.cobroRepository = cobroRepository;
        this.solicitudRepository = solicitudRepository;
    }

    /** Crea el cobro PENDIENTE; el total depende del tipo de documento solicitado. */
    public Cobro crearCobro(Solicitud solicitud) {
        Cobro cobro = new Cobro(
                solicitud.getDocumentoSolicitado().getPrecio(),
                Cobro.Estado.PENDIENTE,
                solicitud);
        log.info("COBRO CREADO: solicitud {} por un total de {}", solicitud.getId(), cobro.getTotal());
        return cobroRepository.save(cobro);
    }

    public Cobro buscarPorSolicitud(Integer idSolicitud) {
        return cobroRepository.findBySolicitudId(idSolicitud)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe un cobro para la solicitud " + idSolicitud));
    }

    /** Simula el pago: cobro APROBADO y solicitud COBRO_APROBADO. */
    @Transactional
    public Cobro pagar(Integer idSolicitud) {
        Cobro cobro = buscarPorSolicitud(idSolicitud);

        if (cobro.getEstado() != Cobro.Estado.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El cobro no está pendiente, estado actual: " + cobro.getEstado());
        }

        cobro.setEstado(Cobro.Estado.APROBADO);
        cobroRepository.save(cobro);

        Solicitud solicitud = cobro.getSolicitud();
        solicitud.setEstado(Solicitud.Estado.COBRO_APROBADO);
        solicitudRepository.save(solicitud);

        log.info("COBRO APROBADO: solicitud {}", idSolicitud);
        return cobro;
    }

    /** Comprobación de que el cobro de la solicitud ya fue realizado. */
    public boolean estaAprobado(Integer idSolicitud) {
        return cobroRepository.findBySolicitudId(idSolicitud)
                .map(c -> c.getEstado() == Cobro.Estado.APROBADO)
                .orElse(false);
    }
}
