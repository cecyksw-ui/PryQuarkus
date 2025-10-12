package com.resolutions.integration;

import com.resolutions.application.ports.in.PersonaUseCase;
import com.resolutions.application.ports.in.ClienteUseCase;
import com.resolutions.application.ports.in.CuentaUseCase;
import com.resolutions.application.ports.in.MovimientoUseCase;
import com.resolutions.model.Persona;
import com.resolutions.model.Cliente;
import com.resolutions.model.Cuenta;
import com.resolutions.model.Movimiento;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración para poblar la base de datos con datos de prueba específicos
 * Limpia automáticamente las tablas antes de cada ejecución y crea todos los datos necesarios
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataPopulationIntegrationTest {

    @Inject
    PersonaUseCase personaUseCase;

    @Inject
    ClienteUseCase clienteUseCase;

    @Inject
    CuentaUseCase cuentaUseCase;

    @Inject
    MovimientoUseCase movimientoUseCase;

    @Inject
    EntityManager entityManager;

    /**
     * Limpia todas las tablas antes de ejecutar las pruebas
     */
    @BeforeAll
    @Transactional
    public void cleanDatabase() {
        System.out.println("🧹 Limpiando base de datos antes de poblar...");
        
        try {
            // Limpiar en orden debido a las foreign keys
            entityManager.createNativeQuery("DELETE FROM arq_hex.movimiento").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM arq_hex.cuenta").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM arq_hex.cliente").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM arq_hex.persona").executeUpdate();
            
            // Resetear secuencias para IDs consistentes
            entityManager.createNativeQuery("ALTER SEQUENCE arq_hex.persona_persona_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE arq_hex.cliente_cliente_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE arq_hex.cuenta_cuenta_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE arq_hex.movimiento_movimiento_id_seq RESTART WITH 1").executeUpdate();
            
            entityManager.flush();
            System.out.println("✅ Base de datos limpiada exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error limpiando base de datos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Prueba principal que crea todos los datos de prueba en una sola transacción
     * Esto garantiza consistencia y atomicidad
     */
    @Test
    @Order(1)
    @Transactional
    @DisplayName("Poblar base de datos con datos de prueba completos")
    public void test01_PopulateCompleteTestData() {
        System.out.println("🚀 Iniciando poblado completo de datos de prueba...");
        
        try {
            // 1. Crear Personas
            System.out.println("\n👥 Creando Personas...");
            
            Persona jose = new Persona(null, "Jose Lema", "M", 30, "Otavalo sn y principal", "098254785");
            Integer josePersonaId = personaUseCase.createPersona(jose);
            assertEquals(1, josePersonaId, "Jose Lema debe tener ID 1");
            System.out.println("✅ Jose Lema creado - ID: " + josePersonaId);
            
            Persona marianela = new Persona(null, "Marianela Montalvo", "F", 28, "13 junio y Equinoccial", "098875187");
            Integer marianelaPersonaId = personaUseCase.createPersona(marianela);
            assertEquals(2, marianelaPersonaId, "Marianela debe tener ID 2");
            System.out.println("✅ Marianela Montalvo creada - ID: " + marianelaPersonaId);
            
            Persona juan = new Persona(null, "Juan Osorio", "M", 35, "Lomas de san isidro", "097548965");
            Integer juanPersonaId = personaUseCase.createPersona(juan);
            assertEquals(3, juanPersonaId, "Juan debe tener ID 3");
            System.out.println("✅ Juan Osorio creado - ID: " + juanPersonaId);
            
            // 2. Crear Clientes
            System.out.println("\n💳 Creando Clientes...");
            
            Cliente joseCliente = new Cliente(null, josePersonaId, "1234", true);
            Integer joseClienteId = clienteUseCase.createCliente(joseCliente);
            assertEquals(1, joseClienteId, "Jose cliente debe tener ID 1");
            System.out.println("✅ Cliente Jose Lema creado - ID: " + joseClienteId);
            
            Cliente marianelaCliente = new Cliente(null, marianelaPersonaId, "5678", true);
            Integer marianelaClienteId = clienteUseCase.createCliente(marianelaCliente);
            assertEquals(2, marianelaClienteId, "Marianela cliente debe tener ID 2");
            System.out.println("✅ Cliente Marianela Montalvo creado - ID: " + marianelaClienteId);
            
            Cliente juanCliente = new Cliente(null, juanPersonaId, "1245", true);
            Integer juanClienteId = clienteUseCase.createCliente(juanCliente);
            assertEquals(3, juanClienteId, "Juan cliente debe tener ID 3");
            System.out.println("✅ Cliente Juan Osorio creado - ID: " + juanClienteId);
            
            // 3. Crear Cuentas
            System.out.println("\n🏦 Creando Cuentas...");
            
            // Cuenta ahorro Jose Lema
            Cuenta joseAhorro = new Cuenta(null, "478758", "AHORRO", new BigDecimal("2000.00"), true, joseClienteId);
            Integer joseAhorroId = cuentaUseCase.createCuenta(joseAhorro);
            assertEquals(1, joseAhorroId, "Cuenta Jose debe tener ID 1");
            System.out.println("✅ Cuenta Ahorro Jose Lema (478758) creada - ID: " + joseAhorroId);
            
            // Cuenta corriente Marianela
            Cuenta marianelaCorriente = new Cuenta(null, "225487", "CORRIENTE", new BigDecimal("2000.00"), true, marianelaClienteId);
            Integer marianelaCorId = cuentaUseCase.createCuenta(marianelaCorriente);
            assertEquals(2, marianelaCorId, "Cuenta Marianela debe tener ID 2");
            System.out.println("✅ Cuenta Corriente Marianela (225487) creada - ID: " + marianelaCorId);
            
            // Cuentas Juan Osorio
            Cuenta juanAhorro = new Cuenta(null, "495878", "AHORRO", new BigDecimal("0.00"), true, juanClienteId);
            Integer juanAhorroId = cuentaUseCase.createCuenta(juanAhorro);
            assertEquals(3, juanAhorroId, "Cuenta Juan ahorro debe tener ID 3");
            System.out.println("✅ Cuenta Ahorro Juan Osorio (495878) creada - ID: " + juanAhorroId);
            
            Cuenta juanCorriente = new Cuenta(null, "496825", "CORRIENTE", new BigDecimal("540.00"), true, juanClienteId);
            Integer juanCorId = cuentaUseCase.createCuenta(juanCorriente);
            assertEquals(4, juanCorId, "Cuenta Juan corriente debe tener ID 4");
            System.out.println("✅ Cuenta Corriente Juan Osorio (496825) creada - ID: " + juanCorId);
            
            // 4. Crear Movimientos
            System.out.println("\n💰 Creando Movimientos...");
            
            // Movimientos Jose Lema (2023-02-10)
            LocalDate fechaJose = LocalDate.of(2023, 2, 10);
            Movimiento joseDeposito = new Movimiento(null, fechaJose, "Credito", new BigDecimal("575.00"), new BigDecimal("2575.00"), joseAhorroId);
            Integer joseDep = movimientoUseCase.createMovimiento(joseDeposito);
            System.out.println("✅ Depósito Jose Lema (+$575) - ID: " + joseDep);
            
            Movimiento joseRetiro = new Movimiento(null, fechaJose, "Debito", new BigDecimal("-25.00"), new BigDecimal("2550.00"), joseAhorroId);
            Integer joseRet = movimientoUseCase.createMovimiento(joseRetiro);
            System.out.println("✅ Retiro Jose Lema (-$25) - ID: " + joseRet);
            
            // Movimientos Marianela (2023-02-08)
            LocalDate fechaMarianela = LocalDate.of(2023, 2, 8);
            Movimiento marianelaDeposito = new Movimiento(null, fechaMarianela, "Credito", new BigDecimal("594.00"), new BigDecimal("2594.00"), marianelaCorId);
            Integer marianelaDep = movimientoUseCase.createMovimiento(marianelaDeposito);
            System.out.println("✅ Depósito Marianela (+$594) - ID: " + marianelaDep);
            
            Movimiento marianelaRetiro = new Movimiento(null, fechaMarianela, "Debito", new BigDecimal("-4.00"), new BigDecimal("2590.00"), marianelaCorId);
            Integer marianelaRet = movimientoUseCase.createMovimiento(marianelaRetiro);
            System.out.println("✅ Retiro Marianela (-$4) - ID: " + marianelaRet);
            
            // Movimientos Juan Osorio (2023-02-06)
            LocalDate fechaJuan = LocalDate.of(2023, 2, 6);
            Movimiento juanDeposito = new Movimiento(null, fechaJuan, "Credito", new BigDecimal("2000.00"), new BigDecimal("2000.00"), juanAhorroId);
            Integer juanDep = movimientoUseCase.createMovimiento(juanDeposito);
            System.out.println("✅ Depósito Juan Osorio (+$2000) - ID: " + juanDep);
            
            Movimiento juanRetiro = new Movimiento(null, fechaJuan, "Debito", new BigDecimal("-2000.00"), new BigDecimal("0.00"), juanAhorroId);
            Integer juanRet = movimientoUseCase.createMovimiento(juanRetiro);
            System.out.println("✅ Retiro Juan Osorio (-$2000) - ID: " + juanRet);
            
            entityManager.flush(); // Forzar escritura a BD
            
            System.out.println("\n🎉 ¡Datos de prueba creados exitosamente!");
            
        } catch (Exception e) {
            System.err.println("❌ Error creando datos de prueba: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Verificación final de que todos los datos fueron creados correctamente
     */
    @Test
    @Order(2)
    @DisplayName("Verificar que todos los datos fueron creados correctamente")
    public void test02_VerifyAllDataCreated() {
        System.out.println("\n🔍 Verificando datos creados...");
        
        // Verificar personas
        var personas = personaUseCase.getAllPersonas();
        assertEquals(3, personas.size(), "Deben existir exactamente 3 personas");
        System.out.println("✅ Personas verificadas: " + personas.size());
        
        // Verificar clientes
        var clientes = clienteUseCase.getAllClientes();
        assertEquals(3, clientes.size(), "Deben existir exactamente 3 clientes");
        System.out.println("✅ Clientes verificados: " + clientes.size());
        
        // Verificar cuentas
        var cuentas = cuentaUseCase.getAllCuentas();
        assertEquals(4, cuentas.size(), "Deben existir exactamente 4 cuentas");
        System.out.println("✅ Cuentas verificadas: " + cuentas.size());
        
        // Verificar que las personas tienen los nombres correctos
        boolean joseExists = personas.stream().anyMatch(p -> "Jose Lema".equals(p.getNombre()));
        boolean marianelaExists = personas.stream().anyMatch(p -> "Marianela Montalvo".equals(p.getNombre()));
        boolean juanExists = personas.stream().anyMatch(p -> "Juan Osorio".equals(p.getNombre()));
        
        assertTrue(joseExists, "Jose Lema debe existir");
        assertTrue(marianelaExists, "Marianela Montalvo debe existir");
        assertTrue(juanExists, "Juan Osorio debe existir");
        
        // Verificar números de cuenta específicos
        boolean cuenta478758 = cuentas.stream().anyMatch(c -> "478758".equals(c.getNumeroCuenta()));
        boolean cuenta225487 = cuentas.stream().anyMatch(c -> "225487".equals(c.getNumeroCuenta()));
        boolean cuenta495878 = cuentas.stream().anyMatch(c -> "495878".equals(c.getNumeroCuenta()));
        boolean cuenta496825 = cuentas.stream().anyMatch(c -> "496825".equals(c.getNumeroCuenta()));
        
        assertTrue(cuenta478758, "Cuenta 478758 (Jose Lema) debe existir");
        assertTrue(cuenta225487, "Cuenta 225487 (Marianela) debe existir");
        assertTrue(cuenta495878, "Cuenta 495878 (Juan Osorio) debe existir");
        assertTrue(cuenta496825, "Cuenta 496825 (Juan Osorio) debe existir");
        
        System.out.println("\n📊 Resumen de datos creados:");
        System.out.println("   👥 3 Personas: Jose Lema, Marianela Montalvo, Juan Osorio");
        System.out.println("   💳 3 Clientes con contraseñas: 1234, 5678, 1245");
        System.out.println("   🏦 4 Cuentas: 478758, 225487, 495878, 496825");
        System.out.println("   💰 6 Movimientos históricos de febrero 2023");
        System.out.println("\n🎉 ¡Verificación exitosa! Base de datos lista para pruebas.");
    }
    
    /**
     * Método de utilidad para mostrar estadísticas de la base de datos
     */
    @AfterAll
    public void showDatabaseStats() {
        System.out.println("\n📈 Estadísticas finales de la base de datos:");
        
        try {
            var personas = personaUseCase.getAllPersonas();
            var clientes = clienteUseCase.getAllClientes();
            var cuentas = cuentaUseCase.getAllCuentas();
            
            System.out.println("   📋 Total registros creados:");
            System.out.println("      • Personas: " + personas.size());
            System.out.println("      • Clientes: " + clientes.size());
            System.out.println("      • Cuentas: " + cuentas.size());
            
            System.out.println("\n   🔗 Para verificar desde endpoints REST:");
            System.out.println("      • GET http://localhost:8080/api/personas");
            System.out.println("      • GET http://localhost:8080/api/clientes");
            System.out.println("      • GET http://localhost:8080/api/cuentas");
            System.out.println("      • GET http://localhost:8080/api/movimientos");
            
            System.out.println("\n   📊 Para generar reportes:");
            System.out.println("      • http://localhost:8080/reportes/estado_cuenta?cliente_id=1&fecha_inicio=2023-02-01&fecha_fin=2023-02-28");
            System.out.println("      • http://localhost:8080/reportes/estado_cuenta?cliente_id=2&fecha_inicio=2023-02-01&fecha_fin=2023-02-28");
            System.out.println("      • http://localhost:8080/reportes/estado_cuenta?cliente_id=3&fecha_inicio=2023-02-01&fecha_fin=2023-02-28");
            
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo estadísticas: " + e.getMessage());
        }
    }
}