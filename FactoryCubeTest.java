package FactoryTest;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.factory.FactoryCube;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryCubeTest {

    private FactoryCube factory;

    @BeforeEach
    public void setUp() {
        factory = new FactoryCube();
    }

    @Test
    public void testEmptyState() {
        IState state = factory.emptyState();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau vide doit contenir exactement 89 cases.");

        for (Token token : board.values()) {
            assertNull(token, "Le plateau de départ doit être entièrement vide.");
        }
    }

    @Test
    public void testTestState() {
        IState state = factory.testState();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases.");

        assertEquals(5, countTokens(board, Ring.class, Team.WHITE), "Il doit y avoir 5 anneaux blancs.");
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK), "Il doit y avoir 5 anneaux noirs.");
        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE), "Il doit y avoir 4 pions blancs.");
        assertEquals(6, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 6 pions noirs.");

        assertTrue(board.get(new CoordinateCube(-1, 5, -4)) instanceof Pawn, "Un pion devrait être en [-1, 5, -4]");
        assertEquals(Team.WHITE, board.get(new CoordinateCube(-1, 5, -4)).getTeam());
    }

    @Test
    public void testStateForBlackLineTest() {
        IState state = factory.stateForBlackLineTest();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases.");

        assertEquals(4, countTokens(board, Ring.class, Team.WHITE));
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK));
        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE));
        assertEquals(25, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 25 pions noirs formant les amas.");

        assertTrue(board.get(new CoordinateCube(1, 3, -4)) instanceof Pawn);
        assertTrue(board.get(new CoordinateCube(0, 3, -3)) instanceof Pawn);
    }

    @Test
    public void testStateForWhiteLineTest() {
        IState state = factory.stateForWhiteLineTest();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases.");

        assertEquals(4, countTokens(board, Ring.class, Team.WHITE));
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK));
        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE));
        assertEquals(27, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 27 pions noirs.");

        assertTrue(board.get(new CoordinateCube(-4, 0, 4)) instanceof Ring);
        assertEquals(Team.BLACK, board.get(new CoordinateCube(-4, 0, 4)).getTeam());
    }

    @Test
    public void testDoubleLineStateTest() {
        IState state = factory.doubleLineStateTest();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases.");

        assertEquals(5, countTokens(board, Ring.class, Team.WHITE));
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK));

        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE), "Il doit y avoir 4 pions blancs formant une ligne.");
        assertEquals(4, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 4 pions noirs formant une ligne.");

        Token centerToken = board.get(new CoordinateCube(0, 0, 0));
        assertNotNull(centerToken, "Le centre [0,0,0] ne doit pas être vide.");
        assertTrue(centerToken instanceof Pawn);
        assertEquals(Team.WHITE, centerToken.getTeam());
    }

    /**
     * Méthode utilitaire pour compter le nombre de jetons d'un certain type et d'une certaine équipe.
     */
    private long countTokens(Map<Coordinate, Token> board, Class<? extends Token> tokenType, Team team) {
        return board.values().stream()
                .filter(token -> token != null)
                .filter(token -> tokenType.isInstance(token))
                .filter(token -> token.getTeam() == team)
                .count();
    }
}