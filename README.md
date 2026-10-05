# SAE_OTHELLO_PARENT : plateau hexagonal et IA Minimax

> SAÉ 2.1 & 2.2 · BUT Informatique · Université de Caen Normandie · 2025-2026
> Projet **Maven** multi-modules en **Java 26**.

Moteur de jeu pour un jeu de plateau à somme nulle sur **terrain hexagonal** (règles de type YINSH, appelé « Othello » dans le sujet), ainsi qu'une **IA** qui choisit le meilleur coup grâce à l'algorithme **Minimax avec élagage Alpha-Beta**.

Ce dépôt correspond au **premier livrable** : modèle du jeu, système de coordonnées hexagonales, jeu en ligne de commande et IA. L'interface graphique JavaFX est le deuxième livrable (voir [Feuille de route](#feuille-de-route)).

---

## Règles du jeu

- Le terrain est un hexagone. Chaque joueur place **5 anneaux** de sa couleur au début de la partie.
- À son tour, un joueur **déplace un de ses anneaux** en ligne droite selon l'un des 6 axes (NO, NE, E, SE, SO, O) :
  - un anneau ne peut **pas** passer par-dessus un autre anneau ;
  - s'il survole des **pions**, il doit s'arrêter à la première case libre située juste après eux ;
  - les pions survolés **changent de couleur**, et un pion de la couleur de l'anneau est déposé sur sa case de départ.
- Quand 5 pions de la même couleur sont alignés, le joueur concerné **retire la ligne et l'un de ses anneaux**. Plusieurs lignes peuvent être retirées l'une après l'autre.
- Le premier joueur qui retire **3 anneaux** gagne.
- Si plus aucun anneau du joueur courant ne peut bouger, la partie est **nulle**.

---

## Structure du dépôt

```
Jeu_JAVA_OOP/
├── pom.xml                         # POM parent (versions de Java et de JUnit)
├── README.md
├── .gitignore
├── .github/workflows/maven.yml     # Intégration continue : mvn verify
│
├── HexagonalCoordinate/            # Module 1 : coordonnées hexagonales (réutilisable)
│   ├── pom.xml
│   └── src/
│       ├── main/java/fr/saegroupe8/iut/
│       │   ├── Coordinate/         # Coordinate (abstraite), CoordinateCube, CoordinateDoubled,
│       │   │                       # Point (record), Direction, Mode
│       │   └── Exception/          # DifferentAxisException
│       └── test/java/              # Tests unitaires des deux systèmes de coordonnées
│
└── OthelloEngine/                  # Module 2 : modèle du jeu + IA (dépend de HexagonalCoordinate)
    ├── pom.xml
    └── src/
        ├── main/java/fr/saegroupe8/iut/
        │   ├── CUIMain.java        # Jeu en ligne de commande (affichage + placement des anneaux)
        │   ├── model/
        │   │   ├── Model.java      # Stocke l'état courant et le met à jour
        │   │   ├── Team.java       # BLACK / WHITE
        │   │   ├── actions/        # Action (abstraite), Move, RemoveLine
        │   │   ├── state/          # IState (interface), State (état immuable)
        │   │   ├── tokens/         # Token (abstraite), Pawn, Ring
        │   │   └── factory/        # IFactory, FactoryCube, FactoryDoubled (terrains vierges et de test)
        │   └── IA/
        │       ├── AI.java         # Interface : Action chooseMove(IState state)
        │       ├── MinimaxAI.java  # Minimax + Alpha-Beta
        │       ├── Node.java       # Nœud de l'arbre de recherche (état, parent, action)
        │       └── MainAI.java     # Partie en ligne de commande : joueur contre IA
        └── test/java/              # Tests : Action, Factory, IA, Model, State, Token
```

### Les deux modules

| Module | Rôle |
|---|---|
| **HexagonalCoordinate** | Représente un plateau hexagonal avec deux systèmes de coordonnées interchangeables : **cubique** `[q, r, s]` (avec `q + r + s = 0`) et **doublée** `[ligne, colonne]`. Fournit voisins, directions, déplacements et alignements. Aucune dépendance au jeu. |
| **OthelloEngine** | Règles du jeu, état immuable, actions, fabriques de terrains, IA et jeu en console. Dépend de `HexagonalCoordinate`. |

---

## Architecture du modèle

- **`State`** est un état de jeu **immuable** : une `HashMap<Coordinate, Token>` (valeur `null` si la case est vide), le joueur courant et les lignes présentes. Chaque coup renvoie un **nouvel** état.
- **`IState`** expose les opérations utilisées par l'IA et l'interface : `move`, `removeLine`, `availableMoves`, `removeToken`, `toggleToken`, `winner`, `isInField`, `lines`, `rings`, `board`, `turn`.
- **`Action`** est une classe abstraite sans attribut ni méthode. Elle sert de type commun à `Move` (départ → arrivée) et `RemoveLine` (ensemble de 5 cases + anneau retiré).
- **`Model`** garde l'état courant et délègue à `IState`. Ses méthodes modifient l'état stocké et ne renvoient rien.
- **`IFactory`** fournit les terrains : `emptyState`, `testState`, `stateForWhiteLineTest`, `stateForBlackLineTest` et `doubleLineStateTest`, en version cubique (`FactoryCube`) et doublée (`FactoryDoubled`).

### L'IA

`MinimaxAI` implémente `AI` et explore l'arbre des situations jusqu'à une profondeur donnée, avec élagage Alpha-Beta. Les feuilles sont évaluées par une somme pondérée : victoire ou défaite (±100 000), anneaux retirés d'avance (±1 000 chacun), mobilité des anneaux (±0,5 par case accessible d'écart) et pions d'avance (±2 chacun). Chaque `Node` mémorise son état, son parent et l'action qui y mène, ce qui permet de remonter au coup à jouer.

---

## Prérequis

- **JDK 26** (le code utilise la syntaxe de Java 25+, par exemple des instructions avant `super()` dans `CoordinateCube`)
- **Maven 3.9+**

## Compiler et tester

À la racine du dépôt :

```bash
mvn clean verify
```

Cette commande compile les deux modules et lance tous les tests JUnit 5.

## Lancer le jeu

Après `mvn package`, depuis la racine du dépôt :

**Jouer contre l'IA** (vous avez les blancs, l'IA les noirs, profondeur 3) :

```bash
# Linux / macOS
java -cp OthelloEngine/target/classes:HexagonalCoordinate/target/classes fr.saegroupe8.iut.IA.MainAI

# Windows
java -cp "OthelloEngine\target\classes;HexagonalCoordinate\target\classes" fr.saegroupe8.iut.IA.MainAI
```

Les coordonnées se saisissent en cubique, trois entiers séparés par des espaces : `q r s` (par exemple `1 -1 0`). Si une ligne est présente, le jeu vous demande l'anneau à retirer.

**Placer ses anneaux et afficher le terrain** :

```bash
java -cp OthelloEngine/target/classes:HexagonalCoordinate/target/classes fr.saegroupe8.iut.CUIMain
```

Déplacez le curseur avec `Z` `Q` `S` `D`, puis `X` pose un anneau (les joueurs alternent). Légende de l'affichage :

| Symbole | Signification |
|:-:|---|
| `o` / `O` | anneau blanc / noir |
| `.` / `x` | pion blanc / noir |
| `_` | case vide |
| `*` | curseur |

---

## Ma part

| Partie | Auteur |
|---|---|
| `removeLine`, `isInField`, `getPawnsLines`, classe `Action`, interface `AI`, classe `MainAI` | _(AHMADI Elyas)_ |


## Feuille de route

- [x] Module `HexagonalCoordinate` (cubique et doublée)
- [x] Modèle : `State`, `Model`, actions, jetons, fabriques
- [x] Jeu en ligne de commande (`CUIMain`, `MainAI`)
- [x] IA Minimax avec élagage Alpha-Beta
- [ ] Livrable 2 : interface graphique JavaFX (modes édition / jeu / retrait de ligne, réglage de l'IA, sauvegarde et chargement au format `SAE212`)

## Organisation Git

- `master` : uniquement les fonctionnalités terminées
- une branche par fonctionnalité (par exemple `feature/remove-line`, `feature/ai`), fusionnée dans `master` une fois testée
- commits réguliers de chaque membre du groupe
