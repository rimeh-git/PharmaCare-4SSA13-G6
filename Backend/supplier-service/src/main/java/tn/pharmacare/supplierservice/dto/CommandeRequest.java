package tn.pharmacare.supplierservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/** Corps JSON pour créer ou modifier une commande d'approvisionnement. */
public record CommandeRequest(
        @NotNull Long fournisseurId,
        LocalDate dateLivraisonPrevue,
        @NotEmpty @Valid List<LigneRequest> lignes) {}
