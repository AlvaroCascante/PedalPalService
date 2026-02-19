package com.quetoquenana.pedalpal.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentTaskRequest {
    @NotBlank(message = "{appointment.task.name.required}")
    private String name;

    @Size(max = 255, message = "{appointment.task.description.max}")
    private String description;
}

