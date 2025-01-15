package br.com.mounts.order_api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order responses
 *
 * @author Raphael Braga
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;
    private String externalOrderId;
    private String clientName;
    private String clientDocument;
    private String status;
    private BigDecimal totalValue;
}
