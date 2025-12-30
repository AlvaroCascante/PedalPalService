package com.quetoquenana.pedalpal.dto;

import com.quetoquenana.pedalpal.model.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComponentRequest {
    private ComponentType componentType;
    private String name;
    private String brand;
    private String model;
    private String notes;
}

