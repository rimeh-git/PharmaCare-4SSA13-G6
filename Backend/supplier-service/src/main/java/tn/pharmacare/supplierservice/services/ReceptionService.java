package tn.pharmacare.supplierservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.pharmacare.supplierservice.dto.ReceptionRequest;
import tn.pharmacare.supplierservice.entities.CommandeApprovisionnement;
import tn.pharmacare.supplierservice.entities.Reception;
import tn.pharmacare.supplierservice.entities.StatutCommande;
import tn.pharmacare.supplierservice.exceptions.BusinessException;
import tn.pharmacare.supplierservice.exceptions.ResourceNotFoundException;
import tn.pharmacare.supplierservice.repositories.ReceptionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ReceptionService {

    private final ReceptionRepository repo;
    private final CommandeService commandeService;

    public ReceptionService(ReceptionRepository repo, CommandeService commandeService) {
        this.repo = repo;
        this.commandeService = commandeService;
    }

    @Transactional(readOnly = true)
    public List<Reception> findAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Reception findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réception introuvable : id=" + id));
    }

    /** Réceptionne une commande VALIDEE : crée la réception et passe la commande à RECUE. */
    public Reception create(ReceptionRequest req) {
        CommandeApprovisionnement c = commandeService.findById(req.commandeId());
        if (repo.existsByCommandeId(c.getId()))
            throw new BusinessException("Cette commande a déjà été réceptionnée");
        if (c.getStatut() != StatutCommande.VALIDEE)
            throw new BusinessException("Seule une commande VALIDEE peut être réceptionnée (statut actuel : " + c.getStatut() + ")");

        Reception r = new Reception();
        r.setCommande(c);
        r.setDateReception(req.dateReception() != null ? req.dateReception() : LocalDate.now());
        r.setConforme(req.conforme());
        r.setObservations(req.observations());
        c.setReception(r);
        c.setStatut(StatutCommande.RECUE);
        return repo.save(r);
    }

    public Reception update(Long id, ReceptionRequest req) {
        Reception r = findById(id);
        if (req.dateReception() != null) r.setDateReception(req.dateReception());
        r.setConforme(req.conforme());
        r.setObservations(req.observations());
        return repo.save(r);
    }

    /** Supprimer une réception remet la commande au statut VALIDEE. */
    public void delete(Long id) {
        Reception r = findById(id);
        CommandeApprovisionnement c = r.getCommande();
        c.setReception(null);
        c.setStatut(StatutCommande.VALIDEE);
        repo.delete(r);
    }
}
