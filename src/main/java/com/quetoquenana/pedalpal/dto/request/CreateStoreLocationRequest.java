package com.quetoquenana.pedalpal.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreLocationRequest {

    @Size(max = 100, message = "{store.location.name.max}")
    private String name;

    @Size(max = 200, message = "{store.location.website.max}")
    private String website;

    @Size(max = 1000, message = "{store.location.address.max}")
    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Size(max = 100, message = "{store.location.phone.max}")
    private String phone;
}

