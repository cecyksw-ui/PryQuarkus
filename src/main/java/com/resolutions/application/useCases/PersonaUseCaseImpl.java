package com.resolutions.application.useCases;

import com.resolutions.application.ports.in.PersonaUseCase;
import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.Persona;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PersonaUseCaseImpl implements PersonaUseCase {

    @Inject
    PersonaRepositoryPort personaRepository;

    @Override
    public Integer createPersona(Persona persona) {
        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        // No validar ID porque se auto-genera
        return personaRepository.save(persona);
    }

    @Override
    public Persona getPersonaById(Integer personaId) {
        return personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + personaId));
    }

    @Override
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    @Override
    public void updatePersona(Persona persona) {
        if (persona.getPersonaId() == null) {
            throw new IllegalArgumentException("El ID de persona es requerido para actualizar");
        }
        if (!personaRepository.existsById(persona.getPersonaId())) {
            throw new RuntimeException("Persona no encontrada con ID: " + persona.getPersonaId());
        }
        personaRepository.update(persona.getPersonaId(), persona);
    }

    @Override
    public void deletePersona(Integer personaId) {
        if (!personaRepository.existsById(personaId)) {
            throw new RuntimeException("Persona no encontrada con ID: " + personaId);
        }
        personaRepository.deleteById(personaId);
    }
}