package com.api.servicioescolar.services;

import com.api.servicioescolar.models.Estudiante;
import com.api.servicioescolar.repositories.EstudianteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public Estudiante crear(Estudiante estudiante) {
        estudiante.setId(null);
        return estudianteRepository.save(estudiante);
    }

    public List<Estudiante> listar() {
        return estudianteRepository.findAll();
    }

    public Estudiante buscarPorId(Integer id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe el estudiante con id " + id));
    }
}
