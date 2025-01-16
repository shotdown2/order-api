package br.com.mounts.order_api.infraestructure.config;

import br.com.mounts.order_api.domain.dto.OrderDto;
import br.com.mounts.order_api.domain.dto.OrderItemDto;
import br.com.mounts.order_api.domain.entiy.OrderEntity;
import br.com.mounts.order_api.domain.entiy.ProductEntity;
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

        modelMapper.typeMap(OrderEntity.class, OrderDto.class)
                .addMappings(mapper ->
                    mapper.map(OrderEntity::getProducts, OrderDto::setItems)
                );

        modelMapper.typeMap(ProductEntity.class, OrderItemDto.class)
                .addMappings(mapper -> {
                    mapper.map(ProductEntity::getName, OrderItemDto::setName);
                    mapper.map(ProductEntity::getQuantity, OrderItemDto::setQuantity);
                    mapper.map(ProductEntity::getUnitPrice, OrderItemDto::setUnitPrice);
                });

        return modelMapper;
    }
}
