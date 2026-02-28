package com.quetoquenana.pedalpal.common.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.quetoquenana.pedalpal.common.util.Constants.ResponseValues.DEFAULT_ERROR_CODE;
import static com.quetoquenana.pedalpal.common.util.Constants.ResponseValues.SUCCESS;

/**
 * Generic API response wrapper for REST endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String message;

    private Integer errorCode;

    private Object data;

    public ApiResponse(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.data = null;
    }

    public ApiResponse(Object data) {
        this.message = SUCCESS;
        this.errorCode = DEFAULT_ERROR_CODE;
        this.data = data;
    }
}

