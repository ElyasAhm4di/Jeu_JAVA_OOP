package but.info.sae2_12.controller;

import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.factory.IFactory;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;



public class GameController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // boutons pour lancer une partie
    @FXML
    private Button btnRandom;
    @FXML
    private Button btnTest;
    @FXML
    private Button btnBlackLine;
    @FXML
    private Button btnWhiteLine;

    // mode edition
    @FXML
    private CheckBox editModeCheckBox;
    @FXML
    private VBox editSubZone;
    @FXML
    private ToggleButton btnRing;
    @FXML
    private ToggleButton btnPawn;
    @FXML
    private ToggleButton btnBlack;
    @FXML
    private ToggleButton btnWhite;


    private ToggleGroup tokenTypeGroup = new ToggleGroup();
    private ToggleGroup teamGroup = new ToggleGroup();


    // colorpickers
    @FXML
    private ColorPicker colorPicker1;
    @FXML
    private ColorPicker colorPicker2;
    @FXML
    private ColorPicker colorPicker3;

    // coordonnees
    @FXML
    private CheckBox showCoordsCheckBox;
    @FXML
    private ListView<String> coordModeListView;

    // slider epaisseur
    @FXML
    private Slider borderSlider;
    @FXML
    private Label borderValueLabel;
    private SimpleIntegerProperty borderThickness = new SimpleIntegerProperty(1);


    @FXML
    public void initialize() {
        // Les boutons dans leurs groups
        btnRing.setToggleGroup(tokenTypeGroup);
        btnPawn.setToggleGroup(tokenTypeGroup);
        btnRing.setSelected(true);

        //
        btnBlack.setToggleGroup(teamGroup);
        btnWhite.setToggleGroup(teamGroup);
        btnBlack.setSelected(true);


        // Coleurs par defaut
        colorPicker1.setValue(Color.GRAY);
        colorPicker2.setValue(Color.DARKGREY);
        colorPicker3.setValue(Color.LIGHTGRAY);


        coordModeListView.setItems(FXCollections.observableArrayList("Cubique", "2D"));
        coordModeListView.getSelectionModel().selectFirst();


        borderSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int val = newVal.intValue();
            borderValueLabel.setText(String.valueOf(val));
            borderThickness.set(val);
        });

    }


    // boutons

    @FXML
    private void onRandomGame() {
        // partie aléatoire
        IFactory factory = new FactoryCube();
        IState state = factory.randomGame();
        mainController.setModel(new Model(state));
    }

    @FXML
    private void onTestState() {
        IFactory factory = new FactoryCube();
        IState state = factory.testState();
        mainController.setModel(new Model(state));
    }

    @FXML
    private void onBlackLineState() {
        IFactory factory = new FactoryCube();
        IState state = factory.stateForBlackLineTest();
        mainController.setModel(new Model(state));
    }


    @FXML
    private void onWhiteLineState() {
        IFactory factory = new FactoryCube();
        IState state = factory.stateForWhiteLineTest();
        mainController.setModel(new Model(state));
    }


    //mode edition

    @FXML
    private void onEditModeToggle() {
        boolean active = editModeCheckBox.isSelected();
        editSubZone.setDisable(!active);
        mainController.getEditionBool().set(active);
    }

    // anneux ou pion
    public Class<?> getSelectedTokenClass() {

        if (btnRing.isSelected()) {
            return Ring.class;
        }

        return Pawn.class;
    }

    //blanc ou noir

    public Team getSelectedTeam() {
        if (btnBlack.isSelected()) {
            return Team.BLACK;
        }
        return Team.WHITE;
    }


    //ColorPicker
    @FXML
    private void onColor1Changed() {
        Color c = colorPicker1.getValue();
        mainController.getHexagonColor1().set(c);
    }

    @FXML
    private void onColor2Changed() {
        Color c = colorPicker2.getValue();
        mainController.getHexagonColor2().set(c);

    }

    @FXML
    private void onColor3Changed() {
        Color c = colorPicker3.getValue();
        mainController.getHexagonColor3().set(c);
    }


    //cordonnes
    @FXML
    private void onShowCoordsToggle() {
        boolean show = showCoordsCheckBox.isSelected();
        mainController.getShowCoordinatesBool().set(show);
    }


    //Curbique ou 2D
    public String getSelectedCoordMode() {
        return coordModeListView.getSelectionModel().getSelectedItem();
    }

    //epaisseur

    public SimpleIntegerProperty borderThicknessProperty() {
        return borderThickness;
    }















    }





