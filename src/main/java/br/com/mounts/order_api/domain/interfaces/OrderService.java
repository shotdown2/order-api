package br.com.mounts.order_api.domain.interfaces;

import br.com.mounts.order_api.domain.dto.OrderDto;
import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for order management
 *
 * @author Raphael Braga
 */

public interface OrderService {

    List<OrderResponse> processOrders(List<OrderRequest> orders);
    Page<OrderDto> findAllOrders(Pageable pageable);
    OrderDto findByExternalOrderId(String externalOrderId);
}
