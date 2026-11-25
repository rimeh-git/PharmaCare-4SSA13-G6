package tn.pharmacare.supplierservice.entities;

import jakarta.persistence.*;

/**
 * Ligne d'une commande : un produit commandé avec sa quantité et son prix d'achat.
 * productId fait référence à un produit de product-service (pas de clé étrangère :
 * chaque microservice a sa propre base).
 */
@Entity
@Table(name = "lignes_commande")
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String nomProduit;
    private int quantite;
    private double prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private CommandeApprovisionnement commande;

    public LigneCommande() {}

    public LigneCommande(Long productId, String nomProduit, int quantite, double prixUnitaire) {
        this.productId = productId;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getNomProduit() { return nomProduit; }
    public void setNomProduit(String nomProduit) { this.nomProduit = nomProduit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public CommandeApprovisionnement getCommande() { return commande; }
    public void setCommande(CommandeApprovisionnement commande) { this.commande = commande; }
}
