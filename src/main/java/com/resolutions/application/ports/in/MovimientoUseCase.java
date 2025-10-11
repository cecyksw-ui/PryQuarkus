package com.resolutions.application.ports.in;

import com.resolutions.model.Movimiento;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoUseCase {
    Integer createMovimiento(Movimiento movimiento);
    Movimiento getMovimientoById(Integer movimientoId);
    List<Movimiento> getAllMovimientos();
    void updateMovimiento(Integer movimientoId, Movimiento movimiento);
    void deleteMovimiento(Integer movimientoId);
    List<Movimiento> getMovimientosByCuentaId(Integer cuentaId);
    List<Movimiento> getMovimientosByFechaRange(LocalDate fechaInicio, LocalDate fechaFin);
}