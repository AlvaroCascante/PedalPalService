package com.quetoquenana.pedalpal.model.data;

import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentTaskRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "appointment_tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppointmentTask extends Auditable {

    // //JSON Views to control serialization responses
    public static class AppointmentTaskList extends ApiBaseResponseView.Always {}
    public static class AppointmentTaskDetail extends AppointmentTask.AppointmentTaskList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    public static AppointmentTask createFromRequest(CreateAppointmentTaskRequest request, Appointment appointment) {
        return AppointmentTask.builder()
                .appointment(appointment)
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public void updateFromRequest(UpdateAppointmentTaskRequest request) {
        if (request.getName() != null) this.name = request.getName();
        if (request.getDescription() != null) this.description = request.getDescription();
    }
}
