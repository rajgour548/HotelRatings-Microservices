package com.raj.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/userFallback")
    public String userFallback() {
        return "User Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/hotelFallback")
    public String hotelFallback() {
        return "Hotel Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/ratingFallback")
    public String ratingFallback() {
        return "Rating Service is currently unavailable. Please try again later.";
    }
}
