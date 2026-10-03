package CoordinateCubeTest;


import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.Coordinate.Mode;
import fr.saegroupe8.iut.Exception.DifferentAxisException;
import org.junit.jupiter.api.Test;
import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinateCubeTest {

    @Test
    public void testConstructorAndGetters() {
        CoordinateCube coord = new CoordinateCube(2, -3, 1);

        assertEquals(2, coord.getQ());
        assertEquals(-3, coord.getR());
        assertEquals(1, coord.getS());
    }

    @Test
    public void testInvalidConstructorThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CoordinateCube(1, 1, 1);
        }, "Le constructeur doit jeter une IllegalArgumentException si q + r + s != 0.");
    }

    @Test
    public void testEqualsAndHashCode() {
        CoordinateCube c1 = new CoordinateCube(0, 0, 0);
        CoordinateCube c2 = new CoordinateCube(0, 0, 0);
        CoordinateCube c3 = new CoordinateCube(2, -1, -1);

        assertEquals(c1, c2, "Deux coordonnées avec les mêmes composants doivent être égales.");
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode(), "Leurs hashCodes doivent correspondre.");
    }

    @Test
    public void testTo2DCoordinate() {
        CoordinateCube coord = new CoordinateCube(3, -2, -1);
        assertNotNull(coord.to2DCoordinate());
        assertEquals(3, coord.to2DCoordinate().x());
        assertEquals(-2, coord.to2DCoordinate().y());
    }

    @Test
    public void testBetweenSuccess() throws DifferentAxisException {
        CoordinateCube from = new CoordinateCube(0, 0, 0);
        CoordinateCube to = new CoordinateCube(2, 0, -2);

        List<Coordinate> path = from.between(Mode.POINTY, to);

        assertEquals(1, path.size(), "Il doit y avoir exactement une case intermédiaire.");
        assertEquals(new CoordinateCube(1, 0, -1), path.get(0));
    }

    @Test
    public void testBetweenThrowsDifferentAxisException() {
        CoordinateCube from = new CoordinateCube(0, 0, 0);
        CoordinateCube to = new CoordinateCube(2, 1, -3);

        assertThrows(DifferentAxisException.class, () -> {
            from.between(Mode.POINTY, to);
        }, "Lancer between sur des axes non alignés doit déclencher une DifferentAxisException.");
    }

    @Test
    public void testDirectionExceptionsBasedOnMode() {
        CoordinateCube coord = new CoordinateCube(0, 0, 0);

        assertThrows(InvalidParameterException.class, () -> coord.N(Mode.POINTY));
        assertThrows(InvalidParameterException.class, () -> coord.S(Mode.POINTY));

        assertThrows(InvalidParameterException.class, () -> coord.E(Mode.FLAT));
        assertThrows(InvalidParameterException.class, () -> coord.O(Mode.FLAT));
    }
}