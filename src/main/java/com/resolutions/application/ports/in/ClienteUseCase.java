package com.resolutions.application.ports.in;

import com.resolutions.model.Cliente;
import java.util.List;

public interface ClienteUseCase {
    Integer createCliente(Cliente cliente);
    Cliente getClienteById(Integer clienteId);
    List<Cliente> getAllClientes();
    void updateCliente(Integer clienteId, Cliente cliente);
    void deleteCliente(Integer clienteId);
    List<Cliente> getClientesByEstado(Boolean estado);
}