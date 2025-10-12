package com.resolutions.application.useCases;

import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.model.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaUseCaseImplTest {

    @Mock
    private CuentaRepositoryPort cuentaRepository;

    private CuentaUseCaseImpl cuentaUseCase;

    private Cuenta cuenta1;
    private Cuenta cuenta2;
    private Cuenta cuenta3;
    private Cuenta cuenta4;
    private Cuenta cuenta5;

    @BeforeEach
    void setUp() {
        cuentaUseCase = new CuentaUseCaseImpl();
        cuentaUseCase.cuentaRepository = cuentaRepository;
        
        // Datos de prueba basados en los casos de uso
        // Cuenta 1: Cliente 1 (Jose Lema) - Ahorro
        cuenta1 = new Cuenta(1, "478758", "Ahorro", new BigDecimal("2000"), true, 1);
        
        // Cuenta 2: Cliente 2 (Marianela Montalvo) - Corriente
        cuenta2 = new Cuenta(2, "225487", "Corriente", new BigDecimal("100"), true, 2);
        
        // Cuenta 3: Cliente 3 (Juan Osorio) - Ahorros
        cuenta3 = new Cuenta(3, "495878", "Ahorros", new BigDecimal("0"), true, 3);
        
        // Cuenta 4: Cliente 2 (Marianela Montalvo) - Ahorros
        cuenta4 = new Cuenta(4, "496825", "Ahorros", new BigDecimal("540"), true, 2);
        
        // Cuenta 5: Nueva cuenta corriente para Jose Lema
        cuenta5 = new Cuenta(5, "585545", "Corriente", new BigDecimal("1000"), true, 1);
    }

    @Test
    void testCreateCuenta_JoseLemaAhorro_Success() {
        // Given - Cuenta de ahorro de Jose Lema
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(1);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta1);

        // Then
        assertEquals(1, result);
        verify(cuentaRepository).findByNumeroCuenta("478758");
        verify(cuentaRepository).save(cuenta1);
    }

    @Test
    void testCreateCuenta_MarianelaCorriente_Success() {
        // Given - Cuenta corriente de Marianela Montalvo
        when(cuentaRepository.findByNumeroCuenta("225487")).thenReturn(Optional.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(2);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta2);

        // Then
        assertEquals(2, result);
        verify(cuentaRepository).findByNumeroCuenta("225487");
        verify(cuentaRepository).save(cuenta2);
    }

    @Test
    void testCreateCuenta_JuanAhorros_Success() {
        // Given - Cuenta de ahorros de Juan Osorio con saldo inicial 0
        when(cuentaRepository.findByNumeroCuenta("495878")).thenReturn(Optional.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(3);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta3);

        // Then
        assertEquals(3, result);
        assertEquals(new BigDecimal("0"), cuenta3.getSaldoInicial());
        verify(cuentaRepository).findByNumeroCuenta("495878");
        verify(cuentaRepository).save(cuenta3);
    }

    @Test
    void testCreateCuenta_MarianellaSegundaCuenta_Success() {
        // Given - Segunda cuenta de Marianela (ahorros)
        when(cuentaRepository.findByNumeroCuenta("496825")).thenReturn(Optional.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(4);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta4);

        // Then
        assertEquals(4, result);
        assertEquals(new BigDecimal("540"), cuenta4.getSaldoInicial());
        assertEquals("Ahorros", cuenta4.getTipoCuenta());
        verify(cuentaRepository).findByNumeroCuenta("496825");
        verify(cuentaRepository).save(cuenta4);
    }

    @Test
    void testCreateCuenta_JoseLemaNuevaCorriente_Success() {
        // Given - Nueva cuenta corriente para Jose Lema
        when(cuentaRepository.findByNumeroCuenta("585545")).thenReturn(Optional.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(5);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta5);

        // Then
        assertEquals(5, result);
        assertEquals(new BigDecimal("1000"), cuenta5.getSaldoInicial());
        assertEquals("Corriente", cuenta5.getTipoCuenta());
        assertEquals(Integer.valueOf(1), cuenta5.getClienteId()); // Jose Lema
        verify(cuentaRepository).findByNumeroCuenta("585545");
        verify(cuentaRepository).save(cuenta5);
    }

    @Test
    void testCreateCuenta_NumeroCuentaExists_ThrowsException() {
        // Given
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta1));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> cuentaUseCase.createCuenta(cuenta1)
        );
        
        assertEquals("Ya existe una cuenta con el número: 478758", exception.getMessage());
        verify(cuentaRepository).findByNumeroCuenta("478758");
        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void testGetCuentaById_Success() {
        // Given
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta1));

        // When
        Cuenta result = cuentaUseCase.getCuentaById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCuentaId());
        assertEquals("478758", result.getNumeroCuenta());
        assertEquals("Ahorro", result.getTipoCuenta());
        assertEquals(new BigDecimal("2000"), result.getSaldoInicial());
        assertTrue(result.getEstado());
        verify(cuentaRepository).findById(1);
    }

    @Test
    void testGetCuentasByClienteId_MarianelaDosCC() {
        // Given - Marianela tiene dos cuentas
        List<Cuenta> expectedCuentas = Arrays.asList(cuenta2, cuenta4);
        when(cuentaRepository.findByClienteId(2)).thenReturn(expectedCuentas);

        // When
        List<Cuenta> result = cuentaUseCase.getCuentasByClienteId(2);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("225487", result.get(0).getNumeroCuenta()); // Corriente
        assertEquals("496825", result.get(1).getNumeroCuenta()); // Ahorros
        verify(cuentaRepository).findByClienteId(2);
    }

    @Test
    void testGetAllCuentas_ReturnsFiveCuentas() {
        // Given
        List<Cuenta> expectedCuentas = Arrays.asList(cuenta1, cuenta2, cuenta3, cuenta4, cuenta5);
        when(cuentaRepository.findAll()).thenReturn(expectedCuentas);

        // When
        List<Cuenta> result = cuentaUseCase.getAllCuentas();

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(cuentaRepository).findAll();
    }

    @Test
    void testUpdateCuenta_Success() {
        // Given
        Cuenta cuentaActualizada = new Cuenta(1, "478758", "Ahorro", new BigDecimal("2500"), true, 1);
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta1));

        // When
        cuentaUseCase.updateCuenta(1, cuentaActualizada);

        // Then
        verify(cuentaRepository).findById(1);
        verify(cuentaRepository).update(1, cuentaActualizada);
    }

    @Test
    void testDeleteCuenta_Success() {
        // Given
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta1));

        // When
        cuentaUseCase.deleteCuenta(1);

        // Then
        verify(cuentaRepository).findById(1);
        verify(cuentaRepository).deleteById(1);
    }

    @Test
    void testCreateCuenta_NullNumeroCuenta_ThrowsException() {
        // Given
        Cuenta cuentaInvalida = new Cuenta(null, null, "Ahorro", new BigDecimal("1000"), true, 1);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> cuentaUseCase.createCuenta(cuentaInvalida)
        );
        
        assertEquals("El número de cuenta es requerido", exception.getMessage());
        verifyNoInteractions(cuentaRepository);
    }

    @Test
    void testCreateCuenta_NullClienteId_ThrowsException() {
        // Given
        Cuenta cuentaInvalida = new Cuenta(null, "123456", "Ahorro", new BigDecimal("1000"), true, null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> cuentaUseCase.createCuenta(cuentaInvalida)
        );
        
        assertEquals("El ID del cliente es requerido", exception.getMessage());
        verifyNoInteractions(cuentaRepository);
    }
}