package com.quetoquenana.pedalpal.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoreLocationRequest {
    private String name;
    private String website;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
}

