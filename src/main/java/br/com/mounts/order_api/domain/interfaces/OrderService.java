package br.com.mounts.order_api.domain.interfaces;

import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;

import java.util.List;

/**
 * Service interface for order management
 *
 * @author Raphael Braga
 */

public interface OrderService {

    List<OrderResponse> processOrders(List<OrderRequest> orders);
}
