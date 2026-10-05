package CoordinateDoubleTest;

import static org.junit.jupiter.api.Assertions.*;


import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateDoubled;
import fr.saegroupe8.iut.Coordinate.Mode;
import fr.saegroupe8.iut.Exception.DifferentAxisException;
import org.junit.jupiter.api.Test;
import java.security.InvalidParameterException;
import java.util.List;


public class CoordinateDoubledTest {

    @Test
    public void testConstructorAndGetters() {
        CoordinateDoubled coord = new CoordinateDoubled(9, 5);

        assertEquals(9, coord.getX());
        assertEquals(5, coord.getY());
    }

    @Test
    public void testEqualsAndHashCode() {
        CoordinateDoubled c1 = new CoordinateDoubled(4, 2);
        CoordinateDoubled c2 = new CoordinateDoubled(4, 2);
        CoordinateDoubled c3 = new CoordinateDoubled(8, 12);

        assertEquals(c1, c2, "Deux CoordinateDoubled identiques doivent être égales.");
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode(), "Leurs hashCodes doivent matcher pour la HashMap.");
    }

    @Test
    public void testTo2DCoordinate() {
        CoordinateDoubled coord = new CoordinateDoubled(15, 5);
        assertNotNull(coord.to2DCoordinate());
        assertEquals(15, coord.to2DCoordinate().x());
        assertEquals(5, coord.to2DCoordinate().y());
    }

    @Test
    public void testBetweenSuccessHorizontalAxis() throws DifferentAxisException {
        CoordinateDoubled from = new CoordinateDoubled(4, 5);
        CoordinateDoubled to = new CoordinateDoubled(8, 5);

        List<Coordinate> path = from.between(Mode.POINTY, to);

        assertEquals(1, path.size());
        assertEquals(new CoordinateDoubled(6, 5), path.get(0));
    }

    @Test
    public void testBetweenThrowsDifferentAxisException() {
        CoordinateDoubled from = new CoordinateDoubled(2, 2);
        CoordinateDoubled to = new CoordinateDoubled(5, 10);

        assertThrows(DifferentAxisException.class, () -> {
            from.between(Mode.POINTY, to);
        }, "Un déplacement en diagonale incorrect doit lever une DifferentAxisException.");
    }

    @Test
    public void testDirectionExceptionsBasedOnMode() {
        CoordinateDoubled coord = new CoordinateDoubled(9, 5);

        assertThrows(InvalidParameterException.class, () -> coord.N(Mode.POINTY));
        assertThrows(InvalidParameterException.class, () -> coord.S(Mode.POINTY));

        assertThrows(InvalidParameterException.class, () -> coord.E(Mode.FLAT));
        assertThrows(InvalidParameterException.class, () -> coord.O(Mode.FLAT));
    }
}