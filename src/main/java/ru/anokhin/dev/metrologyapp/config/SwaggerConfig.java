package ru.anokhin.dev.metrologyapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${project.version}")
    private String projectVersion;

    @Bean
    public OpenAPI customoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Metrology App API")
                        .description("API для управления метрологическими измерениями и подразделениями")
                        .version(projectVersion)
                );
    }
}
