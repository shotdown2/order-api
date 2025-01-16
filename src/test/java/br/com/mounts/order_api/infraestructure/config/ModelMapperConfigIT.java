package br.com.mounts.order_api.infraestructure.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the ModelMapperConfig class.
 *
 * @author Raphael Braga
 */
class ModelMapperConfigIT {

    private final ModelMapperConfig modelMapperConfig = new ModelMapperConfig();

    @Test
    void shouldCreateModelMapperBean() {
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        assertThat(modelMapper)
                .isNotNull()
                .isInstanceOf(ModelMapper.class);
    }
}
