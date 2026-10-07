package but.info.sae2_12.controller;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.tokens.Ring;
import coordinates.Coordinate;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Set;

// Mode retrait de ligne
public class RemoveLineMode extends InteractionMode {


    private Set<Coordinate> ligneLocked = null;


    private Team equipeLigne = null;

    // La ligne en cours de survol
    private Set<Coordinate> ligneHovered = null;

    @Override
    public void handleClick(MouseEvent event, HexSquare hexSquare) {
        var coord = hexSquare.getCoordinate();
        var model = getMainController().getModel();

        if (ligneLocked == null) {
            // cherche une ligne contenant cette case
            Set<Coordinate> ligne = getLigneContenant(coord);
            if (ligne == null) return;


            ligneLocked = ligne;
            equipeLigne = model.getTokenAt(coord).getTeam();

            // Colore la ligne en jaune pour montrer qu'elle est sélectionnée
            for (Coordinate c : ligneLocked) {
                getMainController().getHexSquare(c).setHighlightColor(Color.YELLOW);
            }

        } else {
            // 2nde: vérifie que c'est un anneau de la bonne équipe
            var jeton = model.getTokenAt(coord);


            if (!(jeton instanceof Ring r) || r.getTeam() != equipeLigne) return;

            // Retire la ligne et l'anneux
            model.removeLine(ligneLocked, coord);

            // réinitialise
            for (Coordinate c : ligneLocked) {
                getMainController().getHexSquare(c).resetColor();
            }
            ligneLocked  = null;
            equipeLigne  = null;
        }
    }

    @Override
    public void entered(MouseEvent event, HexSquare hexSquare) {

        if (ligneLocked == null) {
            ligneHovered = getLigneContenant(hexSquare.getCoordinate());
            if (ligneHovered != null) {
                for (Coordinate c : ligneHovered) {
                    getMainController().getHexSquare(c).setHighlightColor(Color.ORANGE);
                }
            }
        }
        hexSquare.highlight();
    }

    @Override
    public void exited(MouseEvent event, HexSquare hexSquare) {
        if (ligneLocked == null && ligneHovered != null) {
            for (Coordinate c : ligneHovered) {
                getMainController().getHexSquare(c).resetColor();
            }
            ligneHovered = null;
        }
        hexSquare.resetColor();
    }


    private Set<Coordinate> getLigneContenant(Coordinate coord) {
        for (Set<Coordinate> ligne : getMainController().getModel().getPawnsLines()) {
            if (ligne.contains(coord)) return ligne;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Suppression de ligne";
    }
}
