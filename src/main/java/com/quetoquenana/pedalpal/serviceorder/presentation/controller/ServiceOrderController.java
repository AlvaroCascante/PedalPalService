package com.quetoquenana.pedalpal.serviceorder.presentation.controller;

import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.serviceorder.application.command.AddServiceOrderCommentCommand;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.application.query.GetServiceOrderCommentsQueryService;
import com.quetoquenana.pedalpal.serviceorder.application.query.ServiceOrderQueryService;
import com.quetoquenana.pedalpal.serviceorder.application.result.ChangeServiceOrderStatusResult;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderCommentResult;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.AddServiceOrderCommentUseCase;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.ChangeServiceOrderStatusUseCase;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.request.AddServiceOrderCommentRequest;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.request.ChangeServiceOrderStatusRequest;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ChangeServiceOrderStatusResponse;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ServiceOrderCommentResponse;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ServiceOrderResponse;
import com.quetoquenana.pedalpal.serviceorder.presentation.mapper.ServiceOrderApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST endpoints for service orders.
 */
@RestController
@RequestMapping("/v1/api/service-orders")
@RequiredArgsConstructor
@Slf4j
public class ServiceOrderController {

    private final ChangeServiceOrderStatusUseCase changeServiceOrderStatusUseCase;
    private final AddServiceOrderCommentUseCase addServiceOrderCommentUseCase;
    private final GetServiceOrderCommentsQueryService commentQueryService;
    private final ServiceOrderApiMapper apiMapper;
    private final ServiceOrderQueryService queryService;

    /**
     * Changes the status of a service order.
     */
    @PostMapping("/{id}/status")
    @PreAuthorize("(hasRole('ADMIN')) or (hasRole('TECHNICIAN'))")
    public ResponseEntity<ApiResponse> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeServiceOrderStatusRequest request
    ) {
        log.info("POST /v1/api/service-orders/{}/status Received request to change service order status: {}", id, request);

        ChangeServiceOrderStatusCommand command = apiMapper.toCommand(id, request);
        ChangeServiceOrderStatusResult result = changeServiceOrderStatusUseCase.execute(command);
        ChangeServiceOrderStatusResponse response = apiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    /**
     * Retrieves a service order by id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable UUID id
    ) {
        ServiceOrderResult result = queryService.getById(id);
        ServiceOrderResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    /**
     * Retrieves a service order by id.
     */
    @GetMapping("/bike/{bikeId}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getByBikeId(
            @PathVariable UUID bikeId
    ) {
        List<ServiceOrderResult> result = queryService.getByBikeId(bikeId);
        List<ServiceOrderResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    /**
     * Adds a comment to a service order.
     */
    @PostMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> addComment(
            @PathVariable UUID id,
            @Valid @RequestBody AddServiceOrderCommentRequest request
    ) {
        log.info("POST /v1/api/service-orders/{}/comments called", id);

        AddServiceOrderCommentCommand command = apiMapper.toCommand(id, request);
        ServiceOrderCommentResult result = addServiceOrderCommentUseCase.execute(command);
        ServiceOrderCommentResponse response = apiMapper.toResponse(result);

        return ResponseEntity.ok(new ApiResponse(response));
    }

    /**
     * Retrieves comments for a service order.
     */
    @GetMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getComments(
            @PathVariable UUID id
    ) {
        log.info("GET /v1/api/service-orders/{}/comments called", id);

        List<ServiceOrderCommentResult> results = commentQueryService.getByServiceOrderId(id);
        List<ServiceOrderCommentResponse> responses = results.stream().map(apiMapper::toResponse).toList();

        return ResponseEntity.ok(new ApiResponse(responses));
    }
}
