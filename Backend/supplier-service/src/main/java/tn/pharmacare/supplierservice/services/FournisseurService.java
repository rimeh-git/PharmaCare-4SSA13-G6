package tn.pharmacare.supplierservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.pharmacare.supplierservice.entities.Fournisseur;
import tn.pharmacare.supplierservice.exceptions.BusinessException;
import tn.pharmacare.supplierservice.exceptions.ResourceNotFoundException;
import tn.pharmacare.supplierservice.repositories.FournisseurRepository;

import java.util.List;

@Service
@Transactional
public class FournisseurService {

    private final FournisseurRepository repo;

    public FournisseurService(FournisseurRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<Fournisseur> findAll(String nom, Boolean actif) {
        if (nom != null && !nom.isBlank()) return repo.findByNomContainingIgnoreCase(nom);
        if (Boolean.TRUE.equals(actif)) return repo.findByActifTrue();
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Fournisseur findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur introuvable : id=" + id));
    }

    public Fournisseur create(Fournisseur f) {
        if (f.getMatriculeFiscal() != null && repo.existsByMatriculeFiscal(f.getMatriculeFiscal()))
            throw new BusinessException("Matricule fiscal déjà utilisé : " + f.getMatriculeFiscal());
        f.setId(null);
        return repo.save(f);
    }

    public Fournisseur update(Long id, Fournisseur data) {
        Fournisseur f = findById(id);
        f.setNom(data.getNom());
        f.setEmail(data.getEmail());
        f.setTelephone(data.getTelephone());
        f.setAdresse(data.getAdresse());
        f.setVille(data.getVille());
        f.setMatriculeFiscal(data.getMatriculeFiscal());
        f.setActif(data.isActif());
        return repo.save(f);
    }

    public void delete(Long id) {
        repo.delete(findById(id));
    }
}
