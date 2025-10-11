package com.resolutions.application.ports.out;

import com.resolutions.model.Cuenta;
import java.util.List;
import java.util.Optional;

public interface CuentaRepositoryPort {
    Integer save(Cuenta cuenta);
    Optional<Cuenta> findById(Integer cuentaId);
    List<Cuenta> findAll();
    void update(Integer cuentaId, Cuenta cuenta);
    void deleteById(Integer cuentaId);
    boolean existsById(Integer cuentaId);
    List<Cuenta> findByClienteId(Integer clienteId);
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    boolean existsByNumeroCuenta(String numeroCuenta);
}