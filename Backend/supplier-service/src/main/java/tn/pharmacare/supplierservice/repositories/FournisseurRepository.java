package tn.pharmacare.supplierservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pharmacare.supplierservice.entities.Fournisseur;

import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    List<Fournisseur> findByActifTrue();
    List<Fournisseur> findByNomContainingIgnoreCase(String nom);
    boolean existsByMatriculeFiscal(String matriculeFiscal);
}
