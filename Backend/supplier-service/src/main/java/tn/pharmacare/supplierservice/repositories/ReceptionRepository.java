package tn.pharmacare.supplierservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pharmacare.supplierservice.entities.Reception;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {
    boolean existsByCommandeId(Long commandeId);
}
