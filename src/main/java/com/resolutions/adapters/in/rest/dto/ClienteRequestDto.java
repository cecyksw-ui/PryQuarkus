package com.resolutions.adapters.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para manejar las solicitudes de creación/actualización de clientes
 * que incluye tanto datos de persona como de cliente
 */
public class ClienteRequestDto {
    
    @JsonProperty("nombres")
    private String nombres;
    
    @JsonProperty("direccion") 
    private String direccion;
    
    @JsonProperty("telefono")
    private String telefono;
    
    @JsonProperty("contrasena")
    private String contrasena;
    
    @JsonProperty("estado")
    private Boolean estado = true;
    
    // Campos opcionales para datos de persona
    private String genero;
    private Integer edad;

    public ClienteRequestDto() {
    }

    public ClienteRequestDto(String nombres, String direccion, String telefono, String contrasena, Boolean estado) {
        this.nombres = nombres;
        this.direccion = direccion;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.estado = estado;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return "ClienteRequestDto{" +
                "nombres='" + nombres + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", estado=" + estado +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                '}';
    }
}