package br.com.mounts.order_api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO for order requests
 *
 * @author Raphael Braga
 */

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderRequest implements Serializable {
    @EqualsAndHashCode.Include
    private String externalOrderId;
    private String clientName;
    private String clientDocument;
    private transient List<OrderItemDto> items;

    public boolean validate() {
        if (Objects.isNull(externalOrderId) || externalOrderId.trim().isEmpty()) {
            return false;
        }
        if (Objects.isNull(clientName) || clientName.trim().isEmpty()) {
            return false;
        }
        if (Objects.isNull(clientDocument) || clientDocument.trim().isEmpty()) {
            return false;
        }
        if (Objects.isNull(items) || items.isEmpty()) {
            return false;
        }

        return items.stream().allMatch(OrderItemDto::validate);
    }
}
