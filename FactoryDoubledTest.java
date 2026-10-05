package FactoryTest;

import static org.junit.jupiter.api.Assertions.*;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateDoubled;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.factory.FactoryDoubled;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class FactoryDoubledTest {

    private FactoryDoubled factory;

    @BeforeEach
    public void setUp() {
        factory = new FactoryDoubled();
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

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases, même après placement des jetons.");

        assertEquals(5, countTokens(board, Ring.class, Team.WHITE), "Il doit y avoir 5 anneaux blancs.");
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK), "Il doit y avoir 5 anneaux noirs.");
        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE), "Il doit y avoir 4 pions blancs.");
        assertEquals(6, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 6 pions noirs.");

        Token testPawn = board.get(new CoordinateDoubled(10, 12));
        assertNotNull(testPawn, "Il devrait y avoir un jeton en [y=10, x=12]");
        assertTrue(testPawn instanceof Pawn);
        assertEquals(Team.WHITE, testPawn.getTeam());
    }

    @Test
    public void testStateForBlackLineTest() {
        IState state = factory.stateForBlackLineTest();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases.");

        assertEquals(4, countTokens(board, Ring.class, Team.WHITE));
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK));
        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE));
        assertEquals(25, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 25 pions noirs.");

        assertTrue(board.get(new CoordinateDoubled(8, 16)) instanceof Pawn);
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

        Token testRing = board.get(new CoordinateDoubled(5, 1));
        assertNotNull(testRing);
        assertTrue(testRing instanceof Ring);
        assertEquals(Team.BLACK, testRing.getTeam());
    }

    @Test
    public void testDoubleLineStateTest() {
        IState state = factory.doubleLineStateTest();
        Map<Coordinate, Token> board = state.board();

        assertEquals(85, board.size(), "Le plateau doit contenir 89 cases.");

        assertEquals(5, countTokens(board, Ring.class, Team.WHITE));
        assertEquals(5, countTokens(board, Ring.class, Team.BLACK));

        assertEquals(4, countTokens(board, Pawn.class, Team.WHITE), "Il doit y avoir 5 pions blancs formant une ligne.");
        assertEquals(4, countTokens(board, Pawn.class, Team.BLACK), "Il doit y avoir 5 pions noirs formant une ligne.");

        Token centerToken = board.get(new CoordinateDoubled(5, 11));
        assertNotNull(centerToken, "Le centre de la ligne [y=5, x=11] ne doit pas être vide.");
        assertTrue(centerToken instanceof Pawn);
        assertEquals(Team.BLACK, centerToken.getTeam());
    }

    /**
     * Méthode utilitaire pour compter le nombre de jetons d'un certain type et d'une certaine équipe.
     */
    private long countTokens(Map<Coordinate, Token> board, Class<? extends Token> tokenType, Team team) {
        return board.values().stream()
                .filter(token -> token != null)
                .filter(tokenType::isInstance)
                .filter(token -> token.getTeam() == team)
                .count();
    }
}