# OthelloGUI : interface graphique JavaFX

SAÉ 2.1 et 2.2, livrable 2 · BUT Informatique · Université de Caen Normandie · 2025-2026 · JavaFX 21, Maven, Java 21

Application de bureau pour jouer, éditer et analyser des parties du jeu de plateau hexagonal décrit dans le [README du dépôt](../README.md#règles).

> Projet Maven autonome : il n'est pas déclaré dans le `pom.xml` racine, donc la CI du dépôt ne le compile pas (voir [Limites connues](#limites-connues)).

## Fonctionnalités

| Domaine | Description |
|---|---|
| Jeu | Clic sur un anneau du joueur courant pour afficher ses arrivées possibles, second clic pour le déplacer. |
| Retrait de ligne | Mode activé automatiquement dès qu'une ligne de 5 pions existe. Survol orange, clic jaune, puis clic sur un anneau de l'équipe pour retirer la ligne et l'anneau. |
| Édition | Case à cocher. Clic gauche : pose ou retire un jeton de l'équipe choisie. Clic droit : vide la case. |
| Positions prédéfinies | Partie aléatoire, état test, ligne noire, ligne blanche. |
| Affichage | Couleurs des cases modifiables, coordonnées cubiques affichables. |
| IA | Barre d'évaluation de la position, 5 poids modifiables, bouton « Meilleur coup ». |
| Sauvegarde | Menu Partie → Sauvegarder / Charger, format binaire `.yns`. |
| Fin de partie | Fenêtre de victoire quand une équipe a retiré 3 anneaux. |

Le mode d'interaction se déduit de l'état : édition si la case est cochée, sinon retrait de ligne s'il existe une ligne, sinon jeu.

## Lancer

Prérequis : JDK 21, Maven.

```bash
mvn clean install
cd application
mvn javafx:run
```

## Architecture

```
OthelloGUI/
├── pom.xml                         POM parent (Java 21, JavaFX 21)
├── hexagonalCoordinate/            Coordonnées hexagonales (paquet coordinates)
└── application/                    Application JavaFX
    └── src/main/
        ├── java/but/info/sae2_12/
        │   ├── App.java            Point d'entrée, charge Main.fxml
        │   ├── controller/         Contrôleurs FXML, modes d'interaction, HexSquare
        │   ├── model/              Modèle observable, état immuable, jetons, actions, fabriques
        │   ├── AI/                 MiniMax, MinimaxAI
        │   └── persistence/        GameStorage (fichiers .yns)
        └── resources/…             Fichiers FXML
```

`Main.fxml` assemble cinq vues (menu, plateau, IA, commandes de partie, barre d'état), chacune avec son contrôleur ; `MainController` les relie.

**Choix de conception**
- **Observateur** : le `Model` expose l'état dans une `SimpleObjectProperty<IState>`, les contrôleurs s'y abonnent (listeners, bindings).
- **État immuable** : `State` est un `record`, chaque coup renvoie un nouvel état. L'IA peut explorer sans modifier la partie affichée.
- **Modes d'interaction** : `InteractionMode` (`handleClick`, `entered`, `exited`) est implémentée par `ClassicMode`, `RemoveLineMode` et `Edition`. Chaque case transmet ses événements au mode courant.
- **Coordonnées** : cubiques et doublées gérées par le modèle et la sauvegarde ; l'interface ne génère que des parties cubiques.

## IA

Minimax avec élagage alpha-bêta (`AI/MiniMax.java`, `AI/MinimaxAI.java`).

- **Barre d'évaluation** : position évaluée du point de vue des noirs, ramenée entre 0 et 1. Poids par défaut : victoire ±100 000, anneau retiré ±1 000, ligne de 4 ±50, pion ±10, mobilité ±0,5 par case. Les champs de saisie sont liés aux poids par des bindings bidirectionnels.
- **Meilleur coup** : `MinimaxAI` à profondeur 2 pour l'équipe qui doit jouer. Si une ligne est en attente, l'IA choisit l'anneau à retirer.

## Format `.yns`

Binaire, `DataOutputStream`, caractères sur 2 octets. Ordre des champs :

| Champ | Contenu |
|---|---|
| Signature | `SAE212` (ASCII, 6 octets) ; refus du fichier sinon |
| Équipe à jouer | `B` ou `W` |
| Phase | `M` ou `L` (écrite, ignorée au chargement) |
| Coordonnées | `C` (cubique) ou `D` (doublé) |
| Nombre de jetons | entier, 4 octets |
| Jetons (répété) | position (3 entiers en cubique, 2 en doublé), type `R`/`P`, équipe `B`/`W` |

Au chargement, le plateau vide est rempli avec les jetons et les lignes en attente sont recalculées.

## Limites connues

1. **Deux commandes non branchées.** Le curseur d'épaisseur des bordures (`GameController.borderThicknessProperty()` n'est pas liée à `MainController.getStrokeWidth()`) et le choix de notation Cubique/2D (`getSelectedCoordMode()` n'est jamais lu) n'ont aucun effet. Correction prévue : les lier dans `MainController.initialize()`.
2. **Modèle dupliqué.** Le projet embarque sa propre copie du modèle, de l'IA et des coordonnées (`but.info.sae2_12`, `coordinates`), car le modèle de l'interface expose des propriétés JavaFX que le moteur, en Java pur, n'a pas. Les fusionner demande un adaptateur côté interface.
3. **Hors CI**, et Java 21 ici contre Java 26 pour le reste du dépôt.
4. **Aucun test automatisé** dans ce dossier.
5. **Pas de partie contre l'IA** dans l'interface (elle évalue et suggère). Pour jouer contre l'IA : `MainAI` dans `OthelloEngine`.
6. **Poids de l'IA globaux** (propriétés statiques de `MiniMax`) : les modifier change toutes les évaluations.
7. **Chargement peu défensif.** Seule la signature `.yns` est vérifiée ; tout caractère d'équipe autre que `B` est lu comme blanc, et le nombre de jetons annoncé n'est pas comparé au contenu.

## Usage de l'IA

Des outils d'IA générative ont servi d'assistant : compréhension d'erreurs, débogage, relecture et mise en forme de la documentation. La conception et la logique du projet ont été réalisées par l'équipe.
