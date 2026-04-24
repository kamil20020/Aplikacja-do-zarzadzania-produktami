package pl.products.management.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.cors.CorsConfiguration;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.products.management.config.JwtFilter;
import pl.products.management.config.SecurityConfig;
import pl.products.management.mapper.ProductMapper;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.service.AuthService;
import pl.products.management.service.ProductService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private AuthService authService;

    @MockBean
    private CorsConfiguration corsConfiguration;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetDetailsById() throws Exception {

        //given
        UUID id = UUID.randomUUID();
        ProductDetailsResponse expectedResponse = new ProductDetailsResponse(null, null, null, null, null);

        //when
        when(productMapper.map(any(ProductEntity.class))).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(
            get("/products/" + id.toString())
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String responseStr = mvcResult.getResponse().getContentAsString();
        ProductDetailsResponse gotResponse = objectMapper.readValue(responseStr, ProductDetailsResponse.class);

        assertEquals(expectedResponse, gotResponse);
    }

    @Test
    void shouldNotGetDetailsByIdWithNotExistingId() {


    }

    @Test
    void shouldNotCreateWhenUnlogged() {
    }

    @Test
    void shouldNotCreateWhenNotAdmin() {
    }

    @Test
    void shouldDeleteById() {
    }

    @Test
    void shouldNotDeleteByIdWhenUnlogged() {
    }

    @Test
    void shouldNotDeleteByIdWhenNotAdmin() {
    }
}