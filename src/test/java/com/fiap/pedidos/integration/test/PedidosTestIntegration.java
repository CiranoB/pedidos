package com.fiap.pedidos.integration.test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fiap.pedidos.enums.PaymentStatus;
import com.fiap.pedidos.integration.config.TestConfigs;
import com.fiap.pedidos.integration.containers.AbstractIntegrationTest;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.records.UpdateOrderRecord;
import com.fiap.pedidos.repositories.OrderRepository;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PedidosTestIntegration extends AbstractIntegrationTest {

        private static RequestSpecification specification;

        @Autowired
        private OrderRepository orderRepository;

        private com.fiap.pedidos.models.Order response;

        @BeforeAll
        void setUp() {
                final var order = new com.fiap.pedidos.models.Order();
                order.setUserEmail("email@email.com");
                order.setCreateDate(LocalDateTime.now());
                order.setPaymentStatus(PaymentStatus.PAID);
                order.setProductQuantities(Map.of("teste", 1));
                this.response = this.orderRepository.save(order);
        }

        @Test
        @Order(0)
        void saveOrder() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order/save")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .body(new OrderRecord("teste@gmail.com", Map.of("teste", 1)))
                                .when().post().then()
                                .statusCode(200)
                                .extract().body().asString();

                assertNotNull(response);
        }

        @Test
        @Order(1)
        void getOrderById() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .when().get("/{orderId}", this.response.getIdOrder()).then()
                                .statusCode(200)
                                .extract().body().asString();

                assertNotNull(response);
        }

        @Test
        @Order(2)
        void getOrderByEmail() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .queryParam("email", "email@email.com")
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .when().get().then()
                                .statusCode(200)
                                .extract().body().asString();

                assertNotNull(response);
        }

        @Test
        @Order(3)
        void getAll() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order/all")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .when().get().then()
                                .statusCode(200)
                                .extract().body().asString();

                assertNotNull(response);
        }
        
        @Test
        @Order(4)
        void updateOrderStatus() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order/update_status")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .body(new UpdateOrderRecord(this.response.getIdOrder(), PaymentStatus.CANCELED))
                                .when().patch().then()
                                .statusCode(200)
                                .extract().body().asString();

                assertNotNull(response);
        }

        @Test
        @Order(5)
        void saveOrderFailedEmail() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order/save")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .body(new OrderRecord("teste", Map.of("teste", 1)))
                                .when().post().then()
                                .statusCode(422)
                                .extract().body().asString();

                assertNotNull(response);
        }
        
        @Test
        @Order(6)
        void saveOrderFailedQuantitiesProduct() {
                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .setBasePath("/api/order/save")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                final var response = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FRONT)
                                .body(new OrderRecord("teste", Map.of("teste@teste.com", 0)))
                                .when().post().then()
                                .statusCode(422)
                                .extract().body().asString();

                assertNotNull(response);
        }
}
