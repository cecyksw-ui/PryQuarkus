package com.resolutions.application.useCases;

import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.model.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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

/**
 * Pruebas unitarias para CuentaUseCaseImpl
 * Valida toda la lógica de negocio para operaciones CRUD de cuentas
 */
@ExtendWith(MockitoExtension.class)
class CuentaUseCaseImplTest {

    @Mock
    private CuentaRepositoryPort cuentaRepository;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    private CuentaUseCaseImpl cuentaUseCase;

    private Cuenta cuenta1;
    private Cuenta cuenta2;
    private Cuenta cuenta3;
    private Cuenta cuenta4;

    @BeforeEach
    void setUp() {
        cuentaUseCase = new CuentaUseCaseImpl();
        cuentaUseCase.cuentaRepository = cuentaRepository;
        cuentaUseCase.clienteRepository = clienteRepository;
        
        // Datos de prueba basados en los casos de uso
        // Cuenta 1: Cliente 1 (Jose Lema) - Ahorro
        cuenta1 = new Cuenta(1, "478758", "AHORRO", new BigDecimal("2000.00"), true, 1);
        
        // Cuenta 2: Cliente 2 (Marianela Montalvo) - Corriente
        cuenta2 = new Cuenta(2, "225487", "CORRIENTE", new BigDecimal("2000.00"), true, 2);
        
        // Cuenta 3: Cliente 3 (Juan Osorio) - Ahorros
        cuenta3 = new Cuenta(3, "495878", "AHORRO", new BigDecimal("0.00"), true, 3);
        
        // Cuenta 4: Cliente 3 (Juan Osorio) - Corriente
        cuenta4 = new Cuenta(4, "496825", "CORRIENTE", new BigDecimal("540.00"), true, 3);
    }

    @Test
    @DisplayName("Crear cuenta ahorro Jose Lema exitosamente")
    void testCreateCuenta_JoseLemaAhorro_Success() {
        // Given - Cuenta de ahorro de Jose Lema
        when(clienteRepository.existsById(1)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(1);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta1);

        // Then
        assertEquals(1, result);
        verify(clienteRepository).existsById(1);
        verify(cuentaRepository).existsByNumeroCuenta("478758");
        verify(cuentaRepository).save(cuenta1);
    }

    @Test
    @DisplayName("Crear cuenta corriente Marianela exitosamente")
    void testCreateCuenta_MarianelaCorriente_Success() {
        // Given - Cuenta corriente de Marianela Montalvo
        when(clienteRepository.existsById(2)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("225487")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(2);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta2);

        // Then
        assertEquals(2, result);
        assertEquals("CORRIENTE", cuenta2.getTipoCuenta());
        verify(clienteRepository).existsById(2);
        verify(cuentaRepository).existsByNumeroCuenta("225487");
        verify(cuentaRepository).save(cuenta2);
    }

    @Test
    @DisplayName("Crear cuenta ahorro Juan Osorio con saldo cero")
    void testCreateCuenta_JuanAhorros_Success() {
        // Given - Cuenta de ahorros de Juan Osorio con saldo inicial 0
        when(clienteRepository.existsById(3)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("495878")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(3);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta3);

        // Then
        assertEquals(3, result);
        assertEquals(new BigDecimal("0.00"), cuenta3.getSaldoInicial());
        assertEquals("AHORRO", cuenta3.getTipoCuenta());
        verify(clienteRepository).existsById(3);
        verify(cuentaRepository).existsByNumeroCuenta("495878");
        verify(cuentaRepository).save(cuenta3);
    }

    @Test
    @DisplayName("Crear cuenta corriente Juan Osorio exitosamente")
    void testCreateCuenta_JuanCorriente_Success() {
        // Given - Cuenta corriente de Juan Osorio
        when(clienteRepository.existsById(3)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("496825")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(4);

        // When
        Integer result = cuentaUseCase.createCuenta(cuenta4);

        // Then
        assertEquals(4, result);
        assertEquals(new BigDecimal("540.00"), cuenta4.getSaldoInicial());
        assertEquals("CORRIENTE", cuenta4.getTipoCuenta());
        assertEquals(Integer.valueOf(3), cuenta4.getClienteId()); // Juan Osorio
        verify(clienteRepository).existsById(3);
        verify(cuentaRepository).existsByNumeroCuenta("496825");
        verify(cuentaRepository).save(cuenta4);
    }

    @Test
    @DisplayName("Error al crear cuenta - Número de cuenta ya existe")
    void testCreateCuenta_NumeroCuentaExists_ThrowsException() {
        // Given
        when(clienteRepository.existsById(1)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> cuentaUseCase.createCuenta(cuenta1)
        );
        
        assertEquals("Ya existe una cuenta con número: 478758", exception.getMessage());
        verify(clienteRepository).existsById(1);
        verify(cuentaRepository).existsByNumeroCuenta("478758");
        verify(cuentaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Error al crear cuenta - Número de cuenta nulo")
    void testCreateCuenta_NullNumeroCuenta_ThrowsException() {
        // Given
        Cuenta cuentaInvalida = new Cuenta(null, null, "AHORRO", new BigDecimal("1000"), true, 1);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> cuentaUseCase.createCuenta(cuentaInvalida)
        );
        
        assertEquals("El número de cuenta es requerido", exception.getMessage());
        verifyNoInteractions(cuentaRepository);
    }

    @Test
    @DisplayName("Error al crear cuenta - ID de cliente nulo")
    void testCreateCuenta_NullClienteId_ThrowsException() {
        // Given
        Cuenta cuentaInvalida = new Cuenta(null, "123456", "AHORRO", new BigDecimal("1000"), true, null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> cuentaUseCase.createCuenta(cuentaInvalida)
        );
        
        assertEquals("El ID del cliente es requerido", exception.getMessage());
        verifyNoInteractions(cuentaRepository);
    }

    @Test
    @DisplayName("Obtener cuenta por ID exitosamente")
    void testGetCuentaById_Success() {
        // Given
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta1));

        // When
        Cuenta result = cuentaUseCase.getCuentaById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCuentaId());
        assertEquals("478758", result.getNumeroCuenta());
        assertEquals("AHORRO", result.getTipoCuenta());
        assertEquals(new BigDecimal("2000.00"), result.getSaldoInicial());
        assertTrue(result.getEstado());
        verify(cuentaRepository).findById(1);
    }

    @Test
    @DisplayName("Error al obtener cuenta - No encontrada")
    void testGetCuentaById_NotFound_ThrowsException() {
        // Given
        when(cuentaRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> cuentaUseCase.getCuentaById(999)
        );

        assertEquals("Cuenta no encontrada con ID: 999", exception.getMessage());
        verify(cuentaRepository).findById(999);
    }

    @Test
    @DisplayName("Obtener cuentas por cliente ID - Juan Osorio con dos cuentas")
    void testGetCuentasByClienteId_JuanDosCuentas() {
        // Given - Juan Osorio tiene dos cuentas
        List<Cuenta> expectedCuentas = Arrays.asList(cuenta3, cuenta4);
        when(cuentaRepository.findByClienteId(3)).thenReturn(expectedCuentas);

        // When
        List<Cuenta> result = cuentaUseCase.getCuentasByClienteId(3);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("495878", result.get(0).getNumeroCuenta()); // Ahorro
        assertEquals("496825", result.get(1).getNumeroCuenta()); // Corriente
        verify(cuentaRepository).findByClienteId(3);
    }

    @Test
    @DisplayName("Obtener todas las cuentas exitosamente")
    void testGetAllCuentas_ReturnsCuatroCuentas() {
        // Given
        List<Cuenta> expectedCuentas = Arrays.asList(cuenta1, cuenta2, cuenta3, cuenta4);
        when(cuentaRepository.findAll()).thenReturn(expectedCuentas);

        // When
        List<Cuenta> result = cuentaUseCase.getAllCuentas();

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        verify(cuentaRepository).findAll();
    }

    @Test
    @DisplayName("Actualizar cuenta exitosamente")
    void testUpdateCuenta_Success() {
        // Given
        Cuenta cuentaActualizada = new Cuenta(1, "478758", "AHORRO", new BigDecimal("2500.00"), true, 1);
        when(cuentaRepository.existsById(1)).thenReturn(true);

        // When
        cuentaUseCase.updateCuenta(1, cuentaActualizada);

        // Then
        verify(cuentaRepository).existsById(1);
        verify(cuentaRepository).update(1, cuentaActualizada);
    }

    @Test
    @DisplayName("Error al actualizar cuenta - No encontrada")
    void testUpdateCuenta_NotFound_ThrowsException() {
        // Given
        Cuenta cuentaActualizada = new Cuenta(999, "999999", "AHORRO", new BigDecimal("1000.00"), true, 1);
        when(cuentaRepository.existsById(999)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> cuentaUseCase.updateCuenta(999, cuentaActualizada)
        );

        assertEquals("Cuenta no encontrada con ID: 999", exception.getMessage());
        verify(cuentaRepository).existsById(999);
        verify(cuentaRepository, never()).update(anyInt(), any());
    }

    @Test
    @DisplayName("Eliminar cuenta exitosamente")
    void testDeleteCuenta_Success() {
        // Given
        when(cuentaRepository.existsById(1)).thenReturn(true);

        // When
        cuentaUseCase.deleteCuenta(1);

        // Then
        verify(cuentaRepository).existsById(1);
        verify(cuentaRepository).deleteById(1);
    }

    @Test
    @DisplayName("Error al eliminar cuenta - No encontrada")
    void testDeleteCuenta_NotFound_ThrowsException() {
        // Given
        when(cuentaRepository.existsById(999)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> cuentaUseCase.deleteCuenta(999)
        );

        assertEquals("Cuenta no encontrada con ID: 999", exception.getMessage());
        verify(cuentaRepository).existsById(999);
        verify(cuentaRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Crear cuenta con saldo inicial por defecto")
    void testCreateCuenta_DefaultSaldoInicial() {
        // Given
        Cuenta cuentaSinSaldo = new Cuenta(null, "123456", "AHORRO", null, true, 1);
        when(clienteRepository.existsById(1)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("123456")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(5);

        // When
        Integer result = cuentaUseCase.createCuenta(cuentaSinSaldo);

        // Then
        assertEquals(5, result);
        // El saldo inicial debería ser 0 por defecto si es null
        verify(clienteRepository).existsById(1);
        verify(cuentaRepository).existsByNumeroCuenta("123456");
        verify(cuentaRepository).save(cuentaSinSaldo);
    }
}