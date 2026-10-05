package IATest;

import fr.saegroupe8.iut.IA.AI;
import fr.saegroupe8.iut.IA.MinimaxAI;
import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import fr.saegroupe8.iut.model.factory.FactoryCube;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.state.State;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MinimaxAITest {

    private FactoryCube factory;
    private IState etat;

    @BeforeEach
    public void setUp() {
        factory = new FactoryCube();
        etat = factory.testState();
    }

    @Test
    public void testMinimaxEstUneAI() {
        MinimaxAI ia = new MinimaxAI(2, Team.BLACK);
        assertTrue(ia instanceof AI, "MinimaxAI doit implementer l'interface AI.");
    }

    @Test
    public void testChooseMoveRenvoieUnCoup() {
        MinimaxAI ia = new MinimaxAI(2, etat.turn());
        Action coup = ia.chooseMove(etat);
        assertNotNull(coup, "Sur un etat jouable, l'IA doit renvoyer un coup.");
    }

    @Test
    public void testChooseMoveRenvoieUnMoveSansLigne() {
        MinimaxAI ia = new MinimaxAI(2, etat.turn());
        Action coup = ia.chooseMove(etat);
        assertTrue(coup instanceof Move, "Sans ligne sur le plateau, le coup doit etre un deplacement.");
    }

    @Test
    public void testLeCoupChoisiEstLegal() {
        Team equipeQuiJoue = etat.turn();
        MinimaxAI ia = new MinimaxAI(2, equipeQuiJoue);
        Action coupBrut = ia.chooseMove(etat);

        assertNotNull(coupBrut, "L'IA doit renvoyer un coup.");
        assertTrue(coupBrut instanceof Move, "Sans ligne, le coup doit etre un Move.");

        Move coup = (Move) coupBrut;

        assertTrue(etat.rings().get(equipeQuiJoue).contains(coup.getFrom()),
                "Le coup doit partir d'un anneau de l'equipe qui joue.");

        Set<Coordinate> casesOk = etat.availableMoves(coup.getFrom());
        assertTrue(casesOk.contains(coup.getTo()),
                "La case d'arrivee doit faire partie des deplacements autorises.");
    }

    @Test
    public void testProfondeurUnFonctionne() {
        MinimaxAI ia = new MinimaxAI(1, etat.turn());
        assertNotNull(ia.chooseMove(etat), "L'IA doit renvoyer un coup meme en profondeur 1.");
    }

    @Test
    public void testChooseMoveRenvoieRemoveLineQuandLignePresente() {
        Map<Coordinate, Token> board = factory.emptyState().board();

        CoordinateCube[] pionsNoirs = {
                new CoordinateCube(-2, 0, 2), new CoordinateCube(-1, 0, 1),
                new CoordinateCube(0, 0, 0),  new CoordinateCube(1, 0, -1),
                new CoordinateCube(2, 0, -2)
        };
        Set<Coordinate> ligne = new HashSet<>();
        for (CoordinateCube c : pionsNoirs) {
            board.put(c, new Pawn(Team.BLACK));
            ligne.add(c);
        }

        board.put(new CoordinateCube(-4, 0, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-5, 1, 4), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-5, 2, 3), new Ring(Team.BLACK));
        board.put(new CoordinateCube(4, -3, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(3, -2, -1), new Ring(Team.WHITE));
        board.put(new CoordinateCube(4, -1, -3), new Ring(Team.WHITE));

        List<Set<Coordinate>> lignes = new ArrayList<>();
        lignes.add(ligne);
        IState avecLigne = new State(board, Team.BLACK, lignes);

        MinimaxAI ia = new MinimaxAI(2, Team.BLACK);
        Action coup = ia.chooseMove(avecLigne);

        assertNotNull(coup, "L'IA doit renvoyer un coup.");
        assertTrue(coup instanceof RemoveLine,
                "Quand une ligne est presente, l'IA doit choisir de retirer une ligne.");
    }
}