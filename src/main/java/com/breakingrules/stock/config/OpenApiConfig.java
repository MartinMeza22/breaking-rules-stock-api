package com.breakingrules.stock.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Breaking Rules Stock API")
                        .description("API REST para gestión de stock de indumentaria. Permite CRUD, filtros, alertas de stock bajo y exportación CSV.")
                        .version("1.0.0"));
    }
}