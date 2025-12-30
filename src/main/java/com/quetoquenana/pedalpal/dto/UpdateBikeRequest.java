package com.quetoquenana.pedalpal.dto;

import com.quetoquenana.pedalpal.model.BikeStatus;
import com.quetoquenana.pedalpal.model.BikeType;
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
    private BikeType type;
    private String serialNumber;
    private String notes;
    private BikeStatus status; // allow updating status (including soft-delete)
}

