package tn.pharmacare.supplierservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.pharmacare.supplierservice.dto.CommandeRequest;
import tn.pharmacare.supplierservice.entities.CommandeApprovisionnement;
import tn.pharmacare.supplierservice.entities.StatutCommande;
import tn.pharmacare.supplierservice.services.CommandeService;

import java.util.List;

/** CRUD Commandes d'approvisionnement : /api/suppliers/commandes */
@RestController
@RequestMapping("/api/suppliers/commandes")
public class CommandeController {

    private final CommandeService service;

    public CommandeController(CommandeService service) { this.service = service; }

    // GET /api/suppliers/commandes   ?fournisseurId=1   ?statut=EN_ATTENTE
    @GetMapping
    public List<CommandeApprovisionnement> getAll(@RequestParam(name = "fournisseurId", required = false) Long fournisseurId,
                                                  @RequestParam(name = "statut", required = false) StatutCommande statut) {
        return service.findAll(fournisseurId, statut);
    }

    @GetMapping("/{id}")
    public CommandeApprovisionnement getById(@PathVariable("id") Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<CommandeApprovisionnement> create(@Valid @RequestBody CommandeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public CommandeApprovisionnement update(@PathVariable("id") Long id, @Valid @RequestBody CommandeRequest req) {
        return service.update(id, req);
    }

    // PATCH /api/suppliers/commandes/1/statut?valeur=VALIDEE
    @PatchMapping("/{id}/statut")
    public CommandeApprovisionnement changerStatut(@PathVariable("id") Long id, @RequestParam("valeur") StatutCommande valeur) {
        return service.changerStatut(id, valeur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}