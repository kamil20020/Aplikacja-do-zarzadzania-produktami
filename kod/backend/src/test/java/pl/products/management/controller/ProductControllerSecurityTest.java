package pl.products.management.controller;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.products.management.model.api.request.CreateProductRequest;
import pl.products.management.model.api.request.UpdateProductRequest;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.service.ProductService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    void shouldGetPage() throws Exception {

        //given
        //when
        Mockito.when(productService.findPage(any())).thenReturn(Page.empty());

        //then
        mockMvc.perform(
            get("/products")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    void shouldNotGetPageWhenUnlogged() throws Exception {

        //given
        //when
        Mockito.when(productService.findPage(any())).thenReturn(Page.empty());

        //then
        mockMvc.perform(
            get("/products")
        )
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andReturn();
    }

    @Test
    @WithMockUser
    void shouldGetPageWithCategory() throws Exception {

        //given
        //when
        Mockito.when(productService.findPageByCategory(any(), any())).thenReturn(Page.empty());

        //then
        mockMvc.perform(
            get("/products/category/test")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    void shouldNotGetPageWithCategoryWhenUnlogged() throws Exception {

        //given
        //when
        Mockito.when(productService.findPageByCategory(any(), any())).thenReturn(Page.empty());

        //then
        mockMvc.perform(
            get("/products/category/test")
        )
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andReturn();
    }

    @Test
    @WithMockUser
    void shouldGetDetailsById() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(
            get("/products/" + UUID.randomUUID())
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    void shouldNotGetDetailsByIdWhenUnlogged() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(
            get("/products/" + UUID.randomUUID())
        )
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andReturn();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreate() throws Exception {

        //given
        CreateProductRequest request = new CreateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.28"),
            null
        );

        //when
        //then
        mockMvc.perform(
            post("/products")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andReturn();
    }

    @Test
    @WithMockUser
    void shouldNotCreateWhenNotAdmin() throws Exception {

        //given
        CreateProductRequest request = new CreateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.28"),
            null
        );

        //when
        //then
        mockMvc.perform(
            post("/products")
            .content(objectMapper.writeValueAsString(request))
        )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andReturn();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateById() throws Exception {

        //given
        UpdateProductRequest request = new UpdateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.28"),
            null
        );

        //when
        //then
        mockMvc.perform(
            put("/products/" + UUID.randomUUID())
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    @WithMockUser
    void shouldNotUpdateByIdWhenNotAdmin() throws Exception {

        //given
        UpdateProductRequest request = new UpdateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.28"),
            null
        );

        //when
        //then
        mockMvc.perform(
            put("/products/" + UUID.randomUUID())
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andReturn();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteById() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(
            delete("/products/" + UUID.randomUUID())
        )
        .andDo(print())
        .andExpect(status().isNoContent())
        .andReturn();
    }

    @Test
    @WithMockUser
    void shouldNotDeleteByIdWhenNotAdmin() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(
            delete("/products/" + UUID.randomUUID())
        )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andReturn();
    }
}
