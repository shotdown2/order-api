package br.com.mounts.order_api.infraestructure.config;

import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.entiy.OrderEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper setup
 *
 * Provides a configured ModelMapper bean with strict matching strategy
 * and null value skipping enabled.
 *
 * This configuration includes mappings for OrderRequest to OrderEntity.
 *
 * @author Raphael Braga
 */

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.typeMap(OrderRequest.class, OrderEntity.class)
                .addMappings(mapper -> {});

        return modelMapper;
    }
}
