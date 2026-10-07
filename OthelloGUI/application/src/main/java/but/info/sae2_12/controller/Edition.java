package but.info.sae2_12.controller;

import but.info.sae2_12.model.Team;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

// Mode édition
public class Edition extends InteractionMode {

    @Override
    public void handleClick(MouseEvent event, HexSquare hexSquare) {
        var coord = hexSquare.getCoordinate();

        if (event.getButton() == MouseButton.SECONDARY) {
            // Clic droit
            getMainController().getModel().removeToken(coord);

        } else if (event.getButton() == MouseButton.PRIMARY) {
            // Clic gauche
            Team team      = getMainController().getSelectedTeam();        // noir ou blanc
            Class<?> token = getMainController().getSelectedTokenClass();  // Ring.class ou Pawn.class


            getMainController().getModel().toggleToken(coord, token, team);
        }
    }

    @Override
    public void entered(MouseEvent event, HexSquare hexSquare) {
        hexSquare.highlight(); // survol : colore la case
    }

    @Override
    public void exited(MouseEvent event, HexSquare hexSquare) {
        hexSquare.resetColor(); // quitte
    }

    @Override
    public String toString() {
        return "Édition";
    }
}
