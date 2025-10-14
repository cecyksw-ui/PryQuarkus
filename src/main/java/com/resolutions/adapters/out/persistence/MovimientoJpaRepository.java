package com.resolutions.adapters.out.persistence;

import com.resolutions.application.ports.out.MovimientoRepositoryPort;
import com.resolutions.model.Movimiento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovimientoJpaRepository implements MovimientoRepositoryPort {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public Integer save(Movimiento movimiento) {
        entityManager.persist(movimiento);
        entityManager.flush();
        return movimiento.getMovimientoId();
    }

    @Override
    public Optional<Movimiento> findById(Integer movimientoId) {
        Movimiento movimiento = entityManager.find(Movimiento.class, movimientoId);
        return Optional.ofNullable(movimiento);
    }

    @Override
    public List<Movimiento> findAll() {
        return entityManager.createQuery("SELECT m FROM Movimiento m ORDER BY m.fecha DESC", Movimiento.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Integer movimientoId, Movimiento movimiento) {
        Movimiento existingMovimiento = entityManager.find(Movimiento.class, movimientoId);
        if (existingMovimiento != null) {
            existingMovimiento.setFecha(movimiento.getFecha());
            existingMovimiento.setTipoMovimiento(movimiento.getTipoMovimiento());
            existingMovimiento.setValor(movimiento.getValor());
            existingMovimiento.setSaldo(movimiento.getSaldo());
            existingMovimiento.setCuentaId(movimiento.getCuentaId());
            entityManager.merge(existingMovimiento);
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer movimientoId) {
        Movimiento movimiento = entityManager.find(Movimiento.class, movimientoId);
        if (movimiento != null) {
            entityManager.remove(movimiento);
        }
    }

    @Override
    public boolean existsById(Integer movimientoId) {
        return findById(movimientoId).isPresent();
    }

    @Override
    public List<Movimiento> findByCuentaId(Integer cuentaId) {
        return entityManager.createQuery("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId ORDER BY m.fecha DESC", Movimiento.class)
                .setParameter("cuentaId", cuentaId)
                .getResultList();
    }

    @Override
    public List<Movimiento> findByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return entityManager.createQuery("SELECT m FROM Movimiento m WHERE m.fecha >= :fechaInicio AND m.fecha <= :fechaFin ORDER BY m.fecha DESC", Movimiento.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    @Override
    public List<Movimiento> findByCuentaIdAndFechaRange(Integer cuentaId, LocalDate fechaInicio, LocalDate fechaFin) {
        return entityManager.createQuery("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId AND m.fecha >= :fechaInicio AND m.fecha <= :fechaFin ORDER BY m.fecha DESC", Movimiento.class)
                .setParameter("cuentaId", cuentaId)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }
    
    @Override
    public Optional<Movimiento> findLastMovimientoByCuentaId(Integer cuentaId) {
        List<Movimiento> movimientos = entityManager.createQuery(
                "SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId ORDER BY m.fecha DESC, m.movimientoId DESC", 
                Movimiento.class)
                .setParameter("cuentaId", cuentaId)
                .setMaxResults(1)
                .getResultList();
        
        return movimientos.isEmpty() ? Optional.empty() : Optional.of(movimientos.get(0));
    }
    
    @Override
    public BigDecimal getSaldoActualByCuentaId(Integer cuentaId) {
        Optional<Movimiento> ultimoMovimiento = findLastMovimientoByCuentaId(cuentaId);
        return ultimoMovimiento.map(Movimiento::getSaldo).orElse(null);
    }
    
    @Override
    public List<Movimiento> findByClienteIdAndFechaRange(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        return entityManager.createQuery(
                "SELECT m FROM Movimiento m " +
                "JOIN m.cuenta c " +
                "WHERE c.clienteId = :clienteId " +
                "AND m.fecha >= :fechaInicio " +
                "AND m.fecha <= :fechaFin " +
                "ORDER BY m.fecha DESC", 
                Movimiento.class)
                .setParameter("clienteId", clienteId)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }
}