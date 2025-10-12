package com.resolutions.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente", schema = "arq_hex")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Integer clienteId;
    
    @Column(name = "persona_id", unique = true, nullable = false)
    private Integer personaId;
    
    @Column(name = "contrasena", length = 50, nullable = false)
    private String contrasena;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", referencedColumnName = "persona_id", insertable = false, updatable = false)
    private Persona persona;

    public Cliente() {
    }

    public Cliente(Integer clienteId, Integer personaId, String contrasena, Boolean estado) {
        this.clienteId = clienteId;
        this.personaId = personaId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "clienteId=" + clienteId +
                ", personaId=" + personaId +
                ", contrasena='" + contrasena + '\'' +
                ", estado=" + estado +
                ", persona=" + persona +
                '}';
    }
}