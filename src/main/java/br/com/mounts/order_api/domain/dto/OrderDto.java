package br.com.mounts.order_api.domain.dto;

import br.com.mounts.order_api.application.enums.OrderStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO for orders
 *
 * @author Raphael Braga
 */

@Data
@Builder
public class OrderDto {
    private UUID id;
    private String externalOrderId;
    private String clientName;
    private String clientDocument;
    private OrderStatusEnum status;
    private BigDecimal totalValue;
    private List<OrderItemDto> items;
}
