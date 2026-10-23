package com.api.servicioescolar.repositories;

import com.api.servicioescolar.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    boolean existsByClave(String clave);
}
