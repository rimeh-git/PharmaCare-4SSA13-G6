package tn.pharmacare.supplierservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.pharmacare.supplierservice.dto.CommandeRequest;
import tn.pharmacare.supplierservice.dto.LigneRequest;
import tn.pharmacare.supplierservice.entities.*;
import tn.pharmacare.supplierservice.exceptions.BusinessException;
import tn.pharmacare.supplierservice.exceptions.ResourceNotFoundException;
import tn.pharmacare.supplierservice.repositories.CommandeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CommandeService {

    private final CommandeRepository repo;
    private final FournisseurService fournisseurService;

    public CommandeService(CommandeRepository repo, FournisseurService fournisseurService) {
        this.repo = repo;
        this.fournisseurService = fournisseurService;
    }

    @Transactional(readOnly = true)
    public List<CommandeApprovisionnement> findAll(Long fournisseurId, StatutCommande statut) {
        if (fournisseurId != null) return repo.findByFournisseurId(fournisseurId);
        if (statut != null) return repo.findByStatut(statut);
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public CommandeApprovisionnement findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : id=" + id));
    }

    public CommandeApprovisionnement create(CommandeRequest req) {
        Fournisseur f = fournisseurService.findById(req.fournisseurId());
        if (!f.isActif()) throw new BusinessException("Le fournisseur " + f.getNom() + " est inactif");

        CommandeApprovisionnement c = new CommandeApprovisionnement();
        c.setReference("CMD-" + System.currentTimeMillis());
        c.setDateCommande(LocalDate.now());
        c.setDateLivraisonPrevue(req.dateLivraisonPrevue());
        c.setStatut(StatutCommande.EN_ATTENTE);
        c.setFournisseur(f);
        remplirLignes(c, req.lignes());
        return repo.save(c);
    }

    public CommandeApprovisionnement update(Long id, CommandeRequest req) {
        CommandeApprovisionnement c = findById(id);
        if (c.getStatut() != StatutCommande.EN_ATTENTE)
            throw new BusinessException("Seule une commande EN_ATTENTE peut être modifiée");
        c.setFournisseur(fournisseurService.findById(req.fournisseurId()));
        c.setDateLivraisonPrevue(req.dateLivraisonPrevue());
        c.getLignes().clear();
        remplirLignes(c, req.lignes());
        return repo.save(c);
    }

    public CommandeApprovisionnement changerStatut(Long id, StatutCommande statut) {
        CommandeApprovisionnement c = findById(id);
        if (c.getStatut() == StatutCommande.RECUE || c.getStatut() == StatutCommande.ANNULEE)
            throw new BusinessException("Commande déjà clôturée (" + c.getStatut() + ")");
        if (statut == StatutCommande.RECUE)
            throw new BusinessException("Utilisez POST /api/suppliers/receptions pour réceptionner une commande");
        c.setStatut(statut);
        return repo.save(c);
    }

    public void delete(Long id) {
        CommandeApprovisionnement c = findById(id);
        if (c.getStatut() == StatutCommande.RECUE)
            throw new BusinessException("Impossible de supprimer une commande déjà reçue");
        repo.delete(c);
    }

    private void remplirLignes(CommandeApprovisionnement c, List<LigneRequest> lignes) {
        for (LigneRequest l : lignes) {
            c.ajouterLigne(new LigneCommande(l.productId(), l.nomProduit(), l.quantite(), l.prixUnitaire()));
        }
        c.recalculerMontant();
    }
}
