package com.api.servicioescolar.services;

import com.api.servicioescolar.models.Estudiante;
import com.api.servicioescolar.models.Solicitud;
import com.api.servicioescolar.repositories.SolicitudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class SolicitudService {

    private static final Logger log = LoggerFactory.getLogger(SolicitudService.class);

    private final SolicitudRepository solicitudRepository;
    private final EstudianteService estudianteService;
    private final CobroService cobroService;

    public SolicitudService(SolicitudRepository solicitudRepository,
                            EstudianteService estudianteService,
                            CobroService cobroService) {
        this.solicitudRepository = solicitudRepository;
        this.estudianteService = estudianteService;
        this.cobroService = cobroService;
    }

    public Solicitud buscarPorId(Integer id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la solicitud con id " + id));
    }

    /** Paso 1: verifica inscripción, crea la solicitud (CREADA) y su cobro (PENDIENTE). */
    @Transactional
    public Solicitud crear(Integer idEstudiante, Solicitud.Documento documento) {
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);

        if (!Boolean.TRUE.equals(estudiante.getInscripcion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El estudiante no está inscrito, no puede solicitar documentos");
        }

        Solicitud solicitud = new Solicitud(documento, LocalDate.now(), Solicitud.Estado.CREADA, estudiante);
        solicitud = solicitudRepository.save(solicitud);

        cobroService.crearCobro(solicitud);
        log.info("SOLICITUD CREADA: id {} ({}) para la matrícula {}",
                solicitud.getId(), documento, estudiante.getMatricula());
        return solicitud;
    }

    /**
     * Paso 3: comprueba el cobro, genera el PDF (representativo), genera la clave única
     * y la "envía" por correo (representativo). Solicitud pasa a CORREO_ENVIADO.
     */
    @Transactional
    public Solicitud generarPdfYEnviarClave(Integer idSolicitud) {
        Solicitud solicitud = buscarPorId(idSolicitud);

        if (solicitud.getEstado() != Solicitud.Estado.COBRO_APROBADO
                || !cobroService.estaAprobado(idSolicitud)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede continuar: el cobro de la solicitud no está aprobado "
                            + "o la solicitud ya avanzó de estado (actual: " + solicitud.getEstado() + ")");
        }

        log.info("PDF GENERADO: {} de la matrícula {}",
                solicitud.getDocumentoSolicitado(), solicitud.getEstudiante().getMatricula());

        String clave = generarClaveUnica();
        solicitud.setClave(clave);
        solicitud.setEstado(Solicitud.Estado.CORREO_ENVIADO);
        solicitud = solicitudRepository.save(solicitud);

        log.info("CORREO ENVIADO: a la matrícula {} con la clave de cierre: {}",
                solicitud.getEstudiante().getMatricula(), clave);
        return solicitud;
    }

    /** Paso 4: valida la clave, cierra la solicitud y "descarga" el PDF (representativo). */
    @Transactional
    public Solicitud cerrar(Integer idSolicitud, String clave) {
        Solicitud solicitud = buscarPorId(idSolicitud);

        if (solicitud.getEstado() != Solicitud.Estado.CORREO_ENVIADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La solicitud no está lista para cerrarse, estado actual: " + solicitud.getEstado());
        }

        if (clave == null || !clave.trim().equalsIgnoreCase(solicitud.getClave())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La clave es incorrecta");
        }

        solicitud.setEstado(Solicitud.Estado.SOLICITUD_CERRADA);
        solicitud = solicitudRepository.save(solicitud);

        log.info("SOLICITUD CERRADA: id {}", solicitud.getId());
        log.info("PDF DESCARGADO: {} de la matrícula {}",
                solicitud.getDocumentoSolicitado(), solicitud.getEstudiante().getMatricula());
        return solicitud;
    }

    private String generarClaveUnica() {
        String clave;
        do {
            clave = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (solicitudRepository.existsByClave(clave));
        return clave;
    }
}
