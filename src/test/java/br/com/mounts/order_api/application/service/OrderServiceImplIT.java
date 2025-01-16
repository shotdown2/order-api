package br.com.mounts.order_api.application.service;

import br.com.mounts.order_api.application.enums.OrderStatusEnum;
import br.com.mounts.order_api.application.exception.BusinessException;
import br.com.mounts.order_api.domain.dto.OrderDto;
import br.com.mounts.order_api.domain.dto.OrderItemDto;
import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;
import br.com.mounts.order_api.domain.entiy.OrderEntity;
import br.com.mounts.order_api.domain.entiy.ProductEntity;
import br.com.mounts.order_api.infraestructure.repository.OrderRepository;
import br.com.mounts.order_api.infraestructure.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the OrderServiceImpl class.
 *
 * @author Raphael Braga
 */
class OrderServiceImplIT {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private OrderServiceImpl orderService;
    private OrderEntity orderEntity;
    private OrderRequest orderRequest;
    private ProductEntity productEntity;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderEntity = new OrderEntity();
        orderEntity.setExternalOrderId("123");
        orderEntity.setClientName("Test Client");
        orderEntity.setTotalValue(BigDecimal.TEN);

        orderRequest = new OrderRequest();
        orderRequest.setExternalOrderId("123");
        orderRequest.setClientName("Test Client");
        orderRequest.setItems(Collections.singletonList(new OrderItemDto("Item 1", BigDecimal.ONE, BigDecimal.TEN)));

        productEntity = new ProductEntity();
        productEntity.setName("Item 1");
        productEntity.setQuantity(BigDecimal.ONE);
        productEntity.setUnitPrice(BigDecimal.TEN);

        orderDto = new OrderDto();
        orderDto.setExternalOrderId("123");
        orderDto.setClientName("Test Client");
        orderDto.setItems(Collections.singletonList(new OrderItemDto("Item 1", BigDecimal.ONE, BigDecimal.TEN)));
    }

    @Test
    void testFindAllOrders() {
        Pageable pageable = mock(Pageable.class);
        Page<OrderEntity> page = mock(Page.class);
        when(orderRepository.findAll(pageable)).thenReturn(page);
        when(page.map(any())).thenReturn(Page.empty());

        Page<OrderDto> orderDtos = orderService.findAllOrders(pageable);

        assertNotNull(orderDtos);
        verify(orderRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindByExternalOrderIdSuccess() {
        when(orderRepository.findByExternalOrderId("123")).thenReturn(Optional.of(orderEntity));
        when(modelMapper.map(orderEntity, OrderDto.class)).thenReturn(orderDto);

        OrderDto result = orderService.findByExternalOrderId("123");

        assertNotNull(result);
        assertEquals("123", result.getExternalOrderId());
        verify(orderRepository, times(1)).findByExternalOrderId("123");
    }

    @Test
    void testFindByExternalOrderIdNotFound() {
        when(orderRepository.findByExternalOrderId("123")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> orderService.findByExternalOrderId("123"));

        assertEquals("Pedido não encontrado com externalOrderId: 123", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), Integer.parseInt(exception.getCode()));
    }

    @Test
    void testProcessOrdersWithValidOrder() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(orderEntity));
        when(modelMapper.map(orderEntity, OrderRequest.class)).thenReturn(orderRequest);
        when(modelMapper.map(any(), eq(OrderResponse.class))).thenReturn(new OrderResponse());

        List<OrderResponse> responses = orderService.processOrders(Collections.singletonList(orderRequest));

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testProcessOrdersWithInvalidOrder() {
        orderRequest.setClientName("");  // Invalid Order due to empty client name

        List<OrderResponse> responses = orderService.processOrders(Collections.singletonList(orderRequest));

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(OrderStatusEnum.INVALIDO.getValue(), responses.get(0).getStatus());
    }

    @Test
    void testSaveOrder() {
        OrderEntity mockOrderEntity = new OrderEntity();
        mockOrderEntity.setTotalValue(BigDecimal.TEN);

        when(modelMapper.map(any(OrderRequest.class), eq(OrderEntity.class))).thenReturn(mockOrderEntity);

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(mockOrderEntity);

        OrderEntity savedOrderEntity = orderService.saveOrder(orderRequest);

        assertNotNull(savedOrderEntity);
        assertEquals(BigDecimal.TEN, savedOrderEntity.getTotalValue());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testBuilderResponse() {
        OrderResponse response = orderService.builderResponse(orderRequest, OrderStatusEnum.PROCESSADO);

        assertNotNull(response);
        assertEquals(OrderStatusEnum.PROCESSADO.getValue(), response.getStatus());
        assertEquals(orderRequest.getExternalOrderId(), response.getExternalOrderId());
    }

    @Test
    void testSaveProducts() {
        when(modelMapper.map(any(OrderItemDto.class), eq(ProductEntity.class))).thenReturn(productEntity);

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        orderService.saveProducts(new OrderItemDto("Item", BigDecimal.ONE, BigDecimal.TEN), orderEntity);

        verify(productRepository, times(1)).save(any(ProductEntity.class));

        assertEquals(orderEntity, productEntity.getOrder());
    }

    @Test
    void testTotalValueOrder() {
        BigDecimal totalValue = orderService.totalValueOrder(orderRequest);

        assertNotNull(totalValue);
        assertEquals(BigDecimal.TEN, totalValue);
    }
}
