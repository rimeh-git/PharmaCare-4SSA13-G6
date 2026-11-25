package tn.pharmacare.supplierservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.pharmacare.supplierservice.dto.ReceptionRequest;
import tn.pharmacare.supplierservice.entities.Reception;
import tn.pharmacare.supplierservice.services.ReceptionService;

import java.util.List;

/** CRUD Réceptions : /api/suppliers/receptions */
@RestController
@RequestMapping("/api/suppliers/receptions")
public class ReceptionController {

    private final ReceptionService service;

    public ReceptionController(ReceptionService service) { this.service = service; }

    @GetMapping
    public List<Reception> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Reception getById(@PathVariable("id") Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Reception> create(@Valid @RequestBody ReceptionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public Reception update(@PathVariable("id") Long id, @RequestBody ReceptionRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}