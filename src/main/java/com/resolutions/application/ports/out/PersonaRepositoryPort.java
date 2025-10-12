package com.resolutions.application.ports.out;

import com.resolutions.model.Persona;
import java.util.List;
import java.util.Optional;

public interface PersonaRepositoryPort {
    Integer save(Persona persona);
    Optional<Persona> findById(Integer personaId);
    List<Persona> findAll();
    void update(Integer personaId, Persona persona);
    void deleteById(Integer personaId);
    boolean existsById(Integer personaId);
}