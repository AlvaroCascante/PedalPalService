package com.quetoquenana.pedalpal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.announcement.presentation.mapper.AnnouncementApiMapper;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.presentation.mapper.AppointmentApiMapper;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.presentation.mapper.BikeApiMapper;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.presentation.mapper.MediaApiMapper;
import com.quetoquenana.pedalpal.product.application.mapper.ProductMapper;
import com.quetoquenana.pedalpal.product.presentation.mapper.ProductApiMapper;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.presentation.mapper.ServiceOrderApiMapper;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.presentation.mapper.StoreApiMapper;
import com.quetoquenana.pedalpal.systemCode.application.mapper.SystemCodeMapper;
import com.quetoquenana.pedalpal.systemCode.presentation.mapper.SystemCodeApiMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public AnnouncementApiMapper createAnnouncementApiMapper(MessageSource messageSource) {
        return new AnnouncementApiMapper(messageSource);
    }

    @Bean
    public AnnouncementMapper createAnnouncementMapper() {
        return new AnnouncementMapper();
    }

    @Bean
    public AppointmentApiMapper createAppointmentApiMapper(MessageSource messageSource) {
        return new AppointmentApiMapper(messageSource);
    }

    @Bean
    public AppointmentMapper createAppointmentMapper() {
        return new AppointmentMapper();
    }

    @Bean
    public BikeApiMapper createBikeApiMapper(MessageSource messageSource) {
        return new BikeApiMapper(messageSource);
    }

    @Bean
    public BikeMapper createBikeMapper(ObjectMapper objectMapper) {
        return new BikeMapper(objectMapper);
    }

    @Bean
    public MediaApiMapper createMediaApiMapper() {
        return new MediaApiMapper();
    }

    @Bean
    public MediaMapper createMediaMapper() {
        return new MediaMapper();
    }

    @Bean
    public ProductApiMapper createProductApiMapper(MessageSource messageSource) {
        return new ProductApiMapper(messageSource);
    }

    @Bean
    public ProductMapper createProductMapper() {
        return new ProductMapper();
    }

    @Bean
    public ServiceOrderApiMapper createServiceOrderApiMapper(MessageSource messageSource) {
        return new ServiceOrderApiMapper(messageSource);
    }

    @Bean
    public ServiceOrderMapper createServiceOrderMapper() {
        return new ServiceOrderMapper();
    }


    @Bean
    public StoreApiMapper createStoreApiMapper(MessageSource messageSource) {
        return new StoreApiMapper(messageSource);
    }

    @Bean
    public StoreMapper createStoreMapper() {
        return new StoreMapper();
    }

    @Bean
    public SystemCodeApiMapper createSystemCodeApiMapper(MessageSource messageSource) {
        return new SystemCodeApiMapper(messageSource);
    }

    @Bean
    public SystemCodeMapper createSystemCodeMapper() {
        return new SystemCodeMapper();
    }
}
