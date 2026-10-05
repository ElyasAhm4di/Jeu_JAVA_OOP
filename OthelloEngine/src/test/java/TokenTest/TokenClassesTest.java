package TokenTest;


import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class TokenClassesTest {



    @Test
    public void testTeamOther() {
        assertEquals(Team.WHITE, Team.BLACK.other(), "L'opposé de BLACK doit être WHITE.");
        assertEquals(Team.BLACK, Team.WHITE.other(), "L'opposé de WHITE doit être BLACK.");
    }

    @Test
    public void testTeamGetColor() {
        assertEquals(Color.BLACK, Team.BLACK.getColor(), "La couleur de l'équipe BLACK doit être le noir AWT.");
        assertEquals(Color.WHITE, Team.WHITE.getColor(), "La couleur de l'équipe WHITE doit être le blanc AWT.");
    }



    @Test
    public void testPawnCreationAndTeam() {
        Pawn whitePawn = new Pawn(Team.WHITE);
        Pawn blackPawn = new Pawn(Team.BLACK);

        assertEquals(Team.WHITE, whitePawn.getTeam(), "Le pion blanc doit appartenir à l'équipe WHITE.");
        assertEquals(Team.BLACK, blackPawn.getTeam(), "Le pion noir doit appartenir à l'équipe BLACK.");
    }

    @Test
    public void testPawnFlip() {
        Pawn pawn = new Pawn(Team.WHITE);

        pawn.flip();
        assertEquals(Team.BLACK, pawn.getTeam(), "Après un flip(), un pion WHITE doit devenir BLACK.");

        pawn.flip();
        assertEquals(Team.WHITE, pawn.getTeam(), "Après un second flip(), le pion doit redevenir WHITE.");
    }



    @Test
    public void testRingCreationAndTeam() {
        Ring whiteRing = new Ring(Team.WHITE);
        Ring blackRing = new Ring(Team.BLACK);

        assertEquals(Team.WHITE, whiteRing.getTeam(), "L'anneau blanc doit appartenir à l'équipe WHITE.");
        assertEquals(Team.BLACK, blackRing.getTeam(), "L'anneau noir doit appartenir à l'équipe BLACK.");
    }
}