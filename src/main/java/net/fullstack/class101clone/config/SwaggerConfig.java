package net.fullstack.class101clone.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

public class SwaggerConfig {
    public OpenAPI openAPI() {
        return new OpenAPI().components(new Components()).info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("Swagger API").description("RESTful API").version("1.0.0");
    }
}
