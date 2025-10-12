package com.resolutions.application.ports.in;

import com.resolutions.model.Persona;
import java.util.List;

public interface PersonaUseCase {
    Integer createPersona(Persona persona);
    Persona getPersonaById(Integer personaId);
    List<Persona> getAllPersonas();
    void updatePersona(Persona persona);
    void deletePersona(Integer personaId);
}