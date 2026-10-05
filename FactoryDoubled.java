package fr.saegroupe8.iut.model.factory;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateDoubled;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.state.State;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import java.util.*;

public class FactoryDoubled implements IFactory {

    @Override
    public IState testState() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateDoubled(2, 14), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(3, 13), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(4, 16), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(5, 15), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(6, 14), new Ring(Team.WHITE));

        board.put(new CoordinateDoubled(5, 1), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(4, 12), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(7, 11), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(8, 10), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(9, 15), new Ring(Team.BLACK));

        board.put(new CoordinateDoubled(0, 10), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(4, 6), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(8, 8), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(10, 12), new Pawn(Team.WHITE));

        CoordinateDoubled[] blackPawns = {
                new CoordinateDoubled(0, 8), new CoordinateDoubled(4, 4),
                new CoordinateDoubled(4, 8), new CoordinateDoubled(5, 5),
                new CoordinateDoubled(6, 2), new CoordinateDoubled(9, 7)
        };
        for (CoordinateDoubled c : blackPawns) {
            board.put(c, new Pawn(Team.BLACK));
        }

        return new State(board, Team.WHITE, new ArrayList<>());
    }

    @Override
    public IState stateForBlackLineTest() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateDoubled(3, 13), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(2, 14), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(5, 15), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(4, 16), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(6, 14), new Ring(Team.WHITE));

        board.put(new CoordinateDoubled(5, 1), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(4, 12), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(7, 11), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(8, 10), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(9, 15), new Ring(Team.BLACK));

        board.put(new CoordinateDoubled(0, 10), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(3, 7), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(8, 8), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(10, 12), new Pawn(Team.WHITE));

        CoordinateDoubled[] blackPawns = {
                new CoordinateDoubled(1, 5), new CoordinateDoubled(1, 7), new CoordinateDoubled(1, 9),
                new CoordinateDoubled(2, 4), new CoordinateDoubled(2, 8), new CoordinateDoubled(3, 5),
                new CoordinateDoubled(3, 9), new CoordinateDoubled(4, 4), new CoordinateDoubled(4, 6),
                new CoordinateDoubled(4, 8), new CoordinateDoubled(4, 14), new CoordinateDoubled(4, 16),
                new CoordinateDoubled(4, 18), new CoordinateDoubled(6, 4), new CoordinateDoubled(6, 16),
                new CoordinateDoubled(6, 18), new CoordinateDoubled(7, 3), new CoordinateDoubled(7, 13),
                new CoordinateDoubled(7, 15), new CoordinateDoubled(7, 17), new CoordinateDoubled(8, 4),
                new CoordinateDoubled(8, 16), new CoordinateDoubled(9, 7), new CoordinateDoubled(8, 14),
                new CoordinateDoubled(8, 12)
        };
        for (CoordinateDoubled c : blackPawns) {
            board.put(c, new Pawn(Team.BLACK));
        }

        return new State(board, Team.BLACK, new ArrayList<>());
    }

    @Override
    public IState stateForWhiteLineTest() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateDoubled(3, 13), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(2, 14), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(5, 15), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(4, 16), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(6, 14), new Ring(Team.WHITE));

        board.put(new CoordinateDoubled(5, 1), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(4, 12), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(7, 11), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(8, 10), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(9, 15), new Ring(Team.BLACK));

        board.put(new CoordinateDoubled(0, 10), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(3, 7), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(8, 8), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(10, 12), new Pawn(Team.WHITE));

        CoordinateDoubled[] blackPawns = {
                new CoordinateDoubled(1, 5), new CoordinateDoubled(1, 7), new CoordinateDoubled(1, 9),
                new CoordinateDoubled(2, 4), new CoordinateDoubled(2, 8), new CoordinateDoubled(3, 5),
                new CoordinateDoubled(3, 9), new CoordinateDoubled(4, 0), new CoordinateDoubled(4, 2),
                new CoordinateDoubled(4, 4), new CoordinateDoubled(4, 6), new CoordinateDoubled(4, 8),
                new CoordinateDoubled(4, 14), new CoordinateDoubled(4, 16), new CoordinateDoubled(4, 18),
                new CoordinateDoubled(6, 4), new CoordinateDoubled(6, 16), new CoordinateDoubled(6, 18),
                new CoordinateDoubled(7, 3), new CoordinateDoubled(7, 13), new CoordinateDoubled(7, 15),
                new CoordinateDoubled(7, 17), new CoordinateDoubled(8, 4), new CoordinateDoubled(8, 16),
                new CoordinateDoubled(9, 7), new CoordinateDoubled(8, 14), new CoordinateDoubled(8, 12)
        };
        for (CoordinateDoubled c : blackPawns) {
            board.put(c, new Pawn(Team.BLACK));
        }

        return new State(board, Team.WHITE, new ArrayList<>());
    }

    @Override
    public IState doubleLineStateTest() {
        Map<Coordinate, Token> board = emptyBoardGenerator();

        board.put(new CoordinateDoubled(3, 9), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(3, 13), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(2, 14), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(4, 14), new Ring(Team.WHITE));
        board.put(new CoordinateDoubled(4, 16), new Ring(Team.WHITE));

        board.put(new CoordinateDoubled(5, 1), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(4, 12), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(7, 11), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(8, 10), new Ring(Team.BLACK));
        board.put(new CoordinateDoubled(9, 15), new Ring(Team.BLACK));

        board.put(new CoordinateDoubled(4, 4), new Pawn(Team.BLACK));
        board.put(new CoordinateDoubled(4, 6), new Pawn(Team.BLACK));
        board.put(new CoordinateDoubled(4, 8), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(4, 10), new Pawn(Team.BLACK));

        board.put(new CoordinateDoubled(5, 7), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(5, 9), new Pawn(Team.WHITE));
        board.put(new CoordinateDoubled(5, 11), new Pawn(Team.BLACK));
        board.put(new CoordinateDoubled(5, 13), new Pawn(Team.WHITE));

        List<Set<Coordinate>> lines = new ArrayList<>();
        return new State(board, Team.WHITE, lines);
    }

    @Override
    public IState emptyState() {
        return new State(emptyBoardGenerator(), Team.BLACK, new ArrayList<>());
    }

    private Map<Coordinate, Token> emptyBoardGenerator() {
        Map<Coordinate, Token> board = new HashMap<>();

        int[][] lignes = {
            {0, 6, 12},
            {1, 3, 15},
            {2, 2, 16},
            {3, 1, 17},
            {4, 0, 18},
            {5, 1, 17},
            {6, 0, 18},
            {7, 1, 17},
            {8, 2, 16},
            {9, 3, 15},
            {10, 6, 12}
        };

        for (int[] ligne : lignes) {
            int row = ligne[0];
            int colMin = ligne[1];
            int colMax = ligne[2];
            for (int col = colMin; col <= colMax; col += 2) {
                board.put(new CoordinateDoubled(row, col), null);
            }
        }

        return board;
    }
}