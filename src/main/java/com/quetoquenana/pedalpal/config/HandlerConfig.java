package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.appointment.application.handler.CompletedUpdatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.application.handler.ConfirmCreatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.application.handler.InProgressUpdatesServiceOrderHandler;
import com.quetoquenana.pedalpal.serviceorder.application.handler.CompleteWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.application.handler.StartWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.application.port.CompleteAppointmentPort;
import com.quetoquenana.pedalpal.serviceorder.application.util.ServiceOrderNumberGenerator;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class HandlerConfig {

    @Bean
    public CompletedUpdatesServiceOrderHandler createCompletedUpdatesServiceOrderHandler(
            ServiceOrderRepository serviceOrderRepository,
            Clock clock
    ) {
        return new CompletedUpdatesServiceOrderHandler(
                serviceOrderRepository,
                clock
        );
    }

    @Bean
    public ConfirmCreatesServiceOrderHandler createConfirmCreatesServiceOrderHandler(
            ServiceOrderRepository serviceOrderRepository,
            ServiceOrderNumberGenerator numberGenerator,
            Clock clock
    ) {
        return new ConfirmCreatesServiceOrderHandler(
                serviceOrderRepository,
                numberGenerator,
                clock
        );
    }

    @Bean
    public InProgressUpdatesServiceOrderHandler createInProgressUpdatesServiceOrderHandler(
            ServiceOrderRepository serviceOrderRepository,
            Clock clock
    ) {
        return new InProgressUpdatesServiceOrderHandler(
                serviceOrderRepository,
                clock
        );
    }

    @Bean
    public StartWorkHandler createStartWorkHandler(Clock clock) {
        return new StartWorkHandler(clock);
    }

    @Bean
    public CompleteWorkHandler createCompleteWorkHandler(
            CompleteAppointmentPort completeAppointmentPort,
            Clock clock
    ) {
        return new CompleteWorkHandler(completeAppointmentPort, clock);
    }
}
