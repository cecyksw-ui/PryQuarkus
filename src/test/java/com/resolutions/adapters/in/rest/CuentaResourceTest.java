package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.CuentaUseCase;
import com.resolutions.model.Cuenta;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CuentaResource
 * Valida el comportamiento de los endpoints REST para gestión de cuentas
 */
@ExtendWith(MockitoExtension.class)
class CuentaResourceTest {

    @Mock
    private CuentaUseCase cuentaUseCase;

    @Mock
    private Logger logger;

    private CuentaResource cuentaResource;

    private Cuenta cuenta1;
    private Cuenta cuenta2;
    private Cuenta cuenta3;
    private Cuenta cuenta4;

    @BeforeEach
    void setUp() {
        cuentaResource = new CuentaResource();
        cuentaResource.cuentaUseCase = cuentaUseCase;
        cuentaResource.logger = logger;

        // Datos de prueba basados en los casos de uso
        cuenta1 = new Cuenta(1, "478758", "AHORRO", new BigDecimal("2000.00"), true, 1);
        cuenta2 = new Cuenta(2, "225487", "CORRIENTE", new BigDecimal("2000.00"), true, 2);
        cuenta3 = new Cuenta(3, "495878", "AHORRO", new BigDecimal("0.00"), true, 3);
        cuenta4 = new Cuenta(4, "496825", "CORRIENTE", new BigDecimal("540.00"), true, 3);
    }

    @Test
    @DisplayName("Crear cuenta exitosamente - Jose Lema Ahorro")
    void testCreateCuenta_JoseLemaAhorro_Success() {
        // Given
        when(cuentaUseCase.createCuenta(any(Cuenta.class))).thenReturn(1);

        // When
        Response response = cuentaResource.createCuenta(cuenta1);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseEntity = (Map<String, Object>) response.getEntity();
        assertEquals(1, responseEntity.get("cuentaId"));
        assertEquals("Cuenta creada exitosamente", responseEntity.get("message"));

        verify(cuentaUseCase).createCuenta(cuenta1);
    }

    @Test
    @DisplayName("Crear cuenta exitosamente - Marianela Corriente")
    void testCreateCuenta_MarianelaCorriente_Success() {
        // Given
        when(cuentaUseCase.createCuenta(any(Cuenta.class))).thenReturn(2);

        // When
        Response response = cuentaResource.createCuenta(cuenta2);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseEntity = (Map<String, Object>) response.getEntity();
        assertEquals(2, responseEntity.get("cuentaId"));
        assertEquals("Cuenta creada exitosamente", responseEntity.get("message"));

        verify(cuentaUseCase).createCuenta(cuenta2);
    }

    @Test
    @DisplayName("Crear cuenta con saldo cero - Juan Osorio")
    void testCreateCuenta_JuanSaldoCero_Success() {
        // Given
        when(cuentaUseCase.createCuenta(any(Cuenta.class))).thenReturn(3);

        // When
        Response response = cuentaResource.createCuenta(cuenta3);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseEntity = (Map<String, Object>) response.getEntity();
        assertEquals(3, responseEntity.get("cuentaId"));

        verify(cuentaUseCase).createCuenta(cuenta3);
    }

    @Test
    @DisplayName("Error al crear cuenta - IllegalArgumentException")
    void testCreateCuenta_IllegalArgumentException_BadRequest() {
        // Given
        when(cuentaUseCase.createCuenta(any(Cuenta.class)))
                .thenThrow(new IllegalArgumentException("Ya existe una cuenta con este número"));

        // When
        Response response = cuentaResource.createCuenta(cuenta1);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Ya existe una cuenta con este número", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Error interno al crear cuenta")
    void testCreateCuenta_InternalError() {
        // Given
        when(cuentaUseCase.createCuenta(any(Cuenta.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // When
        Response response = cuentaResource.createCuenta(cuenta1);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Error interno del servidor", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Obtener cuenta por ID exitosamente")
    void testGetCuentaById_Success() {
        // Given
        when(cuentaUseCase.getCuentaById(1)).thenReturn(cuenta1);

        // When
        Response response = cuentaResource.getCuentaById(1);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        Cuenta responseCuenta = (Cuenta) response.getEntity();
        assertEquals(1, responseCuenta.getCuentaId());
        assertEquals("478758", responseCuenta.getNumeroCuenta());
        assertEquals("AHORRO", responseCuenta.getTipoCuenta());
        assertEquals(new BigDecimal("2000.00"), responseCuenta.getSaldoInicial());
        assertTrue(responseCuenta.getEstado());

        verify(cuentaUseCase).getCuentaById(1);
    }

    @Test
    @DisplayName("Cuenta no encontrada por ID")
    void testGetCuentaById_NotFound() {
        // Given
        when(cuentaUseCase.getCuentaById(999))
                .thenThrow(new RuntimeException("Cuenta no encontrada"));

        // When
        Response response = cuentaResource.getCuentaById(999);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cuenta no encontrada", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Obtener todas las cuentas exitosamente")
    void testGetAllCuentas_Success() {
        // Given
        List<Cuenta> cuentas = Arrays.asList(cuenta1, cuenta2, cuenta3, cuenta4);
        when(cuentaUseCase.getAllCuentas()).thenReturn(cuentas);

        // When
        Response response = cuentaResource.getAllCuentas(null);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        List<Cuenta> responseList = (List<Cuenta>) response.getEntity();
        assertEquals(4, responseList.size());
        assertEquals("478758", responseList.get(0).getNumeroCuenta());
        assertEquals("225487", responseList.get(1).getNumeroCuenta());
        assertEquals("495878", responseList.get(2).getNumeroCuenta());
        assertEquals("496825", responseList.get(3).getNumeroCuenta());

        verify(cuentaUseCase).getAllCuentas();
    }

    @Test
    @DisplayName("Error interno al obtener todas las cuentas")
    void testGetAllCuentas_InternalError() {
        // Given
        when(cuentaUseCase.getAllCuentas())
                .thenThrow(new RuntimeException("Error de base de datos"));

        // When
        Response response = cuentaResource.getAllCuentas(null);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Error interno del servidor", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Obtener cuentas por cliente ID exitosamente")
    void testGetCuentasByClienteId_Success() {
        // Given
        List<Cuenta> cuentasJuan = Arrays.asList(cuenta3, cuenta4);
        when(cuentaUseCase.getCuentasByClienteId(3)).thenReturn(cuentasJuan);

        // When
        Response response = cuentaResource.getAllCuentas(3);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        List<Cuenta> responseList = (List<Cuenta>) response.getEntity();
        assertEquals(2, responseList.size());
        assertEquals("495878", responseList.get(0).getNumeroCuenta()); // Ahorro
        assertEquals("496825", responseList.get(1).getNumeroCuenta()); // Corriente

        verify(cuentaUseCase).getCuentasByClienteId(3);
    }

    @Test
    @DisplayName("Actualizar cuenta exitosamente")
    void testUpdateCuenta_Success() {
        // Given
        Cuenta cuentaActualizada = new Cuenta(1, "478758", "AHORRO", new BigDecimal("2500.00"), true, 1);
        doNothing().when(cuentaUseCase).updateCuenta(1, cuentaActualizada);

        // When
        Response response = cuentaResource.updateCuenta(1, cuentaActualizada);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cuenta actualizada exitosamente", responseEntity.get("message"));

        verify(cuentaUseCase).updateCuenta(1, cuentaActualizada);
    }

    @Test
    @DisplayName("Error al actualizar cuenta - No encontrada")
    void testUpdateCuenta_NotFound() {
        // Given
        Cuenta cuentaActualizada = new Cuenta(999, "999999", "AHORRO", new BigDecimal("1000.00"), true, 1);
        doThrow(new RuntimeException("Cuenta no encontrada con ID: 999"))
                .when(cuentaUseCase).updateCuenta(999, cuentaActualizada);

        // When
        Response response = cuentaResource.updateCuenta(999, cuentaActualizada);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cuenta no encontrada con ID: 999", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Eliminar cuenta exitosamente")
    void testDeleteCuenta_Success() {
        // Given
        doNothing().when(cuentaUseCase).deleteCuenta(1);

        // When
        Response response = cuentaResource.deleteCuenta(1);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cuenta eliminada exitosamente", responseEntity.get("message"));

        verify(cuentaUseCase).deleteCuenta(1);
    }

    @Test
    @DisplayName("Error al eliminar cuenta - No encontrada")
    void testDeleteCuenta_NotFound() {
        // Given
        doThrow(new RuntimeException("Cuenta no encontrada con ID: 999"))
                .when(cuentaUseCase).deleteCuenta(999);

        // When
        Response response = cuentaResource.deleteCuenta(999);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cuenta no encontrada con ID: 999", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Validar datos de cuenta de Juan Osorio con saldo cero")
    void testValidateJuanOsorioCuentaSaldoCero() {
        // Given
        when(cuentaUseCase.getCuentaById(3)).thenReturn(cuenta3);

        // When
        Response response = cuentaResource.getCuentaById(3);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        Cuenta responseCuenta = (Cuenta) response.getEntity();
        assertEquals(3, responseCuenta.getCuentaId());
        assertEquals("495878", responseCuenta.getNumeroCuenta());
        assertEquals("AHORRO", responseCuenta.getTipoCuenta());
        assertEquals(new BigDecimal("0.00"), responseCuenta.getSaldoInicial());
        assertEquals(Integer.valueOf(3), responseCuenta.getClienteId()); // Juan Osorio
        assertTrue(responseCuenta.getEstado());
    }

    @Test
    @DisplayName("Validar datos de cuenta corriente de Juan Osorio")
    void testValidateJuanOsorioCuentaCorriente() {
        // Given
        when(cuentaUseCase.getCuentaById(4)).thenReturn(cuenta4);

        // When
        Response response = cuentaResource.getCuentaById(4);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        Cuenta responseCuenta = (Cuenta) response.getEntity();
        assertEquals(4, responseCuenta.getCuentaId());
        assertEquals("496825", responseCuenta.getNumeroCuenta());
        assertEquals("CORRIENTE", responseCuenta.getTipoCuenta());
        assertEquals(new BigDecimal("540.00"), responseCuenta.getSaldoInicial());
        assertEquals(Integer.valueOf(3), responseCuenta.getClienteId()); // Juan Osorio
        assertTrue(responseCuenta.getEstado());
    }
}