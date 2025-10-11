package com.resolutions.application.useCases;

import com.resolutions.application.ports.in.MovimientoUseCase;
import com.resolutions.application.ports.out.MovimientoRepositoryPort;
import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.model.Movimiento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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