package but.info.sae2_12.controller;

import but.info.sae2_12.AI.MiniMax;
import but.info.sae2_12.AI.MinimaxAI;
import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Action;
import but.info.sae2_12.model.state.IState;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class AIController implements Initializable {
    @FXML private TextField ringRemovedField;
    @FXML private TextField winScoreField;
    @FXML private TextField nearLineField;
    @FXML private TextField pawnField;
    @FXML private TextField mobilityField;
    @FXML private ProgressBar evaluationBar;
    private MainController mainController;
    private final MiniMax miniMax = new MiniMax();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Bindings.bindBidirectional(ringRemovedField.textProperty(), MiniMax.ringRemovedWeightProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(winScoreField.textProperty(), MiniMax.winScoreProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(nearLineField.textProperty(), MiniMax.nearLineWeightProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(pawnField.textProperty(), MiniMax.pawnWeightProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(mobilityField.textProperty(), MiniMax.mobilityWeightProperty(), new NumberStringConverter());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        Model model = mainController.getModel();

        model.currentStateProperty().addListener((obs, ancien, nouveau) -> updateEvaluation(nouveau));


        updateEvaluation(model.getCurrentState());
    }

    private void updateEvaluation(IState state) {
        if (state == null) {
            return;
        }
        double score = miniMax.evaluate(state, Team.BLACK);

        double max = MiniMax.winScoreProperty().get();
        double min = -max;

        double progress = (score - min) / (max - min);
        progress = Math.max(0, Math.min(1, progress));

        evaluationBar.setProgress(progress);
    }

    @FXML
    private void showBestMove() {
        IState state = mainController.getModel().getCurrentState();
        if (state == null) {
            return;
        }

        MinimaxAI ai = new MinimaxAI(state.turn(), 2);
        Action best = ai.chooseMove(state);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Meilleur coup");
        alert.setHeaderText("Coup recommandé par l'IA");
        if (best == null) {
            alert.setContentText("Aucun coup possible.");
        } else {
            alert.setContentText(best.toString());
        }
        alert.show();
    }
}
