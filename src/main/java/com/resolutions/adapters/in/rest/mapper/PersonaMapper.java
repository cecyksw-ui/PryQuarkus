package com.resolutions.adapters.in.rest.mapper;

import com.resolutions.adapters.in.rest.dto.PersonaDto;
import com.resolutions.model.Persona;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonaMapper {

    public PersonaDto toDto(Persona persona) {
        if (persona == null) {
            return null;
        }
        return new PersonaDto(
                persona.getPersonaId(),
                persona.getNombre(),
                persona.getGenero(),
                persona.getEdad(),
                persona.getDireccion(),
                persona.getTelefono()
        );
    }

    public Persona toEntity(PersonaDto dto) {
        if (dto == null) {
            return null;
        }
        return new Persona(
                dto.getPersonaId(),
                dto.getNombre(),
                dto.getGenero(),
                dto.getEdad(),
                dto.getDireccion(),
                dto.getTelefono()
        );
    }
}