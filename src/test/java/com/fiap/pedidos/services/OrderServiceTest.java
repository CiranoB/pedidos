package com.fiap.pedidos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fiap.pedidos.enums.PaymentStatus;
import com.fiap.pedidos.exceptions.InvalidEmailAddress;
import com.fiap.pedidos.exceptions.InvalidProductQuantity;
import com.fiap.pedidos.mock.OrderMock;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.repositories.OrderRepository;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private OrderEventGateway orderEventGateway;

    @Mock
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(mongoTemplate, orderEventGateway, orderRepository);
    }

    @Test
    void testGetAll() {
        when(this.orderRepository.findAll()).thenReturn(List.of(OrderMock.build()));

        final var response = this.orderService.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());

    }

    @Test
    void testGetByOrderId() {
        when(this.orderRepository.findById(anyString())).thenReturn(Optional.of(OrderMock.build()));

        final var response = this.orderService.getByOrderId("");

        assertNotNull(response);
    }

    @Test
    void testGetOrderByEmail() {
        when(this.mongoTemplate.find(any(), any())).thenReturn(List.of(OrderMock.build()));

        final var response = this.orderService.getOrderByEmail("");

        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void testSaveOrder() {

        final var response = this.orderService.saveOrder(new OrderRecord("teste@email.com", Map.of("teste", 1)));

        assertNotNull(response);
    }

    @Test
    void testSaveOrderFailedEmail() {

        final var error = assertThrows(InvalidEmailAddress.class,
                () -> this.orderService.saveOrder(new OrderRecord("teste", Map.of("teste", 1))));

        assertNotNull(error);
        assertEquals("Invalid email address: teste", error.getMessage());
    }

    @Test
    void testSaveOrderFailedProductQuantities() {

        final var error = assertThrows(InvalidProductQuantity.class,
                () -> this.orderService.saveOrder(new OrderRecord("", Map.of("teste", 0))));

        assertNotNull(error);
        assertEquals("Product teste has less than 1 quantity!", error.getMessage());
    }

    @Test
    void testUpdateOrderPaymentStatus() {
        when(this.orderRepository.findById(anyString())).thenReturn(Optional.of(OrderMock.build()));

        final var response = this.orderService.updateOrderPaymentStatus("", PaymentStatus.PENDING);

        assertNotNull(response);

    }
   
    @Test
    void testUpdateOrderPaymentStatusEmpty() {
        when(this.orderRepository.findById(anyString())).thenReturn(Optional.empty());

        final var response = this.orderService.updateOrderPaymentStatus("", PaymentStatus.PENDING);

        assertNotNull(response);

    }
}
