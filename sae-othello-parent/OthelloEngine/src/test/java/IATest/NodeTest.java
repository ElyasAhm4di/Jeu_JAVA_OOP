package IATest;

import fr.saegroupe8.iut.IA.Node;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.factory.FactoryCube;
import fr.saegroupe8.iut.model.state.IState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    private IState etat;

    @BeforeEach
    public void setUp() {
        etat = new FactoryCube().testState();
    }

    @Test
    public void testConstructeurEtGetters() {
        Action action = new Move(new CoordinateCube(0, 0, 0), new CoordinateCube(1, -1, 0));
        Node parent = new Node(etat, null, null);
        Node enfant = new Node(etat, parent, action);

        assertEquals(etat, enfant.getState(), "getState doit renvoyer l'etat passe au constructeur.");
        assertEquals(parent, enfant.getParent(), "getParent doit renvoyer le noeud parent.");
        assertEquals(action, enfant.getAction(), "getAction doit renvoyer l'action passee au constructeur.");
    }

    @Test
    public void testNoeudRacine() {
        // un noeud racine n'a ni parent ni action (comme dans chooseMove)
        Node racine = new Node(etat, null, null);

        assertNull(racine.getParent(), "Le noeud racine n'a pas de parent.");
        assertNull(racine.getAction(), "Le noeud racine n'a pas d'action.");
        assertNotNull(racine.getState(), "Le noeud racine doit quand meme avoir un etat.");
    }
}