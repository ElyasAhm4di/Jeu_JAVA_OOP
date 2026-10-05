package fr.saegroupe8.iut.model.factory;


import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.state.State;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import java.util.*;

public class FactoryCube implements IFactory {

    @Override
    public IState testState() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateCube(4, -3, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, -2, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -1, -3), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, 0, -3), new Ring(Team.WHITE));
        board.put(new CoordinateCube(2, 1, -3), new Ring(Team.WHITE));

        board.put(new CoordinateCube(-4, 0, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(2, -1, -1), new Ring(Team.BLACK));
        board.put(new CoordinateCube(0, 2, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-1, 3, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(1, 4, -5), new Ring(Team.BLACK));

        board.put(new CoordinateCube(3, -5, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-1, -1, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-2, 3, -1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-1, 5, -4), new Pawn(Team.WHITE));

        board.put(new CoordinateCube(2, -5, 3), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(-2, -1, 3), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(0, -1, 1), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(-2, 0, 2), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(-4, 1, 3), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(-3, 4, -1), new Pawn(Team.BLACK));

        List<Set<Coordinate>> lines = new ArrayList<>();
        return new State(board, Team.WHITE, lines);
    }

    @Override
    public IState stateForBlackLineTest() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateCube(3, -2, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -3, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, 0, -3), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -1, -3), new Ring(Team.WHITE));
        board.put(new CoordinateCube(2, 1, -3), new Ring(Team.WHITE));

        board.put(new CoordinateCube(-4, 0, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(2, -1, -1), new Ring(Team.BLACK));
        board.put(new CoordinateCube(0, 2, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-1, 3, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(1, 4, -5), new Ring(Team.BLACK));

        board.put(new CoordinateCube(3, -5, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(0, -2, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-2, 3, -1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-1, 5, -4), new Pawn(Team.WHITE));

        CoordinateCube[] blackPawns = {
                new CoordinateCube(0, -4, 4), new CoordinateCube(1, -4, 3), new CoordinateCube(2, -4, 2),
                new CoordinateCube(-1, -3, 4), new CoordinateCube(1, -3, 2), new CoordinateCube(-1, -2, 3),
                new CoordinateCube(1, -2, 1), new CoordinateCube(-2, -1, 3), new CoordinateCube(-1, -1, 2),
                new CoordinateCube(0, -1, 1), new CoordinateCube(3, -1, -2), new CoordinateCube(4, -1, -3),
                new CoordinateCube(5, -1, -4), new CoordinateCube(-3, 1, 2), new CoordinateCube(3, 1, -4),
                new CoordinateCube(4, 1, -5), new CoordinateCube(-4, 2, 2), new CoordinateCube(1, 2, -3),
                new CoordinateCube(2, 2, -4), new CoordinateCube(3, 2, -5), new CoordinateCube(-4, 3, 1),
                new CoordinateCube(2, 3, -5), new CoordinateCube(-3, 4, -1),
                new CoordinateCube(1, 3, -4), new CoordinateCube(0, 3, -3)
        };

        for (CoordinateCube c : blackPawns) {
            board.put(c, new Pawn(Team.BLACK));
        }

        List<Set<Coordinate>> lines = new ArrayList<>();
        return new State(board, Team.BLACK, lines);
    }

    @Override
    public IState stateForWhiteLineTest() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateCube(3, -2, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -3, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, 0, -3), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -1, -3), new Ring(Team.WHITE));
        board.put(new CoordinateCube(2, 1, -3), new Ring(Team.WHITE));

        board.put(new CoordinateCube(-4, 0, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(2, -1, -1), new Ring(Team.BLACK));
        board.put(new CoordinateCube(0, 2, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-1, 3, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(1, 4, -5), new Ring(Team.BLACK));

        board.put(new CoordinateCube(3, -5, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(0, -2, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-2, 3, -1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-1, 5, -4), new Pawn(Team.WHITE));

        CoordinateCube[] blackPawns = {
                new CoordinateCube(0, -4, 4), new CoordinateCube(1, -4, 3), new CoordinateCube(2, -4, 2),
                new CoordinateCube(-1, -3, 4), new CoordinateCube(1, -3, 2), new CoordinateCube(-1, -2, 3),
                new CoordinateCube(1, -2, 1), new CoordinateCube(-4, -1, 5), new CoordinateCube(-3, -1, 4),
                new CoordinateCube(-2, -1, 3), new CoordinateCube(-1, -1, 2), new CoordinateCube(0, -1, 1),
                new CoordinateCube(3, -1, -2), new CoordinateCube(4, -1, -3), new CoordinateCube(5, -1, -4),
                new CoordinateCube(-3, 1, 2), new CoordinateCube(3, 1, -4), new CoordinateCube(4, 1, -5),
                new CoordinateCube(-4, 2, 2), new CoordinateCube(1, 2, -3), new CoordinateCube(2, 2, -4),
                new CoordinateCube(3, 2, -5), new CoordinateCube(-4, 3, 1), new CoordinateCube(2, 3, -5),
                new CoordinateCube(-3, 4, -1), new CoordinateCube(1, 3, -4), new CoordinateCube(0, 3, -3)
        };

        for (CoordinateCube c : blackPawns) {
            board.put(c, new Pawn(Team.BLACK));
        }

        List<Set<Coordinate>> lines = new ArrayList<>();
        return new State(board, Team.WHITE, lines);
    }

    @Override
    public IState doubleLineStateTest() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateCube(1, -2, 1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, -2, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -3, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, -1, -2), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -1, -3), new Ring(Team.WHITE));

        board.put(new CoordinateCube(-4, 0, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(2, -1, -1), new Ring(Team.BLACK));
        board.put(new CoordinateCube(0, 2, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-1, 3, -2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(1, 4, -5), new Ring(Team.BLACK));

        board.put(new CoordinateCube(-2, -1, 3), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(-1, -1, 2), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(0, -1, 1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(1, -1, 0), new Pawn(Team.BLACK));

        board.put(new CoordinateCube(-1, 0, 1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(0, 0, 0), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(1, 0, -1), new Pawn(Team.BLACK));
        board.put(new CoordinateCube(2, 0, -2), new Pawn(Team.WHITE));

        List<Set<Coordinate>> lines = new ArrayList<>();
        return new State(board, Team.WHITE, lines);
    }

    @Override
    public IState emptyState() {
        return new State(emptyBoardGenerator(), Team.BLACK, new ArrayList<>());
    }

    private Map<Coordinate, Token> emptyBoardGenerator() {
        Map<Coordinate, Token> board = new HashMap<>();
        int radius = 5;

        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);

            for (int r = r1; r <= r2; r++) {
                int s = -q - r;

                // Suppression des 6 coins de l'hexagone pour obtenir exactement 85 cases
                if ((q == 0 && r == -5 && s == 5) ||
                        (q == 5 && r == -5 && s == 0) ||
                        (q == -5 && r == 0 && s == 5) ||
                        (q == 5 && r == 0 && s == -5) ||
                        (q == -5 && r == 5 && s == 0) ||
                        (q == 0 && r == 5 && s == -5)) {
                    continue; // On passe ces 6 cases
                }

                CoordinateCube coord = new CoordinateCube(q, r, s);
                board.put(coord, null);
            }
        }
        return board;
    }
}
