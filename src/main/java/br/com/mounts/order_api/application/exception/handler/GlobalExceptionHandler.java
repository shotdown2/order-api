package br.com.mounts.order_api.application.exception.handler;

import br.com.mounts.order_api.application.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", ex.getCode());
        response.put("message", ex.getMessage());
        response.put("description", ex.getDescription());

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
}
