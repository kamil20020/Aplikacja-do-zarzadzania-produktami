package pl.products.management.mapper;

import org.junit.jupiter.api.Test;
import pl.products.management.model.api.request.CreateProductRequest;
import pl.products.management.model.api.request.UpdateProductRequest;
import pl.products.management.model.api.response.ProductCategoryDetails;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.api.response.ProductHeaderResponse;
import pl.products.management.model.entity.ProductCategoryEntity;
import pl.products.management.model.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private static final ProductMapper productMapper = new ProductMapperImpl(new ProductCategoryMapperImpl());

    @Test
    void shouldMapFromCreateRequest() {

        //given
        CreateProductRequest request = new CreateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.26"),
            UUID.randomUUID().toString()
        );

        //when
        ProductEntity product = productMapper.map(request);

        //then
        assertNotNull(product);
        assertEquals(request.name(), product.getName());
        assertEquals(request.description(), product.getDescription());
        assertNotNull(product.getProductCategory());
    }

    @Test
    void shouldMapFromUpdateRequest() {

        //given
        UpdateProductRequest request = new UpdateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.26"),
            UUID.randomUUID().toString()
        );

        //when
        ProductEntity product = productMapper.map(request);

        //then
        assertNotNull(product);
        assertEquals(request.name(), product.getName());
        assertEquals(request.description(), product.getDescription());
        assertNotNull(product.getProductCategory());
    }

    @Test
    void shouldMapToDetailsResponse() {

        //given
        ProductEntity product = ProductEntity.builder()
            .id(UUID.randomUUID())
            .price(new BigDecimal("22.28"))
            .name("nazwa")
            .description("opis")
            .productCategory(new ProductCategoryEntity())
            .build();

        //when
        ProductDetailsResponse response = productMapper.map(product);

        //then
        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertNotNull(response.productCategory());
    }

    @Test
    void shouldMapToHeaderResponse() {

        //given
        ProductEntity product = ProductEntity.builder()
            .id(UUID.randomUUID())
            .price(new BigDecimal("22.28"))
            .name("nazwa")
            .description("opis")
            .productCategory(new ProductCategoryEntity())
            .build();

        //when
        ProductHeaderResponse response = productMapper.mapToHeader(product);

        //then
        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getName(), response.name());
    }
}