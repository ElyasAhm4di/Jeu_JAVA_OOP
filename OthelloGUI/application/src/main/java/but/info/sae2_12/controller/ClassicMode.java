package but.info.sae2_12.controller;

import but.info.sae2_12.model.tokens.Ring;
import coordinates.Coordinate;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

// Mode classique
public class ClassicMode extends InteractionMode {


    private Coordinate anneauSelectionne = null;

    // cases l'anneau peut aller
    private Set<Coordinate> casesAccessibles = new HashSet<>();

    @Override
    public void handleClick(MouseEvent event, HexSquare hexSquare) {
        var coord = hexSquare.getCoordinate();
        var model = getMainController().getModel();
        var state = model.getCurrentState();

        if (anneauSelectionne == null) {
            //    sélectionner un anneau

            var jeton = state.board().get(coord);


            if (!(jeton instanceof Ring r) || r.getTeam() != state.turn()) return;

            // On mémorise l'anneau et on calcule les cases accessibles
            anneauSelectionne = coord;
            casesAccessibles  = state.availableMoves(coord);


            for (Coordinate c : casesAccessibles) {
                getMainController().getHexSquare(c).showAccessible();
            }

        } else {
            // déplacer l'anneau

            // Efface les indicateurs dans tous les cas
            for (Coordinate c : casesAccessibles) {
                getMainController().getHexSquare(c).hideAccessible();
            }

            if (casesAccessibles.contains(coord)) {
                // La case est accessible , on deplace
                model.moveRing(anneauSelectionne, coord);
            }



            anneauSelectionne = null;
            casesAccessibles  = new HashSet<>();
        }
    }

    @Override
    public void entered(MouseEvent event, HexSquare hexSquare) {
        hexSquare.highlight();
    }

    @Override
    public void exited(MouseEvent event, HexSquare hexSquare) {
        hexSquare.resetColor();
    }

    @Override
    public String toString() {
        return "Jeu";
    }
}
