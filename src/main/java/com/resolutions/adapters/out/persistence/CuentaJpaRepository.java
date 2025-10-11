package com.resolutions.adapters.out.persistence;

import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.model.Cuenta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CuentaJpaRepository implements CuentaRepositoryPort {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public Integer save(Cuenta cuenta) {
        entityManager.persist(cuenta);
        entityManager.flush();
        return cuenta.getCuentaId();
    }

    @Override
    public Optional<Cuenta> findById(Integer cuentaId) {
        Cuenta cuenta = entityManager.find(Cuenta.class, cuentaId);
        return Optional.ofNullable(cuenta);
    }

    @Override
    public List<Cuenta> findAll() {
        return entityManager.createQuery("SELECT c FROM Cuenta c", Cuenta.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Integer cuentaId, Cuenta cuenta) {
        Cuenta existingCuenta = entityManager.find(Cuenta.class, cuentaId);
        if (existingCuenta != null) {
            existingCuenta.setNumeroCuenta(cuenta.getNumeroCuenta());
            existingCuenta.setTipoCuenta(cuenta.getTipoCuenta());
            existingCuenta.setSaldoInicial(cuenta.getSaldoInicial());
            existingCuenta.setEstado(cuenta.getEstado());
            existingCuenta.setClienteId(cuenta.getClienteId());
            entityManager.merge(existingCuenta);
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer cuentaId) {
        Cuenta cuenta = entityManager.find(Cuenta.class, cuentaId);
        if (cuenta != null) {
            entityManager.remove(cuenta);
        }
    }

    @Override
    public boolean existsById(Integer cuentaId) {
        return findById(cuentaId).isPresent();
    }

    @Override
    public List<Cuenta> findByClienteId(Integer clienteId) {
        return entityManager.createQuery("SELECT c FROM Cuenta c WHERE c.clienteId = :clienteId", Cuenta.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        List<Cuenta> result = entityManager.createQuery("SELECT c FROM Cuenta c WHERE c.numeroCuenta = :numeroCuenta", Cuenta.class)
                .setParameter("numeroCuenta", numeroCuenta)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return findByNumeroCuenta(numeroCuenta).isPresent();
    }
}