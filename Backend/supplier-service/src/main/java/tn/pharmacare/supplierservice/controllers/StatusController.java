package tn.pharmacare.supplierservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Permet de voir quelle instance répond (utile avec 2 instances sur 8085 / 8095). */
@RestController
public class StatusController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/api/suppliers/status")
    public Map<String, String> status() {
        return Map.of("service", "supplier-service", "status", "UP", "port", port);
    }
}
