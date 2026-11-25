package tn.pharmacare.supplierservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import tn.pharmacare.supplierservice.entities.*;
import tn.pharmacare.supplierservice.repositories.CommandeRepository;
import tn.pharmacare.supplierservice.repositories.FournisseurRepository;

import java.time.LocalDate;

@SpringBootApplication
@EnableDiscoveryClient
public class SupplierServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierServiceApplication.class, args);
    }

    /** Données de démonstration (insérées seulement si la base est vide). */
    @Bean
    CommandLineRunner initData(FournisseurRepository fournisseurs, CommandeRepository commandes) {
        return args -> {
            if (fournisseurs.count() > 0) return;

            Fournisseur f1 = fournisseurs.save(new Fournisseur("Laboratoires Medis", "contact@medis.tn",
                    "71 000 111", "Route de Tunis km 7", "Nabeul", "MF-1001"));
            Fournisseur f2 = fournisseurs.save(new Fournisseur("Saiph Pharma", "commandes@saiph.tn",
                    "71 000 222", "Zone industrielle", "Ben Arous", "MF-1002"));
            fournisseurs.save(new Fournisseur("Opalia Recordati", "info@opalia.tn",
                    "71 000 333", "Ariana Business Center", "Ariana", "MF-1003"));

            CommandeApprovisionnement c1 = new CommandeApprovisionnement();
            c1.setReference("CMD-0001");
            c1.setDateCommande(LocalDate.now().minusDays(3));
            c1.setDateLivraisonPrevue(LocalDate.now().plusDays(4));
            c1.setStatut(StatutCommande.VALIDEE);
            c1.setFournisseur(f1);
            c1.ajouterLigne(new LigneCommande(1L, "Doliprane 1000mg", 200, 2.1));
            c1.ajouterLigne(new LigneCommande(2L, "Augmentin 1g", 50, 9.8));
            c1.recalculerMontant();
            commandes.save(c1);

            CommandeApprovisionnement c2 = new CommandeApprovisionnement();
            c2.setReference("CMD-0002");
            c2.setDateCommande(LocalDate.now());
            c2.setDateLivraisonPrevue(LocalDate.now().plusDays(7));
            c2.setFournisseur(f2);
            c2.ajouterLigne(new LigneCommande(3L, "Vitamine C 500mg", 120, 4.5));
            c2.recalculerMontant();
            commandes.save(c2);
        };
    }
}
