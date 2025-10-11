package com.resolutions.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectivityTest {
    
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/prueba";
        String username = "postgres";
        String password = "postgres";
        
        System.out.println("Probando conexión a PostgreSQL...");
        System.out.println("URL: " + url);
        System.out.println("Usuario: " + username);
        
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ Conexión exitosa!");
            
            // Verificar versión de PostgreSQL
            try (PreparedStatement stmt = connection.prepareStatement("SELECT version()")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Versión PostgreSQL: " + rs.getString(1));
                }
            }
            
            // Verificar si existe el esquema arq_hex
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'arq_hex'")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("✅ Esquema 'arq_hex' existe");
                } else {
                    System.out.println("❌ Esquema 'arq_hex' NO existe");
                }
            }
            
            // Verificar tablas en el esquema
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'arq_hex'")) {
                ResultSet rs = stmt.executeQuery();
                System.out.println("Tablas en esquema 'arq_hex':");
                boolean hasTablas = false;
                while (rs.next()) {
                    System.out.println("  - " + rs.getString(1));
                    hasTablas = true;
                }
                if (!hasTablas) {
                    System.out.println("  ❌ No hay tablas en el esquema 'arq_hex'");
                }
            }
            
            // Verificar estado de Flyway
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT version, description, installed_on, success FROM flyway_schema_history ORDER BY installed_rank")) {
                ResultSet rs = stmt.executeQuery();
                System.out.println("Historial de migraciones Flyway:");
                boolean hasMigraciones = false;
                while (rs.next()) {
                    System.out.println(String.format("  - %s: %s (%s) - Éxito: %s", 
                        rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getBoolean(4)));
                    hasMigraciones = true;
                }
                if (!hasMigraciones) {
                    System.out.println("  ❌ No hay migraciones de Flyway registradas");
                }
            } catch (SQLException e) {
                System.out.println("  ❌ Tabla flyway_schema_history no existe: " + e.getMessage());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            System.err.println("Código de error: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            
            if (e.getMessage().contains("database \"prueba\" does not exist")) {
                System.err.println("\n🔧 SOLUCIÓN: La base de datos 'prueba' no existe.");
                System.err.println("Necesitas crear la base de datos primero:");
                System.err.println("1. Conectarte como administrador a PostgreSQL");
                System.err.println("2. Ejecutar: CREATE DATABASE prueba;");
            }
        }
    }
}