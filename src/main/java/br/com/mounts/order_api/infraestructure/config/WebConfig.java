package br.com.mounts.order_api.infraestructure.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class
 *
 * Configures CORS settings for the application.
 *
 * Allows all origins, methods, and headers for cross-origin requests,
 * with credentials disabled and a max age of 3600 seconds.
 *
 * Implements WebMvcConfigurer for additional web configuration customizations.
 *
 * @author Raphael Braga
 */
@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
