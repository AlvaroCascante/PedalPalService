package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateComponentRequest;
import com.quetoquenana.pedalpal.model.local.BikeComponent;
import com.quetoquenana.pedalpal.service.BikeComponentService;
import com.quetoquenana.pedalpal.util.JsonObjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.ResponseValues.DEFAULT_ERROR_CODE;
import static com.quetoquenana.pedalpal.util.Constants.ResponseValues.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BikeComponentController.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "admin", roles = "ADMIN")
class BikeComponentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BikeComponentService componentService;

    private final String BASE_URL = "/v1/api/components";

    private final LinkedList<BikeComponent> entitiesList = new LinkedList<>();

    private final UUID TEST_UUID = UUID.fromString("f1e2d3c4-b5a6-47d8-9c0b-111122223333");

    @BeforeEach
    void setup() throws IOException {
        JsonObjectBuilder<BikeComponent> mapper = new JsonObjectBuilder<>(BikeComponent.class);
        entitiesList.addAll(mapper.loadListJsonFile("models/components.json"));
    }

    @Test
    @WithAnonymousUser
    void test_replace_forbidden() throws Exception {
        this.mvc.perform(put(BASE_URL + "/replace/" + TEST_UUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void test_replace_ok() throws Exception {
        JsonObjectBuilder<CreateComponentRequest> createComponentRequestMapper = new JsonObjectBuilder<>(CreateComponentRequest.class);
        String body = createComponentRequestMapper.loadJsonData("requests/replace_component_request.json");

        when(componentService.replaceComponent(any(), any(CreateComponentRequest.class))).thenReturn(entitiesList.getFirst());

        this.mvc.perform(put(BASE_URL + "/replace/" + TEST_UUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        // ApiResponse wrapper
                        jsonPath("$.message").value(SUCCESS),
                        jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE),
                        jsonPath("$.data").isMap(),
                        jsonPath("$.data.id").value(entitiesList.getFirst().getId().toString()),
                        jsonPath("$.data.name").value(entitiesList.getFirst().getName()),
                        jsonPath("$.data.brand").value(entitiesList.getFirst().getBrand()),
                        jsonPath("$.data.model").value(entitiesList.getFirst().getModel()),
                        jsonPath("$.data.componentType.label").exists()
                );
    }

    @Test
    void test_update_ok() throws Exception {
        JsonObjectBuilder<UpdateComponentRequest> updateComponentRequestMapper = new JsonObjectBuilder<>(UpdateComponentRequest.class);
        String body = updateComponentRequestMapper.loadJsonData("requests/update_component_request.json");

        when(componentService.updateComponent(any(), any(UpdateComponentRequest.class))).thenReturn(entitiesList.getFirst());

        this.mvc.perform(put(BASE_URL + "/" + TEST_UUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        // ApiResponse wrapper
                        jsonPath("$.message").value(SUCCESS),
                        jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE),
                        jsonPath("$.data").isMap()
                );
    }

    @Test
    void test_delete_ok() throws Exception {
        doNothing().when(componentService).removeComponent(any());

        this.mvc.perform(delete(BASE_URL + "/" + TEST_UUID)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
