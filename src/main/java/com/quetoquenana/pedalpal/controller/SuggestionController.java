package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.dto.api.request.CreateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.local.Suggestion;
import com.quetoquenana.pedalpal.service.SuggestionService;
import com.quetoquenana.pedalpal.util.JsonViewPageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE;
import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE_SIZE;

@RestController
@RequestMapping("/v1/api/suggestions")
@RequiredArgsConstructor
@Slf4j
public class SuggestionController {

    private final SuggestionService suggestionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchAll(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /v1/api/suggestions called");
        Page<Suggestion> entities = suggestionService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/bike/{bikeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> fetchByBikeId(@PathVariable UUID bikeId) {
        log.info("GET /v1/api/suggestions/bike/{} called", bikeId);
        List<Suggestion> list = suggestionService.findByBikeId(bikeId);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/type/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchBySuggestionType(@PathVariable String code) {
        log.info("GET /v1/api/suggestions/type/{} called", code);
        List<Suggestion> list = suggestionService.findBySuggestionType(code);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> searchByName(@RequestParam String q) {
        log.info("GET /v1/api/suggestions/search?q={} called", q);
        List<Suggestion> list = suggestionService.searchByName(q);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchByDateRange(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        log.info("GET /v1/api/suggestions/date-range?start={}&end={} called", start, end);
        List<Suggestion> list = suggestionService.findByDateRange(start, end);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/suggestions/{} called", id);
        Suggestion s = suggestionService.findById(id);
        return ResponseEntity.ok(new ApiResponse(s));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateSuggestionRequest request) {
        log.info("POST /v1/api/suggestions Received request to create suggestion: {}", request);
        Suggestion saved = suggestionService.createSuggestion(request);
        return ResponseEntity.created(URI.create("/v1/api/suggestions/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateSuggestionRequest request) {
        log.info("PUT /v1/api/suggestions/{} Received request to update suggestion: {}", id, request);
        Suggestion saved = suggestionService.updateSuggestion(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /v1/api/suggestions/{} called", id);
        suggestionService.deleteSuggestion(id);
        return ResponseEntity.noContent().build();
    }
}

