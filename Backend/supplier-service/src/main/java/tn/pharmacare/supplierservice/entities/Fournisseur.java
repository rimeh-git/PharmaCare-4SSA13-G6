package tn.pharmacare.supplierservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Fournisseur (laboratoire, grossiste...) qui approvisionne la pharmacie.
 * Association : 1 Fournisseur  --->  N CommandeApprovisionnement
 */
@Entity
@Table(name = "fournisseurs")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Email(message = "Email invalide")
    private String email;

    private String telephone;
    private String adresse;
    private String ville;

    @Column(unique = true)
    private String matriculeFiscal;

    private boolean actif = true;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // évite la boucle Fournisseur -> Commande -> Fournisseur en JSON
    private List<CommandeApprovisionnement> commandes = new ArrayList<>();

    public Fournisseur() {}

    public Fournisseur(String nom, String email, String telephone, String adresse,
                       String ville, String matriculeFiscal) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.ville = ville;
        this.matriculeFiscal = matriculeFiscal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getMatriculeFiscal() { return matriculeFiscal; }
    public void setMatriculeFiscal(String matriculeFiscal) { this.matriculeFiscal = matriculeFiscal; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public List<CommandeApprovisionnement> getCommandes() { return commandes; }
    public void setCommandes(List<CommandeApprovisionnement> commandes) { this.commandes = commandes; }
}
