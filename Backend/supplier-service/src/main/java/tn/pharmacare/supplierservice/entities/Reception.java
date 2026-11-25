package tn.pharmacare.supplierservice.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Réception de la marchandise d'une commande d'approvisionnement.
 * Association : 1 Reception <---> 1 CommandeApprovisionnement (@OneToOne)
 */
@Entity
@Table(name = "receptions")
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateReception;
    private boolean conforme;       // marchandise conforme à la commande ?
    private String observations;

    @OneToOne
    @JoinColumn(name = "commande_id", unique = true)
    private CommandeApprovisionnement commande;

    public Reception() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDateReception() { return dateReception; }
    public void setDateReception(LocalDate dateReception) { this.dateReception = dateReception; }
    public boolean isConforme() { return conforme; }
    public void setConforme(boolean conforme) { this.conforme = conforme; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public CommandeApprovisionnement getCommande() { return commande; }
    public void setCommande(CommandeApprovisionnement commande) { this.commande = commande; }
}
