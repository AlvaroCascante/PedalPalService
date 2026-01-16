package com.quetoquenana.pedalpal.controller;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/component-types")
public class ComponentTypeController {

    private final MessageSource messageSource;

    public ComponentTypeController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}

