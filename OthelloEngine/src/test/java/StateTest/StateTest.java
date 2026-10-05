package StateTest;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.factory.FactoryCube;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.state.State;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    private FactoryCube factory;

    @BeforeEach
    public void setUp() {
        factory = new FactoryCube();
    }


    @Test
    public void testWinnerNoWinnerAtStart() {
        IState state = factory.emptyState();
        assertNull(state.winner(), "Au début, sans aucun anneau retiré, il ne doit pas y avoir de vainqueur.");
    }

    @Test
    public void testWinnerWhiteWins() {
        Map<Coordinate, Token> board = factory.emptyState().board();

        board.put(new CoordinateCube(0, 0, 0), new Ring(Team.WHITE));
        board.put(new CoordinateCube(1, 0, -1), new Ring(Team.WHITE));

        board.put(new CoordinateCube(-1, 0, 1), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-2, 0, 2), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-3, 0, 3), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-4, 0, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-5, 1, 4), new Ring(Team.BLACK));

        List<Set<Coordinate>> lines = List.of();
        IState state = new State(board, Team.WHITE, lines);

        assertEquals(Team.WHITE, state.winner(), "L'équipe WHITE doit être déclarée gagnante car elle a moins de 3 anneaux.");
    }


    @Test
    public void testRemoveToken() {
        IState state = factory.testState();
        Coordinate target = new CoordinateCube(3, -5, 2);

        assertNotNull(state.board().get(target));

        IState newState = state.removeToken(target);

        assertNotNull(state.board().get(target), "Le State initial doit rester immuable.");
        assertNull(newState.board().get(target), "La case doit être vide dans le nouvel état.");
    }


    @Test
    public void testGetPawnsLinesNoLines() {
        IState state = factory.testState();
        List<Set<Coordinate>> detectedLines = State.getPawnsLines(state.board(), fr.saegroupe8.iut.Coordinate.Mode.POINTY);

        assertTrue(detectedLines.isEmpty(), "Le plateau testState ne devrait contenir aucune ligne de 5 pions.");
    }

    @Test
    public void testGetPawnsLinesDoubleLineShouldBeEmpty() {
        IState state = factory.doubleLineStateTest();
        List<Set<Coordinate>> detectedLines = State.getPawnsLines(state.board(), fr.saegroupe8.iut.Coordinate.Mode.POINTY);

        assertTrue(detectedLines.isEmpty(), "Le plateau doubleLineStateTest ne contient aucune ligne de 5 pions unicolores. Il doit renvoyer 0.");
    }


    @Test
    public void testMoveThrowsIndexOutOfBounds() {
        IState state = factory.testState();

        Move invalidMove = new Move(new CoordinateCube(0, 0, 0), new CoordinateCube(10, -10, 0));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            state.move(invalidMove);
        }, "Le déplacement vers une case hors-terrain doit lever une IndexOutOfBoundsException.");
    }

    @Test
    public void testMoveThrowsIllegalArgumentWhenNoRing() {
        IState state = factory.testState();

        Coordinate fromPawn = new CoordinateCube(3, -5, 2);
        Coordinate toEmpty = new CoordinateCube(3, -4, 1);
        Move invalidMove = new Move(fromPawn, toEmpty);

        assertThrows(IllegalArgumentException.class, () -> {
            state.move(invalidMove);
        }, "Bouger depuis une case sans anneau de la couleur du joueur actuel doit lever une IllegalArgumentException.");
    }

    @Test
    public void testGetPawnsLinesPerfectLineOfFive() {
        Map<Coordinate, Token> board = factory.emptyState().board();

        board.put(new CoordinateCube(-2, 0, 2), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(-1, 0, 1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(0, 0, 0), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(1, 0, -1), new Pawn(Team.WHITE));
        board.put(new CoordinateCube(2, 0, -2), new Pawn(Team.WHITE));

        List<Set<Coordinate>> detectedLines = State.getPawnsLines(board, fr.saegroupe8.iut.Coordinate.Mode.POINTY);

        assertEquals(1, detectedLines.size(), "L'algorithme devrait détecter exactement 1 ligne de 5 pions de même équipe.");

        Set<Coordinate> laLigne = detectedLines.get(0);
        assertTrue(laLigne.contains(new CoordinateCube(0, 0, 0)), "Le pion central [0,0,0] doit faire partie de l'alignement détecté.");
    }
}