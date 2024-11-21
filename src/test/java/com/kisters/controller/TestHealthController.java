package com.kisters.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestHealthController {

    @GetMapping("/health")
    public String health() {
        return "Service is running";
    }
}