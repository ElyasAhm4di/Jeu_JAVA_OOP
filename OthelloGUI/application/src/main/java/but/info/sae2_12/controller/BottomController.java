package but.info.sae2_12.controller;

import but.info.sae2_12.controller.MainController;
import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.state.IState;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BottomController {

    @FXML private Label turnLabel;
    @FXML private Label modeLabel;
    @FXML private Label blackCountLabel;
    @FXML private Label whiteCountLabel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        Model model = mainController.getModel();

        turnLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    IState state = model.getCurrentState();
                    if (state == null) {
                        return "—";
                    }
                    if(state.turn() == but.info.sae2_12.model.Team.BLACK){
                        return "Noir";
                    } else {
                        return "Blanc";
                    }
                },
                model.currentStateProperty()));

            modeLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    InteractionMode mode = mainController.interactionModeProperty().get();
                    if(mode == null){
                        return "-";
                    } else {
                        return mode.toString();
                    }
                },
                mainController.interactionModeProperty()));

            blackCountLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    if (model.getCurrentState() == null){
                        return "";
                    }

                    return "Noir : " + model.getRings(Team.BLACK).size() + " anneaux, " + model.getPawn(Team.BLACK).size() + " pions";
                    }, model.currentStateProperty()));

            whiteCountLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    if (model.getCurrentState() == null) return "";
                    return "Blanc : " + model.getRings(Team.WHITE).size() + " anneaux, " + model.getPawn(Team.WHITE).size() + " pions";
                },
                model.currentStateProperty()));
    }
}