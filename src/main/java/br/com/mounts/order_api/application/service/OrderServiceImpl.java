package br.com.mounts.order_api.application.service;

import br.com.mounts.order_api.application.enums.OrderStatusEnum;
import br.com.mounts.order_api.domain.dto.OrderItemDto;
import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;
import br.com.mounts.order_api.domain.entiy.OrderEntity;
import br.com.mounts.order_api.domain.entiy.ProductEntity;
import br.com.mounts.order_api.domain.interfaces.OrderService;
import br.com.mounts.order_api.infraestructure.repository.OrderRepository;
import br.com.mounts.order_api.infraestructure.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service implementation for order management
 *
 * @author Raphael Braga
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public List<OrderResponse> processOrders(List<OrderRequest> orders) {
        List<OrderResponse> processedOrders = Collections.synchronizedList(new ArrayList<>());

        List<OrderRequest> orderList = orderRepository.findAll()
                .stream().map(entity -> modelMapper.map(entity, OrderRequest.class)).toList();

        orders.parallelStream().forEach(order -> {
            try {
                if (!order.validate()) {
                    processedOrders.add(builderResponse(order, OrderStatusEnum.INVALIDO));
                } else {
                    processOrderValide(order, orderList, processedOrders);
                }
            } catch (Exception e) {
                processedOrders.add(builderResponse(order, OrderStatusEnum.INVALIDO));
            }
        });

        return processedOrders;
    }

    private void saveProducts(OrderItemDto item, OrderEntity savedOrderEntity){
        ProductEntity product = modelMapper.map(item, ProductEntity.class);
        product.setOrder(savedOrderEntity);
        productRepository.save(product);
    }

    private BigDecimal totalValueOrder(OrderRequest order){
        return order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderEntity saveOrder(OrderRequest order){
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderEntity.setTotalValue(totalValueOrder(order));
        orderEntity.setStatus(OrderStatusEnum.PROCESSADO);

        return orderRepository.save(orderEntity);
    }

    private OrderResponse builderResponse(OrderRequest order, OrderStatusEnum status){
        return OrderResponse.builder()
                .externalOrderId(order.getExternalOrderId())
                .totalValue(totalValueOrder(order))
                .clientName(order.getClientName())
                .clientDocument(order.getClientDocument())
                .status(status.getValue())
                .build();
    }

    private void processOrderValide(OrderRequest order, List<OrderRequest> orderList, List<OrderResponse> processedOrders){
        if (orderList.contains(order)) {
            processedOrders.add(builderResponse(order, OrderStatusEnum.DUPLICADO));
        } else {
            OrderEntity orderEntity = saveOrder(order);

            order.getItems().forEach(item -> saveProducts(item, orderEntity));

            processedOrders.add(modelMapper.map(orderEntity, OrderResponse.class));
        }
    }
}
