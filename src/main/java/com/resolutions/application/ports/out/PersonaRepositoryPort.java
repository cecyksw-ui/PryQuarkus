package com.resolutions.application.ports.out;

import com.resolutions.model.Persona;
import java.util.List;
import java.util.Optional;

public interface PersonaRepositoryPort {
    String save(Persona persona);
    Optional<Persona> findById(String personaId);
    List<Persona> findAll();
    void update(String personaId, Persona persona);
    void deleteById(String personaId);
    boolean existsById(String personaId);
}