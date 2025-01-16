package br.com.mounts.order_api.application.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the BusinessException class.
 *
 * @author Raphael Braga
 */

class BusinessExceptionIT {

    @Test
    void shouldCreateBusinessExceptionWithAllFields() {
        String code = "400";
        String description = "Some error occurred";
        String message = "Bad Request";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        BusinessException exception = new BusinessException(code, description, message, status);

        assertThat(exception).isNotNull();
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getDescription()).isEqualTo(description);
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getHttpStatus()).isEqualTo(status);
    }

    @Test
    void shouldCreateBusinessExceptionWithCause() {
        String code = "500";
        String description = "Internal server error";
        String message = "Server Error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Throwable cause = new IllegalArgumentException("Invalid argument");

        BusinessException exception = new BusinessException(code, description, message, status, cause);

        assertThat(exception).isNotNull();
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getDescription()).isEqualTo(description);
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getHttpStatus()).isEqualTo(status);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}