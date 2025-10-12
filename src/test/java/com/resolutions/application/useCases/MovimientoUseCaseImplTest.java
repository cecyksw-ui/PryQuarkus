package com.resolutions.application.useCases;

import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.application.ports.out.MovimientoRepositoryPort;
import com.resolutions.model.Cuenta;
import com.resolutions.model.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoUseCaseImplTest {

    @Mock
    private MovimientoRepositoryPort movimientoRepository;

    @Mock
    private CuentaRepositoryPort cuentaRepository;

    private MovimientoUseCaseImpl movimientoUseCase;

    private Cuenta cuentaAhorro;
    private Cuenta cuentaCorriente;
    private Cuenta cuentaAhorrosJuan;
    private Cuenta cuentaAhorrosMarianela;

    @BeforeEach
    void setUp() {
        movimientoUseCase = new MovimientoUseCaseImpl();
        movimientoUseCase.movimientoRepository = movimientoRepository;
        movimientoUseCase.cuentaRepository = cuentaRepository;
        
        // Datos de prueba basados en los casos de uso
        // Cuenta 1: Jose Lema - Ahorro (478758) - Saldo inicial 2000
        cuentaAhorro = new Cuenta(1, "478758", "Ahorro", new BigDecimal("2000"), true, 1);
        
        // Cuenta 2: Marianela Montalvo - Corriente (225487) - Saldo inicial 100
        cuentaCorriente = new Cuenta(2, "225487", "Corriente", new BigDecimal("100"), true, 2);
        
        // Cuenta 3: Juan Osorio - Ahorros (495878) - Saldo inicial 0
        cuentaAhorrosJuan = new Cuenta(3, "495878", "Ahorros", new BigDecimal("0"), true, 3);
        
        // Cuenta 4: Marianela Montalvo - Ahorros (496825) - Saldo inicial 540
        cuentaAhorrosMarianela = new Cuenta(4, "496825", "Ahorros", new BigDecimal("540"), true, 2);
    }

    @Test
    void testRetiro575_CuentaAhorro_Success() {
        // Given - Retiro de 575 de cuenta de ahorro (saldo: 2000 -> 1425)
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuentaAhorro));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(1);

        // When
        Integer result = movimientoUseCase.createMovimientoConValidacion("Debito", new BigDecimal("575"), 1);

        // Then
        assertEquals(1, result);
        verify(cuentaRepository).findById(1);
        verify(movimientoRepository).save(any(Movimiento.class));
        
        // Verificar que se guardó el movimiento correcto
        verify(movimientoRepository).save(argThat(movimiento -> 
            movimiento.getTipoMovimiento().equals("Debito") &&
            movimiento.getValor().compareTo(new BigDecimal("-575")) == 0 &&
            movimiento.getSaldo().compareTo(new BigDecimal("1425")) == 0 &&
            movimiento.getCuentaId().equals(1)
        ));
    }

    @Test
    void testDeposito600_CuentaCorriente_Success() {
        // Given - Depósito de 600 en cuenta corriente (saldo: 100 -> 700)
        when(cuentaRepository.findById(2)).thenReturn(Optional.of(cuentaCorriente));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(2);

        // When
        Integer result = movimientoUseCase.createMovimientoConValidacion("Credito", new BigDecimal("600"), 2);

        // Then
        assertEquals(2, result);
        verify(cuentaRepository).findById(2);
        verify(movimientoRepository).save(any(Movimiento.class));
        
        // Verificar que se guardó el movimiento correcto
        verify(movimientoRepository).save(argThat(movimiento -> 
            movimiento.getTipoMovimiento().equals("Credito") &&
            movimiento.getValor().compareTo(new BigDecimal("600")) == 0 &&
            movimiento.getSaldo().compareTo(new BigDecimal("700")) == 0 &&
            movimiento.getCuentaId().equals(2)
        ));
    }

    @Test
    void testDeposito150_CuentaAhorrosJuan_Success() {
        // Given - Depósito de 150 en cuenta de ahorros de Juan (saldo: 0 -> 150)
        when(cuentaRepository.findById(3)).thenReturn(Optional.of(cuentaAhorrosJuan));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(3);

        // When
        Integer result = movimientoUseCase.createMovimientoConValidacion("Credito", new BigDecimal("150"), 3);

        // Then
        assertEquals(3, result);
        verify(cuentaRepository).findById(3);
        verify(movimientoRepository).save(any(Movimiento.class));
        
        // Verificar que se guardó el movimiento correcto
        verify(movimientoRepository).save(argThat(movimiento -> 
            movimiento.getTipoMovimiento().equals("Credito") &&
            movimiento.getValor().compareTo(new BigDecimal("150")) == 0 &&
            movimiento.getSaldo().compareTo(new BigDecimal("150")) == 0 &&
            movimiento.getCuentaId().equals(3)
        ));
    }

    @Test
    void testRetiro540_CuentaAhorrosMarianela_Success() {
        // Given - Retiro de 540 en cuenta de ahorros de Marianela (saldo: 540 -> 0)
        when(cuentaRepository.findById(4)).thenReturn(Optional.of(cuentaAhorrosMarianela));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(4);

        // When
        Integer result = movimientoUseCase.createMovimientoConValidacion("Debito", new BigDecimal("540"), 4);

        // Then
        assertEquals(4, result);
        verify(cuentaRepository).findById(4);
        verify(movimientoRepository).save(any(Movimiento.class));
        
        // Verificar que se guardó el movimiento correcto
        verify(movimientoRepository).save(argThat(movimiento -> 
            movimiento.getTipoMovimiento().equals("Debito") &&
            movimiento.getValor().compareTo(new BigDecimal("-540")) == 0 &&
            movimiento.getSaldo().compareTo(new BigDecimal("0")) == 0 &&
            movimiento.getCuentaId().equals(4)
        ));
    }

    @Test
    void testRetiroSaldoInsuficiente_ThrowsException() {
        // Given - Intentar retirar más dinero del disponible
        when(cuentaRepository.findById(3)).thenReturn(Optional.of(cuentaAhorrosJuan)); // Saldo: 0

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> movimientoUseCase.createMovimientoConValidacion("Debito", new BigDecimal("100"), 3)
        );
        
        assertEquals("Saldo insuficiente para realizar el retiro", exception.getMessage());
        verify(cuentaRepository).findById(3);
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void testMovimientoCuentaNoExiste_ThrowsException() {
        // Given
        when(cuentaRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> movimientoUseCase.createMovimientoConValidacion("Credito", new BigDecimal("100"), 999)
        );
        
        assertEquals("Cuenta no encontrada con ID: 999", exception.getMessage());
        verify(cuentaRepository).findById(999);
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void testMovimientoMontoNegativo_ThrowsException() {
        // Given
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuentaAhorro));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> movimientoUseCase.createMovimientoConValidacion("Credito", new BigDecimal("-100"), 1)
        );
        
        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
        verify(cuentaRepository).findById(1);
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void testMovimientoTipoInvalido_ThrowsException() {
        // Given
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuentaAhorro));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> movimientoUseCase.createMovimientoConValidacion("TipoInvalido", new BigDecimal("100"), 1)
        );
        
        assertEquals("Tipo de movimiento no válido: TipoInvalido", exception.getMessage());
        verify(cuentaRepository).findById(1);
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void testCrearMovimiento_CuentaInactiva_ThrowsException() {
        // Given - Cuenta inactiva
        Cuenta cuentaInactiva = new Cuenta(1, "478758", "Ahorro", new BigDecimal("2000"), false, 1);
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuentaInactiva));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> movimientoUseCase.createMovimientoConValidacion("Credito", new BigDecimal("100"), 1)
        );
        
        assertEquals("La cuenta no está activa", exception.getMessage());
        verify(cuentaRepository).findById(1);
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void testGetMovimientoById_Success() {
        // Given
        Movimiento movimiento = new Movimiento(1, LocalDate.now(), "Credito", new BigDecimal("100"), new BigDecimal("200"), 1);
        when(movimientoRepository.findById(1)).thenReturn(Optional.of(movimiento));

        // When
        Movimiento result = movimientoUseCase.getMovimientoById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getMovimientoId());
        assertEquals("Credito", result.getTipoMovimiento());
        assertEquals(new BigDecimal("100"), result.getValor());
        verify(movimientoRepository).findById(1);
    }
}