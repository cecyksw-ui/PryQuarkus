package com.resolutions.application.useCases;

import com.resolutions.application.ports.in.MovimientoUseCase;
import com.resolutions.application.ports.out.MovimientoRepositoryPort;
import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.model.Movimiento;
import com.resolutions.model.Cuenta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class MovimientoUseCaseImpl implements MovimientoUseCase {

    @Inject
    MovimientoRepositoryPort movimientoRepository;

    @Inject
    CuentaRepositoryPort cuentaRepository;

    @Override
    public Integer createMovimiento(Movimiento movimiento) {
        if (movimiento.getCuentaId() == null) {
            throw new IllegalArgumentException("El ID de la cuenta es requerido");
        }
        if (movimiento.getTipoMovimiento() == null || movimiento.getTipoMovimiento().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento es requerido");
        }
        if (movimiento.getValor() == null) {
            throw new IllegalArgumentException("El valor es requerido");
        }
        if (movimiento.getSaldo() == null) {
            throw new IllegalArgumentException("El saldo es requerido");
        }
        if (!cuentaRepository.existsById(movimiento.getCuentaId())) {
            throw new IllegalArgumentException("No existe una cuenta con ID: " + movimiento.getCuentaId());
        }
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDate.now());
        }
        return movimientoRepository.save(movimiento);
    }
    
    @Override
    public Integer createMovimientoConValidacion(String tipoMovimiento, BigDecimal monto, Integer cuentaId) {
        // Validaciones básicas
        if (cuentaId == null) {
            throw new IllegalArgumentException("El ID de la cuenta es requerido");
        }
        if (tipoMovimiento == null || tipoMovimiento.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento es requerido");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (!cuentaRepository.existsById(cuentaId)) {
            throw new IllegalArgumentException("No existe una cuenta con ID: " + cuentaId);
        }
        
        // Obtener saldo actual
        BigDecimal saldoActual = getSaldoActualByCuentaId(cuentaId);
        
        // Validar débito si es necesario
        if (Movimiento.TipoMovimiento.DEBITO.getValor().equalsIgnoreCase(tipoMovimiento)) {
            Movimiento.validarDebito(saldoActual, monto);
        }
        
        // Crear movimiento con cálculo automático
        Movimiento movimiento = new Movimiento(tipoMovimiento, monto, saldoActual, cuentaId);
        
        return movimientoRepository.save(movimiento);
    }
    
    @Override
    public BigDecimal getSaldoActualByCuentaId(Integer cuentaId) {
        // Intentar obtener saldo del último movimiento
        BigDecimal saldoMovimientos = movimientoRepository.getSaldoActualByCuentaId(cuentaId);
        if (saldoMovimientos != null) {
            return saldoMovimientos;
        }
        
        // Si no hay movimientos, usar saldo inicial de la cuenta
        return cuentaRepository.findById(cuentaId)
                .map(Cuenta::getSaldoInicial)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Movimiento getMovimientoById(Integer movimientoId) {
        return movimientoRepository.findById(movimientoId)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + movimientoId));
    }

    @Override
    public List<Movimiento> getAllMovimientos() {
        return movimientoRepository.findAll();
    }

    @Override
    public void updateMovimiento(Integer movimientoId, Movimiento movimiento) {
        if (!movimientoRepository.existsById(movimientoId)) {
            throw new RuntimeException("Movimiento no encontrado con ID: " + movimientoId);
        }
        movimiento.setMovimientoId(movimientoId);
        movimientoRepository.update(movimientoId, movimiento);
    }

    @Override
    public void deleteMovimiento(Integer movimientoId) {
        if (!movimientoRepository.existsById(movimientoId)) {
            throw new RuntimeException("Movimiento no encontrado con ID: " + movimientoId);
        }
        movimientoRepository.deleteById(movimientoId);
    }

    @Override
    public List<Movimiento> getMovimientosByCuentaId(Integer cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> getMovimientosByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return movimientoRepository.findByFechaRange(fechaInicio, fechaFin);
    }
}