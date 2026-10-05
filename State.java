package fr.saegroupe8.iut.model.state;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;
import fr.saegroupe8.iut.Coordinate.Direction;
import fr.saegroupe8.iut.Coordinate.Mode;
import fr.saegroupe8.iut.model.tokens.Pawn;

import java.util.*;

public class State implements IState {

    private final Map<Coordinate, Token> board;
    private final Team turn;
    private final List<Set<Coordinate>> lines;

    public State(Map<Coordinate, Token> board, Team turn, List<Set<Coordinate>> lines) {
        this.board = board;
        this.turn = turn;
        this.lines = lines;
    }

    @Override
    public IState move(Move move) throws Exception {
        Coordinate from = move.getFrom();
        Coordinate to = move.getTo();

        // Vérifications
        if (!this.board().containsKey(from) || !this.board().containsKey(to)) throw new IndexOutOfBoundsException();
        if (!this.lines().isEmpty()) throw new RuntimeException();
        Token tokenFrom = board().get(from);
        if (!(tokenFrom instanceof Ring) || tokenFrom.getTeam() != this.turn()) throw new IllegalArgumentException();
        if(!availableMoves(from).contains(to)) throw new IllegalArgumentException();

        // Création d'un nouveau plateau
        Map<Coordinate, Token> newBoard = this.deepCopyBoard();

        // On remplace l'anneau du début par un pion
        newBoard.put(from, new Pawn(this.turn()));

        // On change la couleur des pions par dessus lequel est passé l'anneau
        List<Coordinate> path = from.between(Mode.POINTY, to);
        for (Coordinate c : path) {
            Token t = newBoard.get(c);
            if (t instanceof Pawn)
                newBoard.put(c, new Pawn(t.getTeam().other()));
        }

        // On replace l'anneau à la fin de sa trajectoire
        newBoard.put(to, new Ring(this.turn()));

        List<Set<Coordinate>> newLines = getPawnsLines(newBoard, Mode.POINTY);

        Team nextTurn = (!newLines.isEmpty() ? this.turn() : this.turn().other());

        // Et enfin on renvoi le nouveau State
        return new State(newBoard, nextTurn, newLines);
    }

    @Override
    public IState removeLine(RemoveLine removeLine) throws RuntimeException {

        if (removeLine.getLine().isEmpty()) throw new RuntimeException();
        if (removeLine.getLine().size() != 5) throw new RuntimeException();

        Map<Coordinate, Token> newBoard = this.deepCopyBoard();
        Team team = null; // Ici la team de référence sera initiée comme étant la team du premier pion
        for (Coordinate c : removeLine.getLine()) {
            Token t = newBoard.get(c);
            if (!(t instanceof Pawn)) throw new RuntimeException(); // S'il ne s'agit pas d'un pion alors il y a un problème...

            if (team == null) {
                team = t.getTeam();
            }

            if (t.getTeam() != team) throw new RuntimeException(); // On vérifie si la team du pion actuel est la même que la team de référence ou non
            else { // Si c'est bon on retire le pion du plateau
                newBoard.put(c, null);
            }

        }
        newBoard.put(removeLine.getRing(), null);

        List<Set<Coordinate>> newLines = getPawnsLines(newBoard, Mode.POINTY);

        return new State(newBoard, this.turn(), newLines);
    }

    @Override
    public Set<Coordinate> availableMoves(Coordinate from) {

        Set<Coordinate> legalMoves = new HashSet<>();

        for (Direction dir : Direction.values()) { // On va regarder dans toutes les directions une à une
            Coordinate current = from;
            boolean foundPawn = false;

            while (true) { // On fait une boucle qui va regarder dans cette direction jusqu'à ne plus pouvoir
                try {
                    current = current.toDir(Mode.POINTY, dir);

                    if (!board.containsKey(current)) break; // Si on est plus dans le plateau on casse la boucle

                    Token t = board().get(current);

                    // Lorsqu'on croise un anneau on casse la boucle parce qu'un anneau ne peut pas aller par dessus un autre
                    if (t instanceof Ring) break;

                    // Lorsqu'on rencontre un pion, on ne peut pas s'arrêter dessus mais on va s'arrêter juste après
                    if (t instanceof Pawn) {
                        foundPawn = true;
                        continue;
                    }

                    if (t == null) {
                        legalMoves.add(current); // On ajoute la case actuellement vérifiée si elle est vide
                        if (!foundPawn)
                            continue; // Si on a eu un ou plusieurs pions avant on s'arrête là et on va vérifier dans la direction suivante
                        else break; // Sinon on peut continuer jusqu'à ne plus pouvoir
                    }
                } catch (RuntimeException e) {
                    break;
                }
            }
        }

        return legalMoves;
    }

    @Override
    public Map<Coordinate, Token> board() {
        return board;
    }

    @Override
    public Map<Team, List<Coordinate>> rings() {
        Map<Team, List<Coordinate>> result = new HashMap<>();
        result.put(Team.BLACK, new ArrayList<>());
        result.put(Team.WHITE, new ArrayList<>());
        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            if (entry.getValue() instanceof Ring) {
                result.get(entry.getValue().getTeam()).add(entry.getKey());
            }
        }
        return result;
    }

    @Override
    public List<Set<Coordinate>> lines() {
        return lines;
    }

    @Override
    public Team turn() {
        return turn;
    }

    public boolean isInField(Coordinate c) {
        return board.containsKey(c);
    }

    public Team winner() {
        List<Coordinate> whiteRings = rings().get(Team.WHITE);
        List<Coordinate> blackRings = rings().get(Team.BLACK);

        if (whiteRings == null || blackRings == null || (whiteRings.isEmpty() && blackRings.isEmpty())) {
            return null;
        }

        if (whiteRings.size() <= 2) {
            return Team.WHITE;
        }

        if (blackRings.size() <= 2) {
            return Team.BLACK;
        }

        return null;
    }

    @Override
    public IState removeToken(Coordinate coord) {
        Map<Coordinate, Token> newBoard = deepCopyBoard();
        newBoard.put(coord, null);
        List<Set<Coordinate>> newLines = getPawnsLines(newBoard, Mode.POINTY);
        return new State(newBoard, turn, newLines);
    }

    @Override
    public IState toggleToken(Coordinate position, Team team, Class<?> tokenClass) {
        Map<Coordinate, Token> newBoard = deepCopyBoard();
        Token existing = newBoard.get(position);

        if (existing != null
                && existing.getClass().equals(tokenClass)
                && existing.getTeam() == team) {
            newBoard.put(position, null);
        } else {
            try {
                Token newToken = (Token) tokenClass.getConstructors()[0].newInstance(team);
                newBoard.put(position, newToken);
            } catch (Exception e) {
                throw new RuntimeException("Impossible de creer une instance de " + tokenClass.getName(), e);
            }
        }

        List<Set<Coordinate>> newLines = getPawnsLines(newBoard, Mode.POINTY);
        return new State(newBoard, turn, newLines);
    }

    private Map<Coordinate, Token> deepCopyBoard() {
        Map<Coordinate, Token> copy = new HashMap<>();
        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            Token t = entry.getValue();
            if (t == null)
                copy.put(entry.getKey(), null);
            else if (t instanceof Pawn)
                copy.put(entry.getKey(), new Pawn(t.getTeam()));
            else if (t instanceof Ring)
                copy.put(entry.getKey(), new Ring(t.getTeam()));
        }
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(board, state.board) && turn == state.turn && Objects.equals(lines, state.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn, lines);
    }


    public static List<Set<Coordinate>> getPawnsLines(Map<Coordinate, Token> board, Mode mode) {
        List<Set<Coordinate>> result = new ArrayList<>();
        Set<Set<Coordinate>> dejaVu = new HashSet<>();

        Direction[] directions;
        if (mode == Mode.POINTY) {
            directions = new Direction[]{Direction.NO, Direction.NE, Direction.E};
        } else {
            directions = new Direction[]{Direction.N, Direction.NO, Direction.NE};
        }

        for (Coordinate c : board.keySet()) {
            if (!(board.get(c) instanceof Pawn)) continue;
            Pawn pawn = (Pawn) board.get(c);
            Team couleur = pawn.getTeam();

            for (Direction dir : directions) {
                Direction dirOpposee = dir.opposite();

                Coordinate debut = c;
                try {
                    while (true) {
                        Coordinate prec = debut.toDir(mode, dirOpposee);
                        if (!board.containsKey(prec)) break;
                        Token t = board.get(prec);
                        if (t instanceof Pawn && t.getTeam() == couleur) {
                            debut = prec;
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {}

                List<Coordinate> ligne = new ArrayList<>();
                Coordinate courant = debut;
                try {
                    while (board.containsKey(courant)) {
                        Token t = board.get(courant);
                        if (t instanceof Pawn && t.getTeam() == couleur) {
                            ligne.add(courant);
                            courant = courant.toDir(mode, dir);
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {}

                for (int i = 0; i <= ligne.size() - 5; i++) {
                    Set<Coordinate> fenetre = new HashSet<>(ligne.subList(i, i + 5));
                    if (!dejaVu.contains(fenetre)) {
                        dejaVu.add(fenetre);
                        result.add(fenetre);
                    }
                }
            }
        }
        return result;
    }

}