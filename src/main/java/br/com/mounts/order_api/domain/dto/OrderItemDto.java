package br.com.mounts.order_api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO for order items
 *
 * @author Raphael Braga
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto implements Serializable {
    private String name;
    private BigDecimal quantity;
    private BigDecimal unitPrice;

    public boolean validate() {
        return isNameValid() && isQuantityValid() && isUnitPriceValid();
    }

    private boolean isNameValid() {
        return Objects.nonNull(name) && !name.trim().isEmpty();
    }

    private boolean isQuantityValid() {
        return Objects.nonNull(quantity) && quantity.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isUnitPriceValid() {
        return Objects.nonNull(unitPrice) && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
    }
}
