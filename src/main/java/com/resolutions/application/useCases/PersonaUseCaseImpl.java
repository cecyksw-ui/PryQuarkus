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
    public String createPersona(Persona persona) {
        if (persona.getPersonaId() == null || persona.getPersonaId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de persona es requerido");
        }
        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (personaRepository.existsById(persona.getPersonaId())) {
            throw new IllegalArgumentException("Ya existe una persona con el ID: " + persona.getPersonaId());
        }
        return personaRepository.save(persona);
    }

    @Override
    public Persona getPersonaById(String personaId) {
        return personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + personaId));
    }

    @Override
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    @Override
    public void updatePersona(String personaId, Persona persona) {
        if (!personaRepository.existsById(personaId)) {
            throw new RuntimeException("Persona no encontrada con ID: " + personaId);
        }
        persona.setPersonaId(personaId);
        personaRepository.update(personaId, persona);
    }

    @Override
    public void deletePersona(String personaId) {
        if (!personaRepository.existsById(personaId)) {
            throw new RuntimeException("Persona no encontrada con ID: " + personaId);
        }
        personaRepository.deleteById(personaId);
    }
}