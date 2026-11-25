package tn.pharmacare.supplierservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.pharmacare.supplierservice.entities.Fournisseur;
import tn.pharmacare.supplierservice.services.FournisseurService;

import java.util.List;

/** CRUD Fournisseurs : /api/suppliers */
@RestController
@RequestMapping("/api/suppliers")
public class FournisseurController {

    private final FournisseurService service;

    public FournisseurController(FournisseurService service) { this.service = service; }

    // GET /api/suppliers            GET /api/suppliers?nom=medi     GET /api/suppliers?actif=true
    @GetMapping
    public List<Fournisseur> getAll(@RequestParam(name = "nom", required = false) String nom,
                                    @RequestParam(name = "actif", required = false) Boolean actif) {
        return service.findAll(nom, actif);
    }

    @GetMapping("/{id}")
    public Fournisseur getById(@PathVariable("id") Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Fournisseur> create(@Valid @RequestBody Fournisseur f) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(f));
    }

    @PutMapping("/{id}")
    public Fournisseur update(@PathVariable("id") Long id, @Valid @RequestBody Fournisseur f) {
        return service.update(id, f);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}