package com.resolutions.adapters.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para las respuestas de cliente que incluye datos de persona
 */
public class ClienteResponseDto {
    
    @JsonProperty("id")
    private Integer clienteId;
    
    @JsonProperty("nombres")
    private String nombres;
    
    @JsonProperty("direccion")
    private String direccion;
    
    @JsonProperty("telefono")
    private String telefono;
    
    @JsonProperty("estado")
    private Boolean estado;
    
    // No incluir contraseña en las respuestas por seguridad
    
    public ClienteResponseDto() {
    }

    public ClienteResponseDto(Integer clienteId, String nombres, String direccion, String telefono, Boolean estado) {
        this.clienteId = clienteId;
        this.nombres = nombres;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ClienteResponseDto{" +
                "clienteId=" + clienteId +
                ", nombres='" + nombres + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado=" + estado +
                '}';
    }
}