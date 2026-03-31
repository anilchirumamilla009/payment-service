package com.techwave.paymentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SpringDoc / Swagger UI configuration aligned with the openapi.yaml contract.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Core Microservice API")
                        .version("0.0.1-SNAPSHOT")
                        .description("Service managing state and access to core data entities.")
                        .contact(new Contact()
                                .name("Stephen Flynn")
                                .email("stephen.flynn@cornerstonefs.com")))
                .servers(List.of(new Server().url("http://localhost:8080")));
    }
}

