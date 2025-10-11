package com.resolutions.application.ports.in;

import com.resolutions.model.Cuenta;
import java.util.List;

public interface CuentaUseCase {
    Integer createCuenta(Cuenta cuenta);
    Cuenta getCuentaById(Integer cuentaId);
    List<Cuenta> getAllCuentas();
    void updateCuenta(Integer cuentaId, Cuenta cuenta);
    void deleteCuenta(Integer cuentaId);
    List<Cuenta> getCuentasByClienteId(Integer clienteId);
    Cuenta getCuentaByNumeroCuenta(String numeroCuenta);
}