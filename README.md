# SAE Othello : plateau hexagonal et IA Minimax

SAÉ 2.1 et 2.2, BUT Informatique, Université de Caen Normandie, 2025-2026. Projet Maven multi-modules en Java 26.

Ce dépôt contient le moteur d'un jeu de plateau à somme nulle sur terrain hexagonal (les règles ressemblent à celles du YINSH, même si le sujet l'appelle « Othello »), et une IA qui choisit son coup avec l'algorithme Minimax et l'élagage alpha-bêta.

Les deux modules Maven forment le premier livrable : le modèle du jeu, le système de coordonnées hexagonales, une version en ligne de commande et l'IA. L'interface graphique JavaFX, deuxième livrable, est un projet à part dans [`OthelloGUI/`](OthelloGUI/README.md).

---

## Les règles

Le terrain est un hexagone. Au début de la partie, chaque joueur place 5 anneaux de sa couleur.

À son tour, un joueur déplace un de ses anneaux en ligne droite, selon l'un des six axes (NO, NE, E, SE, SO, O). Un anneau ne peut pas sauter par-dessus un autre anneau. S'il survole des pions, il doit s'arrêter sur la première case libre juste après eux. Les pions survolés changent de couleur, et un pion de la couleur de l'anneau est laissé sur sa case de départ.

Quand cinq pions de la même couleur sont alignés, leur propriétaire retire la ligne et l'un de ses anneaux. Plusieurs lignes peuvent se retirer à la suite. Le premier qui a retiré trois anneaux gagne. Si plus aucun anneau du joueur courant ne peut bouger, la partie est nulle.

---

## Organisation du dépôt

```
sae_othello_parent/
├── pom.xml                         POM parent (versions de Java et de JUnit)
├── README.md
├── .gitignore
├── .github/workflows/maven.yml     Intégration continue : mvn verify
│
├── HexagonalCoordinate/            Module 1 : coordonnées hexagonales, réutilisable
│   ├── pom.xml
│   └── src/
│       ├── main/java/fr/saegroupe8/iut/
│       │   ├── Coordinate/         Coordinate (abstraite), CoordinateCube, CoordinateDoubled,
│       │   │                       Point (record), Direction, Mode
│       │   └── Exception/          DifferentAxisException
│       └── test/java/              Tests des deux systèmes de coordonnées
│
├── OthelloEngine/                  Module 2 : modèle du jeu et IA (dépend du module 1)
│   ├── pom.xml
│   └── src/
│       ├── main/java/fr/saegroupe8/iut/
│       │   ├── CUIMain.java        Jeu en console : affichage et placement des anneaux
│       │   ├── model/
│       │   │   ├── Model.java      Garde l'état courant et le met à jour
│       │   │   ├── Team.java       BLACK / WHITE
│       │   │   ├── actions/        Action (abstraite), Move, RemoveLine
│       │   │   ├── state/          IState (interface), State (état immuable)
│       │   │   ├── tokens/         Token (abstraite), Pawn, Ring
│       │   │   └── factory/        IFactory, FactoryCube, FactoryDoubled
│       │   └── IA/
│       │       ├── AI.java         Interface : Action chooseMove(IState state)
│       │       ├── MinimaxAI.java  Minimax avec alpha-bêta
│       │       ├── Node.java       Nœud de l'arbre de recherche (état, parent, action)
│       │       └── MainAI.java     Partie en console : joueur contre IA
│       └── test/java/              Tests : Action, Factory, IA, Model, State, Token
│
└── OthelloGUI/                     Livrable 2 : interface JavaFX, projet Maven autonome
```

**HexagonalCoordinate** ne dépend de rien. Il représente un plateau hexagonal avec deux systèmes de coordonnées interchangeables, le cubique `[q, r, s]` (avec `q + r + s = 0`) et le « doublé » `[ligne, colonne]`, et fournit voisins, directions, déplacements et alignements.

**OthelloEngine** contient les règles, l'état immuable, les actions, les fabriques de terrains, l'IA et le jeu en console. Il dépend de `HexagonalCoordinate`.

**OthelloGUI** n'est pas déclaré dans le `pom.xml` racine : il se construit et se lance depuis son propre dossier.

---

## Comment le modèle est construit

`State` est un état de jeu immuable : une `HashMap<Coordinate, Token>` (valeur `null` pour une case vide), le joueur courant et les lignes présentes. Chaque coup renvoie un nouvel état au lieu de modifier l'ancien, ce qui rend l'exploration de l'IA sans danger.

`IState` regroupe ce dont l'IA et l'interface ont besoin : `move`, `removeLine`, `availableMoves`, `removeToken`, `toggleToken`, `winner`, `isInField`, `lines`, `rings`, `board` et `turn`.

`Action` est une classe abstraite vide, qui sert de type commun à `Move` (départ et arrivée) et à `RemoveLine` (les 5 cases de la ligne et l'anneau retiré).

`Model` garde l'état courant et délègue à `IState`. Ses méthodes modifient l'état stocké et ne renvoient rien.

`IFactory` fournit les terrains (`emptyState`, `testState`, `stateForWhiteLineTest`, `stateForBlackLineTest`, `doubleLineStateTest`), en version cubique (`FactoryCube`) et doublée (`FactoryDoubled`).

### L'IA

`MinimaxAI` implémente `AI`. Elle explore l'arbre des positions jusqu'à une profondeur donnée, en élaguant avec alpha-bêta. Les feuilles sont évaluées par une somme pondérée : victoire ou défaite (±100 000), anneaux retirés d'avance (±1 000 chacun), mobilité des anneaux (±0,5 par case accessible d'écart) et pions d'avance (±2 chacun). Chaque `Node` mémorise son état, son parent et l'action qui y mène, ce qui permet de remonter jusqu'au coup à jouer.

---

## Compiler et tester

Il faut un JDK 26 (le code utilise la syntaxe de Java 25 et plus, par exemple des instructions avant `super()` dans `CoordinateCube`) et Maven. À la racine :

```bash
mvn clean verify
```

Cette commande compile les deux modules et lance les tests JUnit 5.

## Jouer

Après un `mvn package`, depuis la racine.

Pour affronter l'IA (vous avez les blancs, elle a les noirs, profondeur 3) :

```bash
# Linux / macOS
java -cp OthelloEngine/target/classes:HexagonalCoordinate/target/classes fr.saegroupe8.iut.IA.MainAI

# Windows
java -cp "OthelloEngine\target\classes;HexagonalCoordinate\target\classes" fr.saegroupe8.iut.IA.MainAI
```

Les coordonnées se saisissent en cubique, trois entiers séparés par des espaces : `q r s`, par exemple `1 -1 0`. Quand une ligne est à retirer, le jeu demande quel anneau enlever.

Pour placer ses anneaux et voir le terrain :

```bash
java -cp OthelloEngine/target/classes:HexagonalCoordinate/target/classes fr.saegroupe8.iut.CUIMain
```

On déplace le curseur avec `Z`, `Q`, `S`, `D`, et `X` pose un anneau (les joueurs alternent). À l'écran, `o` et `O` sont les anneaux blanc et noir, `.` et `x` les pions blanc et noir, `_` une case vide et `*` le curseur.

---

## Qui a fait quoi

Elyas Ahmadi : `removeLine`, `isInField`, `getPawnsLines`, la classe `Action`, l'interface `AI` et la classe `MainAI`.

## Où on en est

- [x] Module `HexagonalCoordinate` (cubique et doublé)
- [x] Modèle : `State`, `Model`, actions, jetons, fabriques
- [x] Jeu en console (`CUIMain`, `MainAI`)
- [x] IA Minimax avec élagage alpha-bêta
- [x] Livrable 2 : interface graphique JavaFX, voir [`OthelloGUI/`](OthelloGUI/README.md) (modes édition, jeu et retrait de ligne, réglage de l'IA, sauvegarde et chargement au format `SAE212`). Deux réglages d'affichage restent à brancher, ils sont détaillés dans ses limites connues.

## Organisation Git

`master` ne reçoit que des fonctionnalités terminées. Chaque fonctionnalité a sa branche (par exemple `feature/remove-line`, `feature/ai`), fusionnée dans `master` une fois testée. Chaque membre du groupe commite régulièrement.
