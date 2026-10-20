package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home", description = "Домашний API")
public class HomeController {
    @GetMapping("/")
    @Operation(summary = "Home endpoint")
    public String index() {
        return "Greetings!";
    }
}
