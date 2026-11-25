package tn.pharmacare.supplierservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pharmacare.supplierservice.entities.CommandeApprovisionnement;
import tn.pharmacare.supplierservice.entities.StatutCommande;

import java.util.List;

public interface CommandeRepository extends JpaRepository<CommandeApprovisionnement, Long> {
    List<CommandeApprovisionnement> findByFournisseurId(Long fournisseurId);
    List<CommandeApprovisionnement> findByStatut(StatutCommande statut);
}
