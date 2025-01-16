package br.com.mounts.order_api.infraestructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPIConfig
 *
 * @author Raphael Braga
 */

@Configuration
@OpenAPIDefinition(info = @Info(title = "Project Management Orders API", version = "1.0", description = "API para gerenciamento de ordens"))
public class OpenAPIConfig {
}
