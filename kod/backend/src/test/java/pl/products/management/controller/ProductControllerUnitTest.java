package pl.products.management.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.cors.CorsConfiguration;
import pl.products.management.config.JwtFilter;
import pl.products.management.config.SecurityConfig;
import pl.products.management.mapper.ProductMapper;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.service.AuthService;
import pl.products.management.service.ProductService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    @WithMockUser
    void shouldGetDetailsById() throws Exception {

        //given
        UUID id = UUID.randomUUID();

        ProductEntity expectedProduct = new ProductEntity();

        ProductDetailsResponse expectedResponse = new ProductDetailsResponse(
            id,
            "nazwa",
            new BigDecimal("22.26"),
            "opis",
            null
        );

        //when
        when(productService.getById(any())).thenReturn(expectedProduct);
        when(productMapper.map(any(ProductEntity.class))).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(
            get("/products/" + id)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String responseStr = mvcResult.getResponse().getContentAsString();
        ProductDetailsResponse gotResponse = objectMapper.readValue(responseStr, ProductDetailsResponse.class);

        assertEquals(expectedResponse, gotResponse);

        verify(productService).getById(id);
        verify(productMapper).map(expectedProduct);
    }

    @Test
    @WithMockUser
    void shouldNotGetDetailsByIdWithNotExistingId() throws Exception {

        //given
        UUID id = UUID.randomUUID();

        //when
        when(productService.getById(any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(
            get("/products/" + id)
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andReturn();

        verify(productService).getById(id);
    }
}