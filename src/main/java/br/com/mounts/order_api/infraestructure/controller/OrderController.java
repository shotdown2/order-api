package br.com.mounts.order_api.infraestructure.controller;

import br.com.mounts.order_api.domain.dto.OrderDto;
import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;
import br.com.mounts.order_api.domain.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing orders
 *
 * Base URL: /orders
 *
 * @author Raphael Braga
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<List<OrderResponse>> createOrders(@RequestBody List<OrderRequest> orders) {
        List<OrderResponse> createdOrders = orderService.processOrders(orders);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrders);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrders(@ParameterObject @PageableDefault(size = 100) Pageable pageable) {
        Page<OrderDto> orders = orderService.findAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{externalOrderId}")
    public ResponseEntity<OrderDto> getOrderByExternalId(@PathVariable String externalOrderId) {
        OrderDto order = orderService.findByExternalOrderId(externalOrderId);
        return ResponseEntity.ok(order);
    }
}
