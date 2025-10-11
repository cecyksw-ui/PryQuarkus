package com.resolutions.application.ports.in;

import com.resolutions.model.Persona;
import java.util.List;

public interface PersonaUseCase {
    String createPersona(Persona persona);
    Persona getPersonaById(String personaId);
    List<Persona> getAllPersonas();
    void updatePersona(String personaId, Persona persona);
    void deletePersona(String personaId);
}