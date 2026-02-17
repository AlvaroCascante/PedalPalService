package com.quetoquenana.pedalpal.presentation.dto.api.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentTaskRequest {
    private String name;

    @Size(max = 255, message = "{appointment.task.description.max}")
    private String description;
}

