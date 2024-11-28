package com.fiap.pedidos.integration.containers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

        static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mongoDBContainer, rabbitMQContainer)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                "spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl(),
                
                "spring.rabbitmq.host", rabbitMQContainer.getHost(),
                "spring.rabbitmq.port", String.valueOf(rabbitMQContainer.getMappedPort(5672)), // Porta AMQP
                "spring.rabbitmq.username", rabbitMQContainer.getAdminUsername(),
                "spring.rabbitmq.password", rabbitMQContainer.getAdminPassword()
            );
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
