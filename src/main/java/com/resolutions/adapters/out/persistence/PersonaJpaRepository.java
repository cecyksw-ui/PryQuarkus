package com.resolutions.adapters.out.persistence;

import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.Persona;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonaJpaRepository implements PersonaRepositoryPort {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public Integer save(Persona persona) {
        entityManager.persist(persona);
        entityManager.flush(); // Forzar flush para obtener el ID generado
        return persona.getPersonaId();
    }

    @Override
    public Optional<Persona> findById(Integer personaId) {
        Persona persona = entityManager.find(Persona.class, personaId);
        return Optional.ofNullable(persona);
    }

    @Override
    public List<Persona> findAll() {
        return entityManager.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Integer personaId, Persona persona) {
        Persona existingPersona = entityManager.find(Persona.class, personaId);
        if (existingPersona != null) {
            existingPersona.setNombre(persona.getNombre());
            existingPersona.setGenero(persona.getGenero());
            existingPersona.setEdad(persona.getEdad());
            existingPersona.setDireccion(persona.getDireccion());
            existingPersona.setTelefono(persona.getTelefono());
            entityManager.merge(existingPersona);
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer personaId) {
        Persona persona = entityManager.find(Persona.class, personaId);
        if (persona != null) {
            entityManager.remove(persona);
        }
    }

    @Override
    public boolean existsById(Integer personaId) {
        return findById(personaId).isPresent();
    }
}