# OthelloGUI : interface graphique JavaFX du jeu de plateau hexagonal

> SAÉ 2.1 & 2.2 · livrable 2 · BUT Informatique · Université de Caen Normandie · 2025-2026
> Application **JavaFX 21** · projet **Maven** sur **Java 21**

Application de bureau pour jouer, éditer et analyser des parties d'un jeu de plateau à somme nulle sur terrain hexagonal (règles de type YINSH, appelé « Othello » dans le sujet). Les règles complètes sont décrites dans le [README du dépôt](../README.md#règles-du-jeu).

L'interface couvre cinq besoins : jouer une partie à la souris, construire une position à la main, retirer les lignes de cinq pions, mesurer ce que « pense » l'IA d'une position, et sauvegarder ou recharger une partie.

> **Statut.** Ce dossier est un projet Maven autonome. Il n'est pas déclaré dans le `pom.xml` racine et n'est donc pas couvert par la CI du dépôt (voir [Limites connues](#limites-connues)).

---

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Lancer l'application](#lancer-lapplication)
- [Architecture](#architecture)
- [L'IA dans l'interface](#lia-dans-linterface)
- [Format de sauvegarde `.yns`](#format-de-sauvegarde-yns)
- [Limites connues](#limites-connues)

---

## Fonctionnalités

L'écran est découpé en cinq zones : barre de menu en haut, plateau au centre, réglages de l'IA à gauche, commandes de partie à droite, barre d'état en bas.

| Domaine | Ce que fait l'application |
|---|---|
| **Jeu** | Un clic sélectionne un anneau du joueur dont c'est le tour et affiche ses cases d'arrivée. Un second clic sur une case accessible déplace l'anneau. Un clic ailleurs annule la sélection. |
| **Retrait de ligne** | Dès qu'une ligne de cinq pions existe, le mode bascule automatiquement. Survoler une case de la ligne la colore en orange, un clic la verrouille en jaune, puis un clic sur un anneau de la bonne équipe retire la ligne et cet anneau. |
| **Édition** | Case à cocher « Activer le mode édition ». Clic gauche : pose ou retire un anneau ou un pion de l'équipe choisie (le même jeton sur la même case est retiré). Clic droit : vide la case. |
| **Parties prédéfinies** | Quatre boutons : *Partie aléatoire* (5 anneaux par équipe placés au hasard), *État test* (position d'essai), *Ligne noire* et *Ligne blanche* (positions où une ligne est à retirer). |
| **Affichage** | Trois couleurs de cases modifiables (les cases suivent un motif à trois teintes) et affichage des coordonnées cubiques sur chaque case, par case à cocher. Le panneau propose aussi un curseur d'épaisseur des bordures (1 à 5) et un choix de notation *Cubique / 2D*, mais ces deux commandes ne sont pas encore reliées au plateau (voir [Limites connues](#limites-connues)). |
| **IA** | Barre d'évaluation de la position, cinq poids modifiables à chaud, bouton *Meilleur coup* (voir [L'IA dans l'interface](#lia-dans-linterface)). |
| **Sauvegarde** | Menu *Partie → Sauvegarder… / Charger…* au format binaire `.yns` (voir [Format de sauvegarde](#format-de-sauvegarde-yns)). |
| **Fin de partie** | Une fenêtre annonce le gagnant dès qu'une équipe a retiré trois anneaux. |
| **Barre d'état** | Équipe qui doit jouer, mode d'interaction courant, nombre d'anneaux et de pions de chaque équipe. |

Le mode d'interaction n'est jamais choisi à la main, sauf pour l'édition. Il est déduit de l'état : édition si la case est cochée, sinon retrait de ligne si une ligne existe, sinon jeu.

---

## Lancer l'application

### Prérequis

- **JDK 21**
- **Maven** (télécharge JavaFX 21 automatiquement)

### Commandes

Depuis ce dossier (`OthelloGUI/`) :

```bash
mvn clean install
cd application
mvn javafx:run
```

La première commande compile les deux modules. La seconde lance `but.info.sae2_12.App`, configurée dans le `pom.xml` de ce dossier. La fenêtre s'ouvre maximisée, avec une partie aléatoire déjà générée.

---

## Architecture

```
OthelloGUI/
├── pom.xml                         # POM parent (Java 21, JavaFX 21, plugin javafx-maven-plugin)
├── README.md
├── hexagonalCoordinate/            # Module 1 : coordonnées hexagonales
│   └── src/main/java/coordinates/  # Coordinate, CoordinateCube, CoordinateDoubled,
│                                   # Direction, Mode, Point, DifferentAxisException
└── application/                    # Module 2 : l'application JavaFX
    └── src/main/
        ├── java/but/info/sae2_12/
        │   ├── App.java            # Point d'entrée : charge Main.fxml
        │   ├── controller/         # Contrôleurs FXML + modes d'interaction + HexSquare
        │   ├── model/              # Modèle observable, état immuable, jetons, actions, fabriques
        │   ├── AI/                 # MiniMax (évaluation, alpha-bêta) et MinimaxAI
        │   └── persistence/        # GameStorage : lecture et écriture des fichiers .yns
        └── resources/but/info/sae2_12/
                                    # Main, Menu, Central, AI, GameController, Bottom (.fxml)
```

### Découpage de l'interface

`Main.fxml` assemble cinq vues par `fx:include`. Chacune a son contrôleur, et le `MainController` les relie.

| Zone | Vue FXML | Contrôleur | Rôle |
|---|---|---|---|
| Haut | `Menu.fxml` | `MenuController` | Charger, sauvegarder, « À propos ». |
| Centre | `Central.fxml` | `CentralController` | Construit le plateau (un `HexSquare` par case) et redessine les jetons à chaque changement d'état. |
| Gauche | `AI.fxml` | `AIController` | Poids de l'IA, barre d'évaluation, meilleur coup. |
| Droite | `GameController.fxml` | `GameController` | Parties prédéfinies, mode édition, couleurs, coordonnées, bordures. |
| Bas | `Bottom.fxml` | `BottomController` | Tour, mode, décompte des pièces. |

### Choix de conception

- **Observateur.** Le `Model` expose l'état courant dans une `SimpleObjectProperty<IState>`. Les contrôleurs s'y abonnent par listeners et par `Bindings`, ce qui évite tout rafraîchissement manuel : on change l'état, le plateau, la barre d'état et la barre d'évaluation se mettent à jour.
- **État immuable.** `State` est un `record` : chaque coup renvoie un nouvel état. C'est ce qui permet à l'IA d'explorer des coups sans jamais abîmer la partie affichée.
- **Modes d'interaction.** `InteractionMode` est une classe abstraite à trois méthodes (`handleClick`, `entered`, `exited`), implémentée par `ClassicMode`, `RemoveLineMode` et `Edition`. Chaque `HexSquare` (un `Polygon`) transmet simplement ses événements souris au mode courant, que le `MainController` recalcule par un binding. Ajouter un mode ne demande aucune modification des cases.
- **Deux systèmes de coordonnées.** Le modèle sait construire un plateau en coordonnées cubiques `[q, r, s]` (`FactoryCube`) ou doublées (`FactoryDoubled`), et la sauvegarde gère les deux. L'interface ne génère aujourd'hui que des parties cubiques ; une partie doublée n'arrive que par le chargement d'un fichier.

---

## L'IA dans l'interface

L'IA réutilise l'algorithme **Minimax avec élagage alpha-bêta** (`AI/MiniMax.java`, `AI/MinimaxAI.java`). L'interface en tire deux usages.

**Barre d'évaluation.** À chaque changement d'état, la position est évaluée du point de vue des noirs, puis ramenée entre 0 et 1 sur la barre (noir à gauche). La fonction d'évaluation additionne des termes pondérés :

| Terme | Champ dans l'interface | Valeur par défaut |
|---|---|---|
| Fin de partie (victoire ou défaite) | Victoire | ±100 000 |
| Anneau retiré | Nombre d'anneau | ±1 000 chacun |
| Ligne de 4 pions | Ligne de 4 | ±50 chacune |
| Pion sur le plateau | Pion | ±10 chacun |
| Mobilité des anneaux | Mobilité | ±0,5 par case |

Les cinq champs sont liés aux poids par des bindings bidirectionnels : modifier une valeur met la barre à jour immédiatement.

**Meilleur coup.** Le bouton lance `MinimaxAI` à profondeur **2** pour l'équipe qui doit jouer et affiche le coup recommandé dans une fenêtre. Si une ligne est en attente, l'IA choisit à la place l'anneau à retirer.

---

## Format de sauvegarde `.yns`

Fichier binaire, écrit avec `DataOutputStream` (octets de poids fort en premier). Les caractères sont écrits sur 2 octets (`writeChar`).

| Champ | Taille | Contenu |
|---|---|---|
| Signature | 6 octets | `SAE212` en ASCII. Un fichier sans cette signature est refusé. |
| Équipe à jouer | 2 octets | `B` (noir) ou `W` (blanc). |
| Phase | 2 octets | `M` (déplacement) ou `L` (ligne en attente). Écrite à la sauvegarde, ignorée au chargement. |
| Système de coordonnées | 2 octets | `C` (cubique) ou `D` (doublé). |
| Nombre de jetons | 4 octets | Entier. |
| Jetons (répété) | variable | Position : 3 entiers `q r s` en cubique, 2 entiers en doublé. Puis le type `R` (anneau) ou `P` (pion), puis l'équipe `B` ou `W`. |

À la lecture, le plateau vide du système de coordonnées indiqué est rempli avec les jetons, et les lignes en attente sont recalculées à partir des pions.

---

## Limites connues

Ces points sont connus et assumés. Ils sont classés par impact.

1. **Deux commandes du panneau ne sont pas branchées.**
   - *Épaisseur des bordures* : le curseur met à jour `GameController.borderThicknessProperty()`, mais rien ne la relie à `MainController.getStrokeWidth()`, que lisent les cases. Le curseur n'a donc aucun effet visible.
   - *Notation Cubique / 2D* : `GameController.getSelectedCoordMode()` n'est jamais lu et `MainController.getCoordinateDisplayMode()` reste sur `Cubique`. Le code de mise à jour des étiquettes (`CentralController.updateLabels`) existe, mais il n'est jamais déclenché.
   - Correction prévue : lier ces deux propriétés dans `MainController.initialize()` (une ligne chacune).
2. **Modèle dupliqué.** Ce projet embarque sa propre copie du modèle, de l'IA et des coordonnées (paquets `but.info.sae2_12` et `coordinates`), alors que le dépôt contient déjà les modules `OthelloEngine` et `HexagonalCoordinate` (paquets `fr.saegroupe8.iut`). La cause est technique : le modèle de l'interface expose des propriétés JavaFX (`currentStateProperty()`, poids de l'IA liés aux champs de saisie) que le moteur, volontairement en Java pur, n'a pas. Fusionner les deux demande d'isoler ces propriétés dans un adaptateur côté interface.
3. **Hors CI.** Le projet n'est pas un module du `pom.xml` racine, et il cible Java 21 alors que le reste du dépôt cible Java 26. La CI du dépôt ne le compile donc pas.
4. **Aucun test automatisé** dans ce dossier.
5. **Pas de partie contre l'IA.** L'interface évalue les positions et suggère un coup, mais ne joue pas à la place d'un joueur. Pour affronter l'IA, utiliser `MainAI` du module `OthelloEngine` (en ligne de commande).
6. **Poids de l'IA globaux.** Les poids sont des propriétés statiques de `MiniMax` : les modifier change toutes les évaluations, y compris celle du bouton *Meilleur coup*.
7. **Chargement peu défensif.** Seule la signature du fichier `.yns` est vérifiée. Les valeurs lues ne sont pas contrôlées : tout caractère d'équipe autre que `B` est lu comme blanc, et le nombre de jetons annoncé n'est pas comparé au contenu.
