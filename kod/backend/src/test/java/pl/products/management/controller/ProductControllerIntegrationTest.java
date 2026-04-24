package pl.products.management.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.repository.ProductCategoryRepository;
import pl.products.management.repository.ProductRepository;
import pl.products.management.repository.UserRepository;
import pl.products.management.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ProductControllerIntegrationTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:17.9");

    @LocalServerPort
    private Integer port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/products";

        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetDetailsById() {

        //given
        ProductEntity product = ProductEntity.builder()
            .build();

//        RestAssured
//            .given()
//                .get("/" + )
    }

    @Test
    void shouldCreate() {
    }

    @Test
    void shouldDeleteByid() {
    }
}