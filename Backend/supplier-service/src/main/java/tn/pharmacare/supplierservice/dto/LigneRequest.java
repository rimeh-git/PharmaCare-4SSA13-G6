package tn.pharmacare.supplierservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LigneRequest(
        @NotNull Long productId,
        String nomProduit,
        @Min(1) int quantite,
        @PositiveOrZero double prixUnitaire) {}
