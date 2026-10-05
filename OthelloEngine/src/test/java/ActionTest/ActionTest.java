package ActionTest;


import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {


    @Test
    public void testMoveConstructorAndGetters() {
        Coordinate from = new CoordinateCube(0, 0, 0);
        Coordinate to = new CoordinateCube(1, -1, 0);

        Move move = new Move(from, to);

        assertEquals(from, move.getFrom(), "Le getter getFrom doit renvoyer la coordonnée initiale.");
        assertEquals(to, move.getTo(), "Le getter getTo doit renvoyer la coordonnée d'arrivée.");
        assertTrue(move instanceof Action, "Move doit bien hériter de la classe abstraite Action.");
    }

    @Test
    public void testMoveSetters() {
        Move move = new Move(new CoordinateCube(0, 0, 0), new CoordinateCube(1, -1, 0));

        Coordinate newFrom = new CoordinateCube(5, -5, 0);
        Coordinate newTo = new CoordinateCube(-5, 5, 0);

        move.setFrom(newFrom);
        move.setTo(newTo);

        assertEquals(newFrom, move.getFrom(), "Le setter setFrom doit mettre à jour la coordonnée.");
        assertEquals(newTo, move.getTo(), "Le setter setTo doit mettre à jour la coordonnée.");
    }


    @Test
    public void testRemoveLineConstructorAndGetters() {
        Set<Coordinate> originalLine = new HashSet<>();
        originalLine.add(new CoordinateCube(0, 0, 0));
        originalLine.add(new CoordinateCube(1, 0, -1));

        Coordinate ring = new CoordinateCube(-4, 0, 4);

        RemoveLine removeLine = new RemoveLine(originalLine, ring);

        assertEquals(ring, removeLine.getRing(), "Le getter getRing doit renvoyer le bon anneau.");
        assertEquals(2, removeLine.getLine().size(), "La ligne doit contenir les 2 coordonnées initiales.");
        assertTrue(removeLine.getLine().containsAll(originalLine), "La ligne doit contenir les éléments passés au constructeur.");
        assertTrue(removeLine instanceof Action, "RemoveLine doit bien hériter de la classe abstraite Action.");

        originalLine.add(new CoordinateCube(2, 0, -2));
        assertEquals(2, removeLine.getLine().size(), "Modifier le Set d'origine ne doit pas modifier le Set interne de l'action !");
    }

    @Test
    public void testRemoveLineSetters() {
        RemoveLine removeLine = new RemoveLine(new HashSet<>(), new CoordinateCube(0, 0, 0));

        Set<Coordinate> newLine = new HashSet<>();
        newLine.add(new CoordinateCube(5, -5, 0));
        Coordinate newRing = new CoordinateCube(-5, 5, 0);

        removeLine.setLine(newLine);
        removeLine.setRing(newRing);

        assertEquals(newRing, removeLine.getRing());
        assertEquals(newLine, removeLine.getLine());
    }

    @Test
    public void testRemoveLineAddToLine() {
        RemoveLine removeLine = new RemoveLine(new HashSet<>(), new CoordinateCube(0, 0, 0));

        assertTrue(removeLine.getLine().isEmpty(), "La ligne doit être vide au départ.");

        Coordinate newCoord = new CoordinateCube(1, -1, 0);
        removeLine.addToLine(newCoord);

        assertEquals(1, removeLine.getLine().size(), "La ligne doit contenir 1 élément après addToLine.");
        assertTrue(removeLine.getLine().contains(newCoord), "L'élément ajouté doit être présent dans le Set.");
    }
}