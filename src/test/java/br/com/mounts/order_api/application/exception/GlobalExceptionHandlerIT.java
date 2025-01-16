package br.com.mounts.order_api.application.exception;

import br.com.mounts.order_api.application.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the GlobalExceptionHandler class.
 *
 * @author Raphael Braga
 */

class GlobalExceptionHandlerIT {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleBusinessException_shouldReturnCorrectResponseEntity() {
        String code = "ERR001";
        String description = "User not found";
        String message = "Invalid credentials";
        HttpStatus status = HttpStatus.NOT_FOUND;
        BusinessException businessException = new BusinessException(code, description, message, status);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleBusinessException(businessException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo(code);
        assertThat(response.getBody().get("message")).isEqualTo(message);
        assertThat(response.getBody().get("description")).isEqualTo(description);
    }
}
