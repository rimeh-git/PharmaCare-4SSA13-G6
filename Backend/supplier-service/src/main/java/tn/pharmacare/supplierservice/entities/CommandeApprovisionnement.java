package tn.pharmacare.supplierservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Commande d'approvisionnement passée à un fournisseur.
 * Associations :
 *   N Commande ---> 1 Fournisseur          (@ManyToOne)
 *   1 Commande ---> N LigneCommande         (@OneToMany)
 *   1 Commande ---> 0..1 Reception          (@OneToOne)
 */
@Entity
@Table(name = "commandes_approvisionnement")
public class CommandeApprovisionnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    private LocalDate dateCommande;
    private LocalDate dateLivraisonPrevue;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    private double montantTotal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fournisseur_id")
    @JsonIgnoreProperties({"commandes"})
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"commande"})
    private List<LigneCommande> lignes = new ArrayList<>();

    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"commande"})
    private Reception reception;

    public CommandeApprovisionnement() {}

    /** Ajoute une ligne en gardant les deux côtés de l'association cohérents. */
    public void ajouterLigne(LigneCommande ligne) {
        ligne.setCommande(this);
        this.lignes.add(ligne);
    }

    public void recalculerMontant() {
        this.montantTotal = lignes.stream()
                .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
                .sum();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }
    public LocalDate getDateLivraisonPrevue() { return dateLivraisonPrevue; }
    public void setDateLivraisonPrevue(LocalDate dateLivraisonPrevue) { this.dateLivraisonPrevue = dateLivraisonPrevue; }
    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }
    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
    public Reception getReception() { return reception; }
    public void setReception(Reception reception) { this.reception = reception; }
}
