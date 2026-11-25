package tn.pharmacare.supplierservice.entities;

public enum StatutCommande {
    EN_ATTENTE,   // créée, pas encore envoyée / validée
    VALIDEE,      // confirmée et envoyée au fournisseur
    RECUE,        // marchandise réceptionnée
    ANNULEE
}
