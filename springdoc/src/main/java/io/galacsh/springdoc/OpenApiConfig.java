package io.galacsh.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(
        title = "Sample API 3",
        version = "1.0.0",
        description = "This is a sample API."
))
public class OpenApiConfig {
    /*
     * Since "org.springdoc:springdoc-openapi-starter-webmvc-api" is
     * defined in the "testImplementation", you can only use annotations
     * to configure the OpenAPI.
     */
}
