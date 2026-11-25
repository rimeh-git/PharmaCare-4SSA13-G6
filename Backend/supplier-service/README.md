# Supplier Service — Gestion des fournisseurs et des approvisionnements

Microservice PharmaCare (Spring Boot 3.4 / Spring Cloud 2024) — responsable : **Yassmine Abbes**

- Port par défaut : **8085** (2ᵉ instance : **8095**)
- Base de données : **MySQL (XAMPP)** `supplier_db` (créée automatiquement) — ou **H2** en mémoire (profil `h2`)
- Client **Eureka** (8761 + 8762) — **Actuator** exposé

---

## 1. Modèle de données

```
Fournisseur 1 ──── N CommandeApprovisionnement 1 ──── N LigneCommande
                              │
                              1
                              │
                           0..1 Reception
```

| Entité | Champs | Associations |
|---|---|---|
| **Fournisseur** | id, nom, email, telephone, adresse, ville, matriculeFiscal (unique), actif | `@OneToMany` → commandes |
| **CommandeApprovisionnement** | id, reference, dateCommande, dateLivraisonPrevue, statut, montantTotal | `@ManyToOne` → fournisseur, `@OneToMany` → lignes, `@OneToOne` → reception |
| **LigneCommande** | id, productId, nomProduit, quantite, prixUnitaire | `@ManyToOne` → commande |
| **Reception** | id, dateReception, conforme, observations | `@OneToOne` → commande |

`StatutCommande` : `EN_ATTENTE` → `VALIDEE` → `RECUE` (ou `ANNULEE`)

`productId` référence un produit de **product-service** (pas de clé étrangère : chaque microservice a sa propre base).

### Règles métier
- Le matricule fiscal est unique.
- Impossible de commander chez un fournisseur inactif.
- `montantTotal` = Σ (quantite × prixUnitaire), calculé automatiquement.
- Une commande n'est modifiable que si elle est `EN_ATTENTE`.
- Seule une commande `VALIDEE` peut être réceptionnée ; la réception la passe à `RECUE`.
- Une commande `RECUE` ne peut pas être supprimée.

---

## 2. Endpoints REST

Accessibles directement (`http://localhost:8085`) ou via le Gateway (`http://localhost:8080`).

### Fournisseurs — `/api/suppliers`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/suppliers` | Liste (filtres : `?nom=medis`, `?actif=true`) |
| GET | `/api/suppliers/{id}` | Détail |
| POST | `/api/suppliers` | Créer |
| PUT | `/api/suppliers/{id}` | Modifier |
| DELETE | `/api/suppliers/{id}` | Supprimer |

### Commandes — `/api/suppliers/commandes`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/suppliers/commandes` | Liste (filtres : `?fournisseurId=1`, `?statut=VALIDEE`) |
| GET | `/api/suppliers/commandes/{id}` | Détail avec lignes |
| POST | `/api/suppliers/commandes` | Créer |
| PUT | `/api/suppliers/commandes/{id}` | Modifier (si EN_ATTENTE) |
| PATCH | `/api/suppliers/commandes/{id}/statut?valeur=VALIDEE` | Changer le statut |
| DELETE | `/api/suppliers/commandes/{id}` | Supprimer |

### Réceptions — `/api/suppliers/receptions`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/suppliers/receptions` | Liste |
| GET | `/api/suppliers/receptions/{id}` | Détail |
| POST | `/api/suppliers/receptions` | Réceptionner une commande VALIDEE |
| PUT | `/api/suppliers/receptions/{id}` | Modifier |
| DELETE | `/api/suppliers/receptions/{id}` | Annuler (la commande repasse VALIDEE) |

`GET /api/suppliers/status` → indique le port de l'instance qui répond.

### Exemples de corps JSON

**Créer un fournisseur**
```json
{
  "nom": "Adwya",
  "email": "contact@adwya.tn",
  "telephone": "71 000 444",
  "adresse": "Route de la Marsa",
  "ville": "Tunis",
  "matriculeFiscal": "MF-1004",
  "actif": true
}
```

**Créer une commande**
```json
{
  "fournisseurId": 1,
  "dateLivraisonPrevue": "2026-12-05",
  "lignes": [
    { "productId": 1, "nomProduit": "Doliprane 1000mg", "quantite": 100, "prixUnitaire": 2.1 },
    { "productId": 4, "nomProduit": "Smecta",           "quantite": 40,  "prixUnitaire": 5.0 }
  ]
}
```

**Réceptionner une commande** (la commande doit d'abord être VALIDEE)
```json
{ "commandeId": 1, "conforme": true, "observations": "Livraison complète" }
```

Des données de démonstration (3 fournisseurs, 2 commandes) sont insérées au premier démarrage.

---

## 3. Lancement

Prérequis : Java 17+, Maven, XAMPP avec MySQL démarré (port 3306, user `root` sans mot de passe).

Depuis la racine `PharmaCare-Backend` :

```bash
# 1) Eureka
mvn -pl discovery-server spring-boot:run

# 2) Instance 1 du supplier-service (port 8085)
mvn -pl supplier-service spring-boot:run

# 3) Instance 2 (port 8095) — dans un autre terminal
mvn -pl supplier-service spring-boot:run -Dspring-boot.run.arguments=--server.port=8095
```

Sans XAMPP (base H2 en mémoire) :
```bash
mvn -pl supplier-service spring-boot:run -Dspring-boot.run.profiles=h2
mvn -pl supplier-service spring-boot:run -Dspring-boot.run.profiles=h2 -Dspring-boot.run.arguments=--server.port=8095
```
Console H2 : http://localhost:8085/h2-console (JDBC URL : `jdbc:h2:mem:supplier_db`, user `sa`).

> Si un seul serveur Eureka tourne (8761), les erreurs de connexion vers 8762 dans les logs sont normales : le service s'enregistre quand même sur 8761.

Dans le dashboard Eureka (http://localhost:8761) on doit voir :
`SUPPLIER-SERVICE  UP (2) - supplier-service:8085 , supplier-service:8095`

---

## 4. Actuator (observabilité)

Tous les endpoints sont exposés (`management.endpoints.web.exposure.include=*`) :

| Endpoint | Rôle |
|---|---|
| http://localhost:8085/actuator | Liste des endpoints |
| http://localhost:8085/actuator/health | État (UP/DOWN) + DB + disque + Eureka |
| http://localhost:8085/actuator/info | Infos de l'application |
| http://localhost:8085/actuator/metrics | Liste des métriques |
| http://localhost:8085/actuator/metrics/jvm.memory.used | Mémoire JVM |
| http://localhost:8085/actuator/metrics/http.server.requests | Requêtes HTTP |
| http://localhost:8085/actuator/env | Variables de configuration |
| http://localhost:8085/actuator/beans | Beans Spring |
| http://localhost:8085/actuator/mappings | Toutes les routes REST |
| http://localhost:8085/actuator/loggers | Niveaux de logs |

Faire les captures d'écran de ces endpoints pour le rendu.
