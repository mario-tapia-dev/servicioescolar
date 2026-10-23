package com.api.servicioescolar.controllers;

import com.api.servicioescolar.models.Estudiante;
import com.api.servicioescolar.services.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudiante crear(@RequestBody Estudiante estudiante) {
        return estudianteService.crear(estudiante);
    }

    @GetMapping
    public List<Estudiante> listar() {
        return estudianteService.listar();
    }

    @GetMapping("/{id}")
    public Estudiante buscarPorId(@PathVariable Integer id) {
        return estudianteService.buscarPorId(id);
    }
}
