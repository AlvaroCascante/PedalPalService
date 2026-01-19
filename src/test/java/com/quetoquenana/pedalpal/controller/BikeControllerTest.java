package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.local.Bike;
import com.quetoquenana.pedalpal.service.BikeService;
import com.quetoquenana.pedalpal.util.JsonObjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.Headers.LOCATION;
import static com.quetoquenana.pedalpal.util.Constants.ResponseValues.DEFAULT_ERROR_CODE;
import static com.quetoquenana.pedalpal.util.Constants.ResponseValues.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest({BikeController.class})
@Import({SecurityConfig.class})
@WithMockUser(username = "admin", roles = "ADMIN")
class BikeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BikeService bikeService;

    private final String BASE_URL = "/v1/api/bikes";

    private final LinkedList<Bike> entitiesList = new LinkedList<>();

    private final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    @BeforeEach
    void setup() throws IOException {
        JsonObjectBuilder<Bike> mapper = new JsonObjectBuilder<>(Bike.class);
        entitiesList.addAll(mapper.loadListJsonFile("models/bikes.json"));
    }

    @Test
    @WithAnonymousUser
    void test_fetchAllBikes_unauthorized() throws Exception {
        this.mvc.perform(get(BASE_URL+"/all"))
                .andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    @WithMockUser(username = "user", roles = "TEST")
    void test_fetchAllBikes_forbidden() throws Exception {
        this.mvc.perform(get(BASE_URL + "/all"))
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_fetchAllBikes_ok() throws Exception {
        when(bikeService.findAll()).thenReturn(entitiesList);
        this.mvc.perform(get(BASE_URL + "/all"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data").isArray(),
                        jsonPath("$.data.length()").value(entitiesList.size()),
                        // fields included in Bike.BikeList
                        jsonPath("$.data[0].id").exists(),
                        jsonPath("$.data[0].name").exists(),
                        jsonPath("$.data[0].brand").exists(),
                        jsonPath("$.data[0].model").exists(),
                        jsonPath("$.data[0].serialNumber").exists(),
                        jsonPath("$.data[0].notes").exists(),
                        jsonPath("$.data[0].isPublic").exists(),
                        // fields that should NOT be present in Bike.BikeList (BikeDetail-only)
                        jsonPath("$.data[0].ownerId").doesNotExist(),
                        jsonPath("$.data[0].year").doesNotExist(),
                        jsonPath("$.data[0].type").doesNotExist(),
                        jsonPath("$.data[0].status").doesNotExist(),
                        jsonPath("$.data[0].components").doesNotExist(),
                        // auditable fields should not be included in Bike.BikeList view
                        jsonPath("$.data[0].createdAt").doesNotExist(),
                        jsonPath("$.data[0].createdBy").doesNotExist(),
                        jsonPath("$.data[0].updatedAt").doesNotExist(),
                        jsonPath("$.data[0].updatedBy").doesNotExist()
                );
    }

    @Test
    void test_getById_ok() throws Exception {
        when(bikeService.findById(any())).thenReturn(entitiesList.getFirst());
        this.mvc.perform(get(BASE_URL + "/" + TEST_UUID))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        // data is a single object for getById
                        jsonPath("$.data.id").exists(),
                        // fields included in Bike.BikeList (inherited by BikeDetail)
                        jsonPath("$.data.name").exists(),
                        jsonPath("$.data.brand").exists(),
                        jsonPath("$.data.model").exists(),
                        jsonPath("$.data.serialNumber").exists(),
                        jsonPath("$.data.notes").exists(),
                        jsonPath("$.data.isPublic").exists(),

                        // BikeDetail-specific fields
                        jsonPath("$.data.ownerId").exists(),
                        jsonPath("$.data.year").exists(),

                        // type/status should expose only the label according to JsonView
                        jsonPath("$.data.type.label").exists(),
                        jsonPath("$.data.type.id").doesNotExist(),
                        jsonPath("$.data.type.code").doesNotExist(),

                        jsonPath("$.data.status.label").exists(),
                        jsonPath("$.data.status.id").doesNotExist(),
                        jsonPath("$.data.status.code").doesNotExist(),

                        // components collection
                        jsonPath("$.data.components").isArray(),
                        jsonPath("$.data.components[0].id").exists(),
                        jsonPath("$.data.components[0].name").exists(),
                        jsonPath("$.data.components[0].brand").exists(),
                        jsonPath("$.data.components[0].model").exists(),
                        // componentType should expose only label
                        jsonPath("$.data.components[0].componentType.label").exists(),
                        jsonPath("$.data.components[0].componentType.id").doesNotExist(),
                        jsonPath("$.data.components[0].componentType.code").doesNotExist(),

                        // auditable fields should NOT be included in the JsonView for BikeDetail
                        jsonPath("$.data.createdAt").doesNotExist(),
                        jsonPath("$.data.createdBy").doesNotExist(),
                        jsonPath("$.data.updatedAt").doesNotExist(),
                        jsonPath("$.data.updatedBy").doesNotExist()
                );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_create_ok() throws Exception {
        JsonObjectBuilder<CreateBikeRequest> createBikeRequestMapper = new JsonObjectBuilder<>(CreateBikeRequest.class);
        String body = createBikeRequestMapper.loadJsonData("requests/create_bike_request.json");

        when(bikeService.createBike(any())).thenReturn(entitiesList.getFirst());

        this.mvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().exists(LOCATION)
                );
    }

    @Test
    void test_createForCustomer_ok() throws Exception {
        JsonObjectBuilder<CreateBikeRequest> createBikeRequestMapper = new JsonObjectBuilder<>(CreateBikeRequest.class);
        String body = createBikeRequestMapper.loadJsonData("requests/create_bike_request.json");

        when(bikeService.createBikeForCustomer(any(), any())).thenReturn(entitiesList.getFirst());

        this.mvc.perform(post(BASE_URL + "/" + TEST_UUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().exists(LOCATION),
                        // ApiResponse wrapper
                        jsonPath("$.message").value(SUCCESS),
                        jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE),
                        jsonPath("$.data").isMap()
                );
    }

    @Test
    void test_create_invalid_data() throws Exception {
        JsonObjectBuilder<CreateBikeRequest> createBikeRequestMapper = new JsonObjectBuilder<>(CreateBikeRequest.class);
        String body = createBikeRequestMapper.loadJsonData("requests/create_bike_invalid_request.json");

        this.mvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation failed: name: Bike name is required"),
                        jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.value())
                );
    }

    @Test
    void test_update_ok() throws Exception {
        JsonObjectBuilder<UpdateBikeRequest> updateBikeRequestMapper = new JsonObjectBuilder<>(UpdateBikeRequest.class);
        String body = updateBikeRequestMapper.loadJsonData("requests/update_bike_request.json");

        when(bikeService.updateBike(any(), any(UpdateBikeRequest.class))).thenReturn(entitiesList.getFirst());

        this.mvc.perform(put(BASE_URL + "/" + TEST_UUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(SUCCESS),
                        jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE),
                        jsonPath("$.data").isMap()
                );
    }

    @Test
    void test_update_record_not_found() throws Exception {
        JsonObjectBuilder<UpdateBikeRequest> updateBikeRequestMapper = new JsonObjectBuilder<>(UpdateBikeRequest.class);
        String body = updateBikeRequestMapper.loadJsonData("requests/update_bike_request.json");

        when(bikeService.updateBike(any(), any(UpdateBikeRequest.class))).thenThrow(new RecordNotFoundException());

        this.mvc.perform(put(BASE_URL + "/" + TEST_UUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        MockMvcResultMatchers.jsonPath("$.message").value("Record not found."),
                        MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value())
                );
    }

    @Test
    void test_delete_ok() throws Exception {
        doNothing().when(bikeService).softDeleteBike(any());

        this.mvc.perform(delete(BASE_URL + "/" + TEST_UUID)
                        .with(csrf()))
                .andDo(print())
                .andExpectAll(
                        status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_addComponent_ok() throws Exception {
        JsonObjectBuilder<CreateComponentRequest> createComponentRequestMapper = new JsonObjectBuilder<>(CreateComponentRequest.class);
        String body = createComponentRequestMapper.loadJsonData("requests/create_component_request.json");

        when(bikeService.addComponent(any(), any(CreateComponentRequest.class))).thenReturn(entitiesList.getFirst().getComponents().getFirst());

        this.mvc.perform(post(BASE_URL + "/" + TEST_UUID + "/components")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().exists(LOCATION)
                );
    }

}
