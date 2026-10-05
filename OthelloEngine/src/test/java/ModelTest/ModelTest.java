package ModelTest;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Model;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.factory.FactoryCube;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    private Model model;
    private IState initialState;
    private FactoryCube factory;

    @BeforeEach
    public void setUp() {
        factory = new FactoryCube();
        initialState = factory.testState();
        model = new Model(initialState);
    }

    @Test
    public void testGetAndSetCurrentState() {
        assertEquals(initialState, model.getCurrentState(), "Le modèle doit renvoyer l'état avec lequel il a été initialisé.");

        IState newState = factory.emptyState();
        model.setCurrentState(newState);

        assertEquals(newState, model.getCurrentState(), "Le modèle doit bien mettre à jour son état courant.");
        assertNotEquals(initialState, model.getCurrentState());
    }

    @Test
    public void testIsInField() {
        Coordinate validCoord = new CoordinateCube(0, 0, 0);
        assertTrue(model.isInField(validCoord), "La case [0,0,0] doit être dans le terrain.");

        Coordinate invalidCoord = new CoordinateCube(10, -10, 0);
        assertFalse(model.isInField(invalidCoord), "La case [10,-10,0] est hors limite, isInField doit renvoyer false.");
    }

    @Test
    public void testGetTokenAt() {
        Coordinate ringCoord = new CoordinateCube(-4, 0, 4);
        Token token = model.getTokenAt(ringCoord);

        assertNotNull(token);
        assertTrue(token instanceof Ring);
        Assertions.assertEquals(Team.BLACK, token.getTeam());

        Coordinate emptyCoord = new CoordinateCube(0, 0, 0);
        assertNull(model.getTokenAt(emptyCoord), "La case [0,0,0] doit être vide dans ce testState.");
    }

    @Test
    public void testGetPawnReturnsOnlyPawns() {

        List<Coordinate> whitePawns = model.getPawn(Team.WHITE);
        List<Coordinate> blackPawns = model.getPawn(Team.BLACK);

        assertEquals(4, whitePawns.size(), "L'équipe blanche doit avoir exactement 4 pions sur ce plateau.");
        assertEquals(6, blackPawns.size(), "L'équipe noire doit avoir exactement 6 pions sur ce plateau.");

    }

    @Test
    public void testDelegationMethods() {
        assertEquals(initialState.turn(), model.getTurn());
        assertEquals(initialState.board(), model.getBoard());
        assertEquals(initialState.lines(), model.getPawnsLines());

        assertEquals(5, model.getRings(Team.WHITE).size());
    }
}