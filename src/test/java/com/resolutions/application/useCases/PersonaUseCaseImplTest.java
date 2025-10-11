package com.resolutions.application.useCases;

import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaUseCaseImplTest {

    @Mock
    private PersonaRepositoryPort personaRepository;

    private PersonaUseCaseImpl personaUseCase;

    private Persona persona;

    @BeforeEach
    void setUp() {
        personaUseCase = new PersonaUseCaseImpl();
        personaUseCase.personaRepository = personaRepository;
        persona = new Persona("PER001", "Juan Perez", "M", 30, "Calle 123", "555-1234");
    }

    @Test
    void testCreatePersona_Success() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn("PER001");

        // When
        String result = personaUseCase.createPersona(persona);

        // Then
        assertEquals("PER001", result);
        verify(personaRepository).existsById("PER001");
        verify(personaRepository).save(persona);
    }

    @Test
    void testCreatePersona_ThrowsException_WhenPersonaIdIsNull() {
        // Given
        persona.setPersonaId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaUseCase.createPersona(persona)
        );
        assertEquals("El ID de persona es requerido", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCreatePersona_ThrowsException_WhenPersonaIdIsEmpty() {
        // Given
        persona.setPersonaId("");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaUseCase.createPersona(persona)
        );
        assertEquals("El ID de persona es requerido", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCreatePersona_ThrowsException_WhenNombreIsNull() {
        // Given
        persona.setNombre(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaUseCase.createPersona(persona)
        );
        assertEquals("El nombre es requerido", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testCreatePersona_ThrowsException_WhenPersonaAlreadyExists() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personaUseCase.createPersona(persona)
        );
        assertEquals("Ya existe una persona con el ID: PER001", exception.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testGetPersonaById_Success() {
        // Given
        when(personaRepository.findById("PER001")).thenReturn(Optional.of(persona));

        // When
        Persona result = personaUseCase.getPersonaById("PER001");

        // Then
        assertNotNull(result);
        assertEquals("PER001", result.getPersonaId());
        assertEquals("Juan Perez", result.getNombre());
        verify(personaRepository).findById("PER001");
    }

    @Test
    void testGetPersonaById_ThrowsException_WhenNotFound() {
        // Given
        when(personaRepository.findById("PER999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> personaUseCase.getPersonaById("PER999")
        );
        assertEquals("Persona no encontrada con ID: PER999", exception.getMessage());
    }

    @Test
    void testGetAllPersonas_Success() {
        // Given
        List<Persona> personas = Arrays.asList(
            new Persona("PER001", "Juan Perez", "M", 30, "Calle 123", "555-1234"),
            new Persona("PER002", "Maria Garcia", "F", 25, "Calle 456", "555-5678")
        );
        when(personaRepository.findAll()).thenReturn(personas);

        // When
        List<Persona> result = personaUseCase.getAllPersonas();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan Perez", result.get(0).getNombre());
        assertEquals("Maria Garcia", result.get(1).getNombre());
        verify(personaRepository).findAll();
    }

    @Test
    void testUpdatePersona_Success() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(true);

        // When
        personaUseCase.updatePersona("PER001", persona);

        // Then
        verify(personaRepository).existsById("PER001");
        verify(personaRepository).update("PER001", persona);
        assertEquals("PER001", persona.getPersonaId());
    }

    @Test
    void testUpdatePersona_ThrowsException_WhenNotFound() {
        // Given
        when(personaRepository.existsById("PER999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> personaUseCase.updatePersona("PER999", persona)
        );
        assertEquals("Persona no encontrada con ID: PER999", exception.getMessage());
        verify(personaRepository, never()).update(anyString(), any());
    }

    @Test
    void testDeletePersona_Success() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(true);

        // When
        personaUseCase.deletePersona("PER001");

        // Then
        verify(personaRepository).existsById("PER001");
        verify(personaRepository).deleteById("PER001");
    }

    @Test
    void testDeletePersona_ThrowsException_WhenNotFound() {
        // Given
        when(personaRepository.existsById("PER999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> personaUseCase.deletePersona("PER999")
        );
        assertEquals("Persona no encontrada con ID: PER999", exception.getMessage());
        verify(personaRepository, never()).deleteById(anyString());
    }
}