package fr.saegroupe8.iut.model.state;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import fr.saegroupe8.iut.model.tokens.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IState {

    IState move(Move move) throws Exception;
    IState removeLine(RemoveLine removeLine);
    Set<Coordinate> availableMoves(Coordinate from);
    Map<Coordinate, Token> board();
    Map<Team, List<Coordinate>> rings();
    List<Set<Coordinate>> lines();
    Team turn();
    IState removeToken(Coordinate c);
    IState toggleToken(Coordinate position, Team team, Class<?> tokenClass);
    Team winner();
    boolean isInField(Coordinate c);

    static List<Set<Coordinate>> getPawnsLines(Map<Coordinate, Token> board) {
        return null;
    }
}