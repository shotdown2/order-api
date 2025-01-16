package br.com.mounts.order_api.infraestructure.controller;

import br.com.mounts.order_api.domain.dto.OrderDto;
import br.com.mounts.order_api.domain.dto.OrderItemDto;
import br.com.mounts.order_api.domain.dto.OrderRequest;
import br.com.mounts.order_api.domain.dto.OrderResponse;
import br.com.mounts.order_api.domain.interfaces.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the OrderController class.
 *
 * @author Raphael Braga
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private OrderService orderService;
    private List<OrderRequest> orders = new ArrayList<>();
    private List<OrderItemDto> items = new ArrayList<>();
    private List<OrderDto> ordersDto = new ArrayList<>();

    @BeforeEach
    void setUp(){
        items.add(new OrderItemDto().builder()
                .name("product test")
                .quantity(BigDecimal.ONE)
                .unitPrice(BigDecimal.TEN)
                .build());

        orders.add(new OrderRequest().builder()
                .externalOrderId("1")
                .clientName("test")
                .clientDocument("1")
                .items(items)
                .build());

        ordersDto.add(new OrderDto().builder()
                .externalOrderId("1")
                .clientName("test")
                .clientDocument("1")
                .items(items)
                .build());
    }

    @Test
    void testCreateOrders() throws Exception {

        OrderResponse response = modelMapper.map(orders, OrderResponse.class);
        response.setId("1");

        when(orderService.processOrders(orders)).thenReturn(Collections.singletonList(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"externalOrderId\":\"1\",\"clientName\":\"test\",\"clientDocument\":\"1\",\"items\":[{\"name\":\"product test\",\"quantity\":1,\"unitPrice\":10}]}]"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(response.getId()));
    }

    @Test
    void testGetOrders() throws Exception {
        Page<OrderDto> page = new PageImpl<>(ordersDto);

        when(orderService.findAllOrders(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetOrderByExternalId() throws Exception {
        String externalOrderId = "123";
        UUID id = UUID.randomUUID();

        ordersDto.get(0).setId(id);

        when(orderService.findByExternalOrderId(externalOrderId)).thenReturn(ordersDto.get(0));

        mockMvc.perform(get("/orders/" + externalOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ordersDto.get(0).getId().toString()));
    }
}
