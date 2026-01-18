package com.quetoquenana.pedalpal.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComponentRequest {
    private String componentType;
    private String status;
    private String name;
    private String brand;
    private String model;
    private String notes;
}

