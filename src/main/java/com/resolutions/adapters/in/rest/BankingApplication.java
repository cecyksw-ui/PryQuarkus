package com.resolutions.adapters.in.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
        title = "Banking API - Arquitectura Hexagonal",
        version = "1.0.0",
        description = "API REST para sistema bancario implementado con arquitectura hexagonal",
        contact = @Contact(
            name = "Equipo de Desarrollo",
            email = "dev@resolutions.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor de Desarrollo"),
        @Server(url = "https://api.resolutions.com", description = "Servidor de Producción")
    }
)
public class BankingApplication extends Application {
}