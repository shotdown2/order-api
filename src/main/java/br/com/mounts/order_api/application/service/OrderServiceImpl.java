package br.com.mounts.order_api.application.service;

import br.com.mounts.order_api.application.enums.OrderStatusEnum;
import br.com.mounts.order_api.application.exception.BusinessException;
import br.com.mounts.order_api.domain.dto.OrderDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @Override
    public Page<OrderDto> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, OrderDto.class));
    }

    @Override
    public OrderDto findByExternalOrderId(String externalOrderId) {

        OrderEntity entity = orderRepository.findByExternalOrderId(externalOrderId)
                .orElseThrow(() -> new BusinessException(
                        String.valueOf(HttpStatus.NOT_FOUND.value()),
                        "Pedido não encontrado",
                        "Pedido não encontrado com externalOrderId: " + externalOrderId,
                        HttpStatus.NOT_FOUND));

        return modelMapper.map(entity, OrderDto.class);
    }

    void saveProducts(OrderItemDto item, OrderEntity savedOrderEntity){
        ProductEntity product = modelMapper.map(item, ProductEntity.class);
        product.setOrder(savedOrderEntity);
        productRepository.save(product);
    }

    BigDecimal totalValueOrder(OrderRequest order){
        return order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    OrderEntity saveOrder(OrderRequest order){
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderEntity.setTotalValue(totalValueOrder(order));
        orderEntity.setStatus(OrderStatusEnum.PROCESSADO);

        return orderRepository.save(orderEntity);
    }

    OrderResponse builderResponse(OrderRequest order, OrderStatusEnum status){
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
