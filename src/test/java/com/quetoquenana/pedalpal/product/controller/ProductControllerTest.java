package com.quetoquenana.pedalpal.product.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.application.SecurityUser;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.application.query.ProductQueryService;
import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.presentation.controller.ProductController;
import com.quetoquenana.pedalpal.product.mapper.ProductApiMapper;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductPackageResponse;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductResponse;
import com.quetoquenana.pedalpal.util.TestProductData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@Import({SecurityConfig.class, ProductApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class ProductControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductQueryService queryService;

    @MockitoBean
    ProductApiMapper apiMapper;

    @MockitoBean
    CurrentUserProvider currentUserProvider;

    @MockitoBean
    MessageSource messageSource;

    @BeforeEach
    void setUpAuth() {
        when(currentUserProvider.getCurrentUser())
                .thenReturn(Optional.of(new SecurityUser(AUTH_USER_ID, "test-user", "Test User", false)));
    }

    @Test
    void shouldReturn200_whenGetProductById() throws Exception {
        UUID productId = UUID.randomUUID();

        ProductResult result = TestProductData.productResult(productId);
        ProductResponse response = TestProductData.productResponse(productId);

        when(queryService.getProductById(productId)).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(productId.toString()))
                .andExpect(jsonPath("$.data.name").value("Chain"));

        verify(queryService, times(1)).getProductById(productId);
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    void shouldReturn404_whenGetProductByIdNotFound() throws Exception {
        UUID productId = UUID.randomUUID();

        when(queryService.getProductById(productId)).thenThrow(new RecordNotFoundException());

        mockMvc.perform(get("/v1/api/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(apiMapper, never()).toResponse(any(ProductResult.class));
    }

    @Test
    void shouldReturn200_whenGetPackageById() throws Exception {
        UUID packageId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        ProductPackageResult result = TestProductData.packageResult(packageId, productId);
        ProductPackageResponse response = TestProductData.packageResponse(packageId, productId);

        when(queryService.getProductPackageById(packageId)).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/packages/{id}", packageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(packageId.toString()))
                .andExpect(jsonPath("$.data.products[0].id").value(productId.toString()));

        verify(queryService, times(1)).getProductPackageById(packageId);
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    void shouldReturn200_whenFindActiveProducts() throws Exception {
        UUID productId = UUID.randomUUID();

        ProductResult result = TestProductData.productResult(productId);
        ProductResponse response = TestProductData.productResponse(productId);

        when(queryService.getActiveProducts()).thenReturn(List.of(result));
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/products/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(productId.toString()));

        verify(queryService, times(1)).getActiveProducts();
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    void shouldReturn200_whenFindActivePackages() throws Exception {
        UUID packageId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        ProductPackageResult result = TestProductData.packageResult(packageId, productId);
        ProductPackageResponse response = TestProductData.packageResponse(packageId, productId);

        when(queryService.getActiveProductPackages()).thenReturn(List.of(result));
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/packages/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(packageId.toString()));

        verify(queryService, times(1)).getActiveProductPackages();
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    @WithMockJwt(userId = "", roles = {})
    void shouldReturn403_whenMissingRoleForProtectedEndpoint() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(get("/v1/api/products/{id}", productId))
                .andExpect(status().isForbidden());

        verify(queryService, never()).getProductById(eq(productId));
    }
}
