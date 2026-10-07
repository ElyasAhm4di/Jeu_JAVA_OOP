package but.info.sae2_12.controller;

import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.persistence.GameStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class MenuController {
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void charger() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger une partie");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Partie YINSH", "*.yns"));

        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }

        try {
            IState loaded = GameStorage.load(file);
            mainController.getModel().setCurrentState(loaded);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Format de fichier invalide");
            alert.show();
        }
    }

    @FXML
    private void sauvegarder() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la partie");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Partie YINSH", "*.yns"));

        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return;
        }

        try {
            GameStorage.save(mainController.getModel().getCurrentState(), file);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de sauvegarder la partie");
            alert.show();
        }
    }

    @FXML
    private void aPropos() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("YINSH — SAÉ 2.1 & 2.2");
        alert.setContentText("Groupe sae2_12\nMembres : Robin, Raphaël, Elyas, Enzo\nBUT Informatique - Université de Caen");
        alert.show();
    }
}
