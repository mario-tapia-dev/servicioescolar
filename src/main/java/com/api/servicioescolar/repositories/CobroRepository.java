package com.api.servicioescolar.repositories;

import com.api.servicioescolar.models.Cobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CobroRepository extends JpaRepository<Cobro, Integer> {

    Optional<Cobro> findBySolicitudId(Integer idSolicitud);
}
