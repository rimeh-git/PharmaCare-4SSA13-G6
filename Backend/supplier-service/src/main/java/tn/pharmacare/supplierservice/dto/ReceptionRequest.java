package tn.pharmacare.supplierservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Corps JSON pour enregistrer la réception d'une commande. */
public record ReceptionRequest(
        @NotNull Long commandeId,
        LocalDate dateReception,
        boolean conforme,
        String observations) {}
