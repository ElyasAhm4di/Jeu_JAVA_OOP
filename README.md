# SAE Othello : plateau hexagonal et IA Minimax

SAÉ 2.1 et 2.2 · BUT Informatique · Université de Caen Normandie · 2025-2026 · Java 26, Maven multi-modules

Moteur d'un jeu de plateau à somme nulle sur terrain hexagonal (règles de type YINSH, appelé « Othello » dans le sujet) et IA qui joue avec l'algorithme Minimax et l'élagage alpha-bêta.

| Livrable | Contenu | Emplacement |
|---|---|---|
| 1 | Coordonnées hexagonales, modèle du jeu, jeu en console, IA | `HexagonalCoordinate/`, `OthelloEngine/` |
| 2 | Interface graphique JavaFX | [`OthelloGUI/`](OthelloGUI/README.md) (projet Maven autonome) |

## Règles

- Terrain hexagonal. Chaque joueur place 5 anneaux de sa couleur.
- À son tour, un joueur déplace un de ses anneaux en ligne droite selon l'un des 6 axes (NO, NE, E, SE, SO, O). Un anneau ne saute pas un autre anneau.
- Un anneau qui survole des pions s'arrête sur la première case libre qui suit. Les pions survolés changent de couleur, et un pion de la couleur de l'anneau reste sur la case de départ.
- Cinq pions alignés de même couleur : leur propriétaire retire la ligne et un de ses anneaux. Plusieurs lignes peuvent se retirer à la suite.
- Le premier joueur qui a retiré 3 anneaux gagne. Si le joueur courant ne peut plus déplacer aucun anneau, la partie est nulle.

## Structure

```
├── pom.xml                      POM parent (Java, JUnit)
├── .github/workflows/maven.yml  CI : mvn verify
├── HexagonalCoordinate/         Coordonnées cubiques [q, r, s] et doublées [ligne, colonne]
│   └── src/main|test/java/fr/saegroupe8/iut/{Coordinate,Exception}
├── OthelloEngine/               Dépend de HexagonalCoordinate
│   └── src/main|test/java/fr/saegroupe8/iut/
│       ├── CUIMain.java         Placement des anneaux et affichage en console
│       ├── model/               Model, Team, actions/, state/, tokens/, factory/
│       └── IA/                  AI, MinimaxAI, Node, MainAI (joueur contre IA)
└── OthelloGUI/                  Interface JavaFX (voir son README)
```

## Conception

- **`State`** : état de jeu immuable (`HashMap<Coordinate, Token>`, joueur courant, lignes présentes). Chaque coup renvoie un nouvel état.
- **`IState`** : opérations utilisées par l'IA et l'interface (`move`, `removeLine`, `availableMoves`, `winner`, `isInField`, `lines`, `rings`, etc.).
- **`Action`** : type commun à `Move` (départ, arrivée) et `RemoveLine` (5 cases, anneau retiré).
- **`Model`** : garde l'état courant et délègue à `IState`.
- **`IFactory`** : fabrique les terrains (vide, de test), en cubique (`FactoryCube`) ou doublé (`FactoryDoubled`).

**IA.** `MinimaxAI` explore l'arbre jusqu'à une profondeur donnée avec élagage alpha-bêta. Évaluation d'une position : victoire ou défaite ±100 000, anneaux retirés d'avance ±1 000 chacun, mobilité ±0,5 par case d'écart, pions d'avance ±2 chacun.

## Utilisation

Prérequis : JDK 26, Maven.

```bash
mvn clean verify        # compile et lance les tests JUnit 5
mvn package
```

Jouer contre l'IA (vous avez les blancs, profondeur 3). Les coordonnées se saisissent en cubique, `q r s` :

```bash
java -cp OthelloEngine/target/classes:HexagonalCoordinate/target/classes fr.saegroupe8.iut.IA.MainAI
# Windows : java -cp "OthelloEngine\target\classes;HexagonalCoordinate\target\classes" ...
```

Placer des anneaux et afficher le terrain (`Z` `Q` `S` `D` pour le curseur, `X` pour poser un anneau) :

```bash
java -cp OthelloEngine/target/classes:HexagonalCoordinate/target/classes fr.saegroupe8.iut.CUIMain
```

Affichage : `o`/`O` anneau blanc/noir, `.`/`x` pion blanc/noir, `_` case vide, `*` curseur.

## Répartition

Elyas Ahmadi : `removeLine`, `isInField`, `getPawnsLines`, classe `Action`, interface `AI`, classe `MainAI`.

## Usage de l'IA

Des outils d'IA générative ont servi d'assistant : compréhension d'erreurs, débogage, relecture et mise en forme de la documentation. La conception et la logique du projet ont été réalisées par l'équipe.

## Organisation Git

`master` ne reçoit que des fonctionnalités terminées, une branche par fonctionnalité (`feature/remove-line`, `feature/ai`…), fusionnée après test.
