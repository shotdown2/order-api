package br.com.mounts.order_api.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    public BusinessException(String code, String description, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public BusinessException(String code, String description, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}