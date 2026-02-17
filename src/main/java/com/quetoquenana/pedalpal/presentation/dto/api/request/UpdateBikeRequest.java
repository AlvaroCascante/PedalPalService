package com.quetoquenana.pedalpal.presentation.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBikeRequest {
    private String name;
    private String brand;
    private String model;
    private Integer year;
    private String type;
    private String serialNumber;
    private String notes;
    private String status;
    private boolean isPublic;
}


