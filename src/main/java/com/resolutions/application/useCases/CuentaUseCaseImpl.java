package com.resolutions.application.useCases;

import com.resolutions.application.ports.in.CuentaUseCase;
import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.model.Cuenta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class CuentaUseCaseImpl implements CuentaUseCase {

    @Inject
    CuentaRepositoryPort cuentaRepository;

    @Inject
    ClienteRepositoryPort clienteRepository;

    @Override
    public Integer createCuenta(Cuenta cuenta) {
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cuenta es requerido");
        }
        if (cuenta.getClienteId() == null) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (!clienteRepository.existsById(cuenta.getClienteId())) {
            throw new IllegalArgumentException("No existe un cliente con ID: " + cuenta.getClienteId());
        }
        if (cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
            throw new IllegalArgumentException("Ya existe una cuenta con número: " + cuenta.getNumeroCuenta());
        }
        if (cuenta.getSaldoInicial() == null) {
            cuenta.setSaldoInicial(BigDecimal.ZERO);
        }
        if (cuenta.getEstado() == null) {
            cuenta.setEstado(true);
        }
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta getCuentaById(Integer cuentaId) {
        return cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaId));
    }

    @Override
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    @Override
    public void updateCuenta(Integer cuentaId, Cuenta cuenta) {
        if (!cuentaRepository.existsById(cuentaId)) {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }
        cuenta.setCuentaId(cuentaId);
        cuentaRepository.update(cuentaId, cuenta);
    }

    @Override
    public void deleteCuenta(Integer cuentaId) {
        if (!cuentaRepository.existsById(cuentaId)) {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }
        cuentaRepository.deleteById(cuentaId);
    }

    @Override
    public List<Cuenta> getCuentasByClienteId(Integer clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    @Override
    public Cuenta getCuentaByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con número: " + numeroCuenta));
    }
}