package br.com.mounts.order_api.infraestructure.controller;

import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;
import br.com.mounts.order_api.domain.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
