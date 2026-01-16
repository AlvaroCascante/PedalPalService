package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.data.Bike;
import com.quetoquenana.pedalpal.service.BikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BikeControllerTest {

    @Mock
    private BikeService bikeService;

    private BikeController controller;

    @BeforeEach
    void setUp() {
        controller = new BikeController(bikeService);
    }

    @Test
    void fetchAllBikes_shouldCallService_andReturnOk() {
        List<Bike> bikes = List.of(mock(Bike.class));
        when(bikeService.findAll()).thenReturn(bikes);

        ResponseEntity<ApiResponse> response = controller.fetchAllBikes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bikeService, times(1)).findAll();
    }

    @Test
    void fetchActiveBikes_shouldCallService_andReturnOk() {
        List<Bike> bikes = List.of(mock(Bike.class));
        when(bikeService.findActive()).thenReturn(bikes);

        ResponseEntity<ApiResponse> response = controller.fetchActiveBikes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bikeService, times(1)).findActive();
    }

    @Test
    void getById_shouldCallService_andReturnOk() {
        UUID id = UUID.randomUUID();
        Bike bike = mock(Bike.class);
        when(bikeService.findById(id)).thenReturn(bike);

        ResponseEntity<ApiResponse> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bikeService, times(1)).findById(id);
    }

    @Test
    void create_shouldCallService_andReturnCreatedWithLocation() {
        CreateBikeRequest request = mock(CreateBikeRequest.class);
        Bike saved = mock(Bike.class);
        UUID id = UUID.randomUUID();
        when(saved.getId()).thenReturn(id);
        when(bikeService.createBike(request)).thenReturn(saved);

        ResponseEntity<Bike> response = controller.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("/api/bikes/" + id, Objects.requireNonNull(response.getHeaders().getLocation()).toString());
        verify(bikeService, times(1)).createBike(request);
    }

    @Test
    void updateBike_shouldCallService_andReturnOk() {
        UUID id = UUID.randomUUID();
        UpdateBikeRequest request = mock(UpdateBikeRequest.class);
        Bike updated = mock(Bike.class);
        when(bikeService.updateBike(id, request)).thenReturn(updated);

        ResponseEntity<Bike> response = controller.updateBike(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bikeService, times(1)).updateBike(id, request);
    }

    @Test
    void deleteBike_shouldCallService_andReturnNoContent() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = controller.deleteBike(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bikeService, times(1)).softDeleteBike(id);
    }
}