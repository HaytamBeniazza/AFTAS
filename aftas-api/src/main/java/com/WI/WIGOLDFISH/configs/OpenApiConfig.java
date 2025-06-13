package com.WI.WIGOLDFISH.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AFTAS - Advanced Fishing Tournament Administration System API")
                        .version("1.0.0")
                        .description("""
                                **AFTAS** is a comprehensive REST API for managing fishing competitions and tournaments.
                                
                                ## Features
                                - **Tournament Management**: Complete CRUD operations for fishing competitions
                                - **Participant Registration**: Secure user registration and authentication
                                - **Competition Scoring**: Real-time scoring and leaderboard management
                                - **Results Analytics**: Comprehensive reporting and analytics
                                - **Admin Panel**: Full administrative control
                                
                                ## Authentication
                                This API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:
                                ```
                                Authorization: Bearer <your-jwt-token>
                                ```
                                
                                ## API Usage
                                - All endpoints return JSON responses
                                - Standard HTTP status codes are used
                                - Pagination is supported for list endpoints
                                - Comprehensive error handling with detailed messages
                                """)
                        .contact(new Contact()
                                .name("Haytam Beniazza")
                                .email("your.email@example.com")
                                .url("https://your-portfolio.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://aftas-api.example.com")
                                .description("Production Server")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /auth/login endpoint")));
    }
} 