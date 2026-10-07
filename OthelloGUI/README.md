# OthelloGUI : l'interface graphique JavaFX

SAÉ 2.1 et 2.2, livrable 2, BUT Informatique, Université de Caen Normandie, 2025-2026. Application JavaFX 21, projet Maven sur Java 21.

Une application de bureau pour jouer, éditer et analyser des parties du jeu de plateau hexagonal (règles de type YINSH, appelé « Othello » dans le sujet). Les règles sont décrites dans le [README du dépôt](../README.md#les-règles).

Elle sert à cinq choses : jouer une partie à la souris, construire une position à la main, retirer les lignes de cinq pions, voir ce que « pense » l'IA d'une position, et sauvegarder ou recharger une partie.

> **À noter.** Ce dossier est un projet Maven autonome. Il n'est pas déclaré dans le `pom.xml` racine, donc la CI du dépôt ne le compile pas (voir [Limites connues](#limites-connues)).

**Sommaire** : [Ce que fait l'application](#ce-que-fait-lapplication) · [Lancer](#lancer-lapplication) · [Architecture](#architecture) · [L'IA](#lia-dans-linterface) · [Format `.yns`](#format-de-sauvegarde-yns) · [Limites connues](#limites-connues)

---

## Ce que fait l'application

L'écran se découpe en cinq zones : la barre de menu en haut, le plateau au centre, les réglages de l'IA à gauche, les commandes de partie à droite et une barre d'état en bas.

**Jouer.** Un clic sélectionne un anneau du joueur dont c'est le tour et affiche ses cases d'arrivée. Un second clic sur une case accessible déplace l'anneau, un clic ailleurs annule la sélection.

**Retirer une ligne.** Dès qu'une ligne de cinq pions existe, l'application bascule d'elle-même dans ce mode. Survoler une case de la ligne la colore en orange, un clic la verrouille en jaune, puis un clic sur un anneau de la bonne équipe retire la ligne et cet anneau.

**Éditer.** Une case à cocher active le mode édition. Un clic gauche pose ou retire un anneau ou un pion de l'équipe choisie (le même jeton sur la même case est retiré), un clic droit vide la case.

**Partir d'une position prédéfinie.** Quatre boutons : *Partie aléatoire* (5 anneaux par équipe placés au hasard), *État test*, *Ligne noire* et *Ligne blanche* (deux positions où une ligne attend d'être retirée).

**Régler l'affichage.** Les trois couleurs de cases (elles suivent un motif à trois teintes) sont modifiables, et on peut afficher les coordonnées cubiques sur chaque case. Le panneau propose aussi un curseur d'épaisseur des bordures (1 à 5) et un choix de notation *Cubique / 2D*, mais ces deux commandes ne sont pas encore reliées au plateau : voir [Limites connues](#limites-connues).

**Interroger l'IA.** Une barre d'évaluation, cinq poids modifiables à chaud et un bouton *Meilleur coup* (détails [plus bas](#lia-dans-linterface)).

**Sauvegarder.** Le menu *Partie → Sauvegarder… / Charger…* lit et écrit des fichiers binaires `.yns` ([format](#format-de-sauvegarde-yns)).

**Finir une partie.** Une fenêtre annonce le gagnant dès qu'une équipe a retiré trois anneaux. La barre d'état indique l'équipe qui doit jouer, le mode courant, et le nombre d'anneaux et de pions de chaque équipe.

On ne choisit jamais le mode d'interaction à la main, sauf pour l'édition. Il se déduit de l'état : édition si la case est cochée, sinon retrait de ligne s'il existe une ligne, sinon jeu.

---

## Lancer l'application

Il faut un JDK 21 et Maven, qui télécharge JavaFX 21 tout seul. Depuis ce dossier :

```bash
mvn clean install
cd application
mvn javafx:run
```

La première commande compile les deux modules. La seconde lance `but.info.sae2_12.App`, configurée dans le `pom.xml`. La fenêtre s'ouvre maximisée, avec une partie aléatoire déjà posée.

---

## Architecture

```
OthelloGUI/
├── pom.xml                         POM parent (Java 21, JavaFX 21, javafx-maven-plugin)
├── README.md
├── hexagonalCoordinate/            Module 1 : coordonnées hexagonales
│   └── src/main/java/coordinates/  Coordinate, CoordinateCube, CoordinateDoubled,
│                                   Direction, Mode, Point, DifferentAxisException
└── application/                    Module 2 : l'application JavaFX
    └── src/main/
        ├── java/but/info/sae2_12/
        │   ├── App.java            Point d'entrée : charge Main.fxml
        │   ├── controller/         Contrôleurs FXML, modes d'interaction, HexSquare
        │   ├── model/              Modèle observable, état immuable, jetons, actions, fabriques
        │   ├── AI/                 MiniMax (évaluation, alpha-bêta) et MinimaxAI
        │   └── persistence/        GameStorage : lecture et écriture des fichiers .yns
        └── resources/but/info/sae2_12/
                                    Main, Menu, Central, AI, GameController, Bottom (.fxml)
```

### Les cinq vues

`Main.fxml` assemble cinq vues avec `fx:include`. Chacune a son contrôleur, et le `MainController` fait le lien entre elles.

- **En haut**, `Menu.fxml` et `MenuController` : charger, sauvegarder, « À propos ».
- **Au centre**, `Central.fxml` et `CentralController` : construit le plateau (un `HexSquare` par case) et redessine les jetons à chaque changement d'état.
- **À gauche**, `AI.fxml` et `AIController` : poids de l'IA, barre d'évaluation, meilleur coup.
- **À droite**, `GameController.fxml` et `GameController` : parties prédéfinies, mode édition, couleurs, coordonnées, bordures.
- **En bas**, `Bottom.fxml` et `BottomController` : tour, mode, décompte des pièces.

### Quelques choix de conception

**Observateur.** Le `Model` expose l'état courant dans une `SimpleObjectProperty<IState>`. Les contrôleurs s'y abonnent par des listeners et des `Bindings`, donc aucun rafraîchissement manuel : on change l'état, et le plateau, la barre d'état et la barre d'évaluation suivent.

**État immuable.** `State` est un `record`, chaque coup renvoie un nouvel état. L'IA peut ainsi tester des coups sans jamais abîmer la partie affichée.

**Modes d'interaction.** `InteractionMode` est une classe abstraite à trois méthodes (`handleClick`, `entered`, `exited`), implémentée par `ClassicMode`, `RemoveLineMode` et `Edition`. Chaque `HexSquare` (un `Polygon`) transmet simplement ses événements souris au mode courant, que le `MainController` recalcule par un binding. Ajouter un mode ne demande de toucher à aucune case.

**Deux systèmes de coordonnées.** Le modèle sait construire un plateau en cubique `[q, r, s]` (`FactoryCube`) ou en doublé (`FactoryDoubled`), et la sauvegarde gère les deux. En revanche l'interface ne génère aujourd'hui que des parties cubiques : une partie doublée n'arrive que par le chargement d'un fichier.

---

## L'IA dans l'interface

L'IA reprend l'algorithme Minimax avec élagage alpha-bêta (`AI/MiniMax.java`, `AI/MinimaxAI.java`) et l'interface en fait deux usages.

**La barre d'évaluation.** À chaque changement d'état, la position est évaluée du point de vue des noirs, puis ramenée entre 0 et 1 sur la barre (noir à gauche). L'évaluation additionne des termes pondérés, chacun lié à un champ de saisie :

| Terme | Champ dans l'interface | Valeur par défaut |
|---|---|---:|
| Fin de partie | Victoire | ±100 000 |
| Anneau retiré | Nombre d'anneau | ±1 000 chacun |
| Ligne de 4 pions | Ligne de 4 | ±50 chacune |
| Pion sur le plateau | Pion | ±10 chacun |
| Mobilité des anneaux | Mobilité | ±0,5 par case |

Les liens sont des bindings bidirectionnels : modifier une valeur met la barre à jour immédiatement.

**Le meilleur coup.** Le bouton lance `MinimaxAI` à profondeur 2 pour l'équipe qui doit jouer, et affiche le coup recommandé dans une fenêtre. Si une ligne attend d'être retirée, l'IA choisit à la place l'anneau à retirer.

---

## Format de sauvegarde `.yns`

Un fichier binaire écrit avec `DataOutputStream` (octets de poids fort en premier). Les caractères occupent 2 octets (`writeChar`). Dans l'ordre :

1. la signature `SAE212` en ASCII, sur 6 octets : un fichier qui ne la porte pas est refusé ;
2. l'équipe à jouer, `B` (noir) ou `W` (blanc) ;
3. la phase, `M` (déplacement) ou `L` (ligne en attente) : écrite à la sauvegarde, ignorée au chargement ;
4. le système de coordonnées, `C` (cubique) ou `D` (doublé) ;
5. le nombre de jetons, un entier sur 4 octets ;
6. les jetons, à la suite : la position (3 entiers `q r s` en cubique, 2 entiers en doublé), le type `R` (anneau) ou `P` (pion), puis l'équipe `B` ou `W`.

À la lecture, le plateau vide du système de coordonnées indiqué est rempli avec les jetons, et les lignes en attente sont recalculées à partir des pions.

---

## Limites connues

Voici ce qui ne tient pas encore, du plus gênant au moins gênant.

1. **Deux commandes ne sont pas branchées.**
   - Le curseur d'*épaisseur des bordures* met à jour `GameController.borderThicknessProperty()`, mais rien ne la relie à `MainController.getStrokeWidth()`, que lisent les cases. Il n'a donc aucun effet visible.
   - Le choix de notation *Cubique / 2D* : `GameController.getSelectedCoordMode()` n'est jamais lu, et `MainController.getCoordinateDisplayMode()` reste sur `Cubique`. Le code qui met à jour les étiquettes (`CentralController.updateLabels`) existe, mais rien ne le déclenche.
   - La correction prévue est simple : lier ces deux propriétés dans `MainController.initialize()`, une ligne chacune.
2. **Le modèle est dupliqué.** Ce projet embarque sa propre copie du modèle, de l'IA et des coordonnées (paquets `but.info.sae2_12` et `coordinates`), alors que le dépôt contient déjà `OthelloEngine` et `HexagonalCoordinate` (paquets `fr.saegroupe8.iut`). La raison est technique : le modèle de l'interface expose des propriétés JavaFX (`currentStateProperty()`, poids de l'IA liés aux champs de saisie) que le moteur, volontairement en Java pur, n'a pas. Pour fusionner les deux, il faudrait isoler ces propriétés dans un adaptateur côté interface.
3. **Hors CI.** Le projet n'est pas un module du `pom.xml` racine, et il vise Java 21 alors que le reste du dépôt vise Java 26. La CI ne le compile donc pas.
4. **Aucun test automatisé** dans ce dossier.
5. **Pas de partie contre l'IA.** L'interface évalue les positions et suggère un coup, mais ne joue pas à la place d'un joueur. Pour affronter l'IA, il faut passer par `MainAI`, en console, dans `OthelloEngine`.
6. **Les poids de l'IA sont globaux.** Ce sont des propriétés statiques de `MiniMax` : les modifier change toutes les évaluations, y compris celle du bouton *Meilleur coup*.
7. **Le chargement fait confiance au fichier.** Seule la signature `.yns` est vérifiée. Les valeurs lues ne sont pas contrôlées : tout caractère d'équipe autre que `B` est lu comme blanc, et le nombre de jetons annoncé n'est pas comparé au contenu.
