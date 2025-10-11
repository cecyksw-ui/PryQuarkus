package com.resolutions.application.ports.out;

import com.resolutions.model.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepositoryPort {
    Integer save(Movimiento movimiento);
    Optional<Movimiento> findById(Integer movimientoId);
    List<Movimiento> findAll();
    void update(Integer movimientoId, Movimiento movimiento);
    void deleteById(Integer movimientoId);
    boolean existsById(Integer movimientoId);
    List<Movimiento> findByCuentaId(Integer cuentaId);
    List<Movimiento> findByFechaRange(LocalDate fechaInicio, LocalDate fechaFin);
    List<Movimiento> findByCuentaIdAndFechaRange(Integer cuentaId, LocalDate fechaInicio, LocalDate fechaFin);
    Optional<Movimiento> findLastMovimientoByCuentaId(Integer cuentaId);
    BigDecimal getSaldoActualByCuentaId(Integer cuentaId);
    List<Movimiento> findByClienteIdAndFechaRange(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}