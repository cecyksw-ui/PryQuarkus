package com.resolutions.adapters.out.persistence;

import com.resolutions.model.Persona;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PersonaJpaRepositoryTest {

    @Inject
    PersonaJpaRepository personaRepository;

    private Persona persona;

    @BeforeEach
    @Transactional
    void setUp() {
        persona = new Persona("PER_TEST_001", "Juan Test", "M", 30, "Calle Test 123", "555-TEST");
    }

    @Test
    @Transactional
    void testSave_Success() {
        // When
        String result = personaRepository.save(persona);

        // Then
        assertEquals("PER_TEST_001", result);
        assertTrue(personaRepository.existsById("PER_TEST_001"));
    }

    @Test
    @Transactional
    void testFindById_Success() {
        // Given
        personaRepository.save(persona);

        // When
        Optional<Persona> result = personaRepository.findById("PER_TEST_001");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Juan Test", result.get().getNombre());
        assertEquals("M", result.get().getGenero());
        assertEquals(30, result.get().getEdad());
    }

    @Test
    void testFindById_NotFound() {
        // When
        Optional<Persona> result = personaRepository.findById("PER_NOT_EXIST");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @Transactional
    void testFindAll_Success() {
        // Given
        personaRepository.save(persona);
        Persona persona2 = new Persona("PER_TEST_002", "Maria Test", "F", 25, "Calle Test 456", "555-TEST2");
        personaRepository.save(persona2);

        // When
        List<Persona> result = personaRepository.findAll();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getNombre().equals("Juan Test")));
        assertTrue(result.stream().anyMatch(p -> p.getNombre().equals("Maria Test")));
    }

    @Test
    @Transactional
    void testUpdate_Success() {
        // Given
        personaRepository.save(persona);
        
        // When
        persona.setNombre("Juan Actualizado");
        persona.setEdad(31);
        personaRepository.update("PER_TEST_001", persona);

        // Then
        Optional<Persona> updated = personaRepository.findById("PER_TEST_001");
        assertTrue(updated.isPresent());
        assertEquals("Juan Actualizado", updated.get().getNombre());
        assertEquals(31, updated.get().getEdad());
    }

    @Test
    @Transactional
    void testUpdate_NotFound() {
        // When & Then
        personaRepository.update("PER_NOT_EXIST", persona);
        
        // Should not throw exception but not update anything
        assertFalse(personaRepository.existsById("PER_NOT_EXIST"));
    }

    @Test
    @Transactional
    void testDeleteById_Success() {
        // Given
        personaRepository.save(persona);
        assertTrue(personaRepository.existsById("PER_TEST_001"));

        // When
        personaRepository.deleteById("PER_TEST_001");

        // Then
        assertFalse(personaRepository.existsById("PER_TEST_001"));
    }

    @Test
    @Transactional
    void testDeleteById_NotFound() {
        // When & Then - Should not throw exception
        personaRepository.deleteById("PER_NOT_EXIST");
        
        // Verify no side effects
        assertFalse(personaRepository.existsById("PER_NOT_EXIST"));
    }

    @Test
    @Transactional
    void testExistsById_True() {
        // Given
        personaRepository.save(persona);

        // When & Then
        assertTrue(personaRepository.existsById("PER_TEST_001"));
    }

    @Test
    void testExistsById_False() {
        // When & Then
        assertFalse(personaRepository.existsById("PER_NOT_EXIST"));
    }
}