package pl.products.management.controller;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.products.management.RestPage;
import pl.products.management.model.api.request.CreateProductRequest;
import pl.products.management.model.api.request.UpdateProductRequest;
import pl.products.management.model.api.response.ProductCategoryDetails;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.api.response.ProductHeaderResponse;
import pl.products.management.model.entity.ProductCategoryEntity;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.model.entity.RoleEntity;
import pl.products.management.model.entity.UserEntity;
import pl.products.management.repository.ProductCategoryRepository;
import pl.products.management.repository.ProductRepository;
import pl.products.management.repository.RoleRepository;
import pl.products.management.repository.UserRepository;
import pl.products.management.service.AuthService;
import pl.products.management.service.RoleService;
import pl.products.management.service.UserService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

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

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/products";

        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private String createAdminUserAndGetAuthHeader(){

        String rawPassword = "KamilKamil1";
        String encryptedPassword = "$2a$10$qM1I98etD3te7IqPAnsOcOvK0NgWjHTvWuiiq1l5dXr/UNAdJJOr.";

        UserEntity toCreateUser = UserEntity.builder()
            .username("admin")
            .password(encryptedPassword)
            .firstname("Adam")
            .lastname("Kowalski")
            .roles(new HashSet<>())
            .build();
        toCreateUser = userRepository.save(toCreateUser);

        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ADMIN");
        adminRole = roleRepository.save(adminRole);

        userService.assignRole(toCreateUser.getId(), adminRole.getId());

        return "Bearer " + authService.login(toCreateUser.getUsername(), rawPassword);
    }

    private String createUserAndGetAuthHeader(){

        String rawPassword = "KamilKamil1";
        String encryptedPassword = "$2a$10$qM1I98etD3te7IqPAnsOcOvK0NgWjHTvWuiiq1l5dXr/UNAdJJOr.";

        UserEntity toCreateUser = UserEntity.builder()
            .username("kamil")
            .password(encryptedPassword)
            .firstname("Kamil")
            .lastname("Nowak")
            .build();

        toCreateUser = userRepository.save(toCreateUser);

        return "Bearer " + authService.login(toCreateUser.getUsername(), rawPassword);
    }

    @Test
    public void shouldGetPage(){

        //given
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setName("kategoria");
        productCategory = productCategoryRepository.save(productCategory);

        ProductEntity product = ProductEntity.builder()
            .name("nazwa")
            .description("opis")
            .price(new BigDecimal("22.26"))
            .productCategory(productCategory)
            .build();
        product = productRepository.save(product);

        ProductEntity product1 = ProductEntity.builder()
            .name("nazwa1")
            .description("opis1")
            .price(new BigDecimal("24.28"))
            .productCategory(null)
            .build();
        product1 = productRepository.save(product1);

        List<ProductEntity> products = List.of(product, product1);

        Pageable pageable = PageRequest.of(0, 2);

        String authHeader = createUserAndGetAuthHeader();

        //when
        Page<ProductHeaderResponse> gotProductsHeadersPage = RestAssured
            .given()
                .param("page", pageable.getPageNumber())
                .param("size", pageable.getPageSize())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
            .get()
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<ProductHeaderResponse>>() {});

        //then
        assertNotNull(gotProductsHeadersPage);
        assertEquals(pageable.getPageNumber(), gotProductsHeadersPage.getNumber());
        assertEquals(pageable.getPageSize(), gotProductsHeadersPage.getSize());
        assertEquals(products.size(), gotProductsHeadersPage.getTotalElements());

        List<ProductHeaderResponse> gotProductsHeaders = gotProductsHeadersPage.getContent();

        assertNotNull(gotProductsHeaders);
        assertEquals(products.size(), gotProductsHeaders.size());

        for(int i = 0; i < products.size(); i++){

            ProductEntity productEntity = products.get(i);
            ProductHeaderResponse productHeaderResponse = gotProductsHeaders.get(i);

            assertEquals(productEntity.getId(), productHeaderResponse.id());
            assertEquals(productEntity.getName(), productHeaderResponse.name());
            assertEquals(productEntity.getPrice(), productHeaderResponse.price());
        }
    }

    @Test
    public void shouldGetPageByCategory(){

        //given
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setName("kategoria");
        productCategory = productCategoryRepository.save(productCategory);

        ProductEntity product = ProductEntity.builder()
            .name("nazwa")
            .description("opis")
            .price(new BigDecimal("22.26"))
            .productCategory(productCategory)
            .build();
        product = productRepository.save(product);

        ProductEntity product1 = ProductEntity.builder()
            .name("nazwa1")
            .description("opis1")
            .price(new BigDecimal("24.28"))
            .productCategory(null)
            .build();
        product1 = productRepository.save(product1);

        List<ProductEntity> productsWithCategories = List.of(product);

        Pageable pageable = PageRequest.of(0, 2);

        String authHeader = createUserAndGetAuthHeader();

        //when
        Page<ProductHeaderResponse> gotProductsHeadersPage = RestAssured
            .given()
                .param("page", pageable.getPageNumber())
                .param("size", pageable.getPageSize())
                .pathParam("categoryName", productCategory.getName())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
            .get("/category/{categoryName}")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<ProductHeaderResponse>>() {});

        //then
        assertNotNull(gotProductsHeadersPage);
        assertEquals(pageable.getPageNumber(), gotProductsHeadersPage.getNumber());
        assertEquals(pageable.getPageSize(), gotProductsHeadersPage.getSize());
        assertEquals(productsWithCategories.size(), gotProductsHeadersPage.getTotalElements());

        List<ProductHeaderResponse> gotProductsHeaders = gotProductsHeadersPage.getContent();

        assertNotNull(gotProductsHeaders);
        assertEquals(productsWithCategories.size(), gotProductsHeaders.size());

        for(int i = 0; i < productsWithCategories.size(); i++){

            ProductEntity productEntity = productsWithCategories.get(i);
            ProductHeaderResponse productHeaderResponse = gotProductsHeaders.get(i);

            assertEquals(productEntity.getId(), productHeaderResponse.id());
            assertEquals(productEntity.getName(), productHeaderResponse.name());
            assertEquals(productEntity.getPrice(), productHeaderResponse.price());
        }
    }

    @Test
    void shouldGetDetailsById() {

        //given
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setName("kategoria");
        productCategory = productCategoryRepository.save(productCategory);

        ProductEntity product = ProductEntity.builder()
            .name("nazwa")
            .description("opis")
            .price(new BigDecimal("22.26"))
            .productCategory(productCategory)
            .build();
        product = productRepository.save(product);

        String authHeader = createUserAndGetAuthHeader();

        //when
        ProductDetailsResponse gotResponse = RestAssured
            .given()
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .pathParam("productId", product.getId())
            .get("/{productId}")
            .then()
                .statusCode(200)
                .extract()
                .as(ProductDetailsResponse.class);

        //then
        assertNotNull(gotResponse);
        assertEquals(gotResponse.id(), product.getId());
        assertEquals(gotResponse.name(), product.getName());
        assertEquals(gotResponse.description(), product.getDescription());
        assertEquals(gotResponse.price(), product.getPrice());

        ProductCategoryDetails gotProductCategoryDetails = gotResponse.productCategory();
        assertNotNull(gotProductCategoryDetails);
        assertEquals(gotProductCategoryDetails.id(), productCategory.getId().toString());
        assertEquals(gotProductCategoryDetails.name(), productCategory.getName());
    }

    @Test
    void shouldCreate() {

        //given
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setName("kategoria");
        productCategory = productCategoryRepository.save(productCategory);

        CreateProductRequest request = new CreateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.26"),
            productCategory.getId().toString()
        );

        String authHeader = createAdminUserAndGetAuthHeader();

        //when
        ProductDetailsResponse response = RestAssured
            .given()
                .body(request)
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
            .post()
            .then()
                .statusCode(201)
                .extract()
                .as(ProductDetailsResponse.class);

        //then
        assertEquals(1, productRepository.count());

        ProductEntity createdProduct = productRepository.findAll().iterator().next();
        assertEquals(request.name(), createdProduct.getName());
        assertEquals(request.description(), createdProduct.getDescription());
        assertEquals(request.price(), createdProduct.getPrice());

        assertNotNull(createdProduct.getProductCategory());
        assertEquals(productCategory.getId(), createdProduct.getProductCategory().getId());

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(createdProduct.getId(), response.id());
        assertEquals(request.name(), response.name());
        assertEquals(request.description(), response.description());
        assertEquals(request.price(), response.price());

        assertNotNull(response.productCategory());
        assertEquals(productCategory.getId().toString(), response.productCategory().id());
        assertEquals(productCategory.getName(), response.productCategory().name());
    }

    @Test
    public void shouldUpdateById(){

        //given
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setName("kategoria");
        productCategory = productCategoryRepository.save(productCategory);

        ProductEntity product = ProductEntity.builder()
            .name("nazwa")
            .description("opis")
            .price(new BigDecimal("22.26"))
            .productCategory(productCategory)
            .build();
        product = productRepository.save(product);

        ProductCategoryEntity productCategory1 = new ProductCategoryEntity();
        productCategory1.setName("kategoria1");
        productCategory1 = productCategoryRepository.save(productCategory1);

        UpdateProductRequest request = new UpdateProductRequest(
            "nazwa1",
            "opis1",
            new BigDecimal("24.28"),
            productCategory1.getId().toString()
        );

        String authHeader = createAdminUserAndGetAuthHeader();

        //when
        ProductDetailsResponse response = RestAssured
            .given()
                .body(request)
                .contentType(ContentType.JSON)
                .pathParam("productId", product.getId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
            .put("/{productId}")
            .then()
                .statusCode(200)
            .extract()
            .as(ProductDetailsResponse.class);

        product = productRepository.findById(product.getId()).get();

        //then
        assertEquals(request.name(), product.getName());
        assertEquals(request.description(), product.getDescription());
        assertEquals(request.price(), product.getPrice());

        assertNotNull(product.getProductCategory());
        assertEquals(request.categoryId(), product.getProductCategory().getId().toString());

        assertNotNull(request);
        assertEquals(product.getId(), response.id());
        assertEquals(request.name(), response.name());
        assertEquals(request.price(), request.price());
        assertEquals(productCategory1.getId().toString(), response.productCategory().id());
        assertEquals(productCategory1.getName(), response.productCategory().name());
    }

    @Test
    void shouldDeleteById() {

        //given
        ProductEntity product = ProductEntity.builder()
            .name("nazwa")
            .description("opis")
            .price(new BigDecimal("22.26"))
            .productCategory(null)
            .build();
        product = productRepository.save(product);

        String authHeader = createAdminUserAndGetAuthHeader();

        //when
        RestAssured
            .given()
                .pathParam("productId", product.getId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
            .delete("/{productId}")
            .then()
                .statusCode(204);

        //then
        assertEquals(0, productRepository.count());
    }
}