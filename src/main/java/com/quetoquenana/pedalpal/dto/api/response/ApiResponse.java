package com.quetoquenana.pedalpal.dto.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.quetoquenana.pedalpal.util.Constants.ResponseValues.DEFAULT_ERROR_CODE;
import static com.quetoquenana.pedalpal.util.Constants.ResponseValues.SUCCESS;

/**
 * Generic API response wrapper for REST endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    @JsonView(ApiBaseResponseView.Always.class)
    private String message;

    @JsonView(ApiBaseResponseView.Always.class)
    private Integer errorCode;

    @JsonView(ApiBaseResponseView.Always.class)
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

