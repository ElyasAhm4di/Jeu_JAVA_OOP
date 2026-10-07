package but.info.sae2_12.controller;

import coordinates.Coordinate;
import coordinates.CoordinateCube;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class CentralController {

    @FXML
    private Pane centralPane;

    private final HashMap<Coordinate, HexSquare> plateauMap = new HashMap<>();

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        buildPlateau();
        // decale le plateau pour qu'il soit entierement visible (les cases du haut ont des Y negatifs sinon)
        centralPane.setTranslateX(50);
        centralPane.setTranslateY(45);

        // quand l'etat du jeu change, on redessine tous les tokens
        mainController.getModel().currentStateProperty().addListener((obs, oldVal, newVal) -> refreshForms());
        // quand le mode d'affichage change, on met a jour tous les labels
        mainController.getCoordinateDisplayMode().addListener((obs, oldVal, newVal) -> updateLabels(newVal));
    }

    // redessine tous les tokens sur le plateau
    private void refreshForms() {
        for (HexSquare hs : plateauMap.values()) {
            hs.updateForm(mainController.getModel().getTokenAt(hs.getCoordinate()), centralPane);
        }
    }

    // met a jour le texte de tous les labels selon le mode (Cubique ou 2D)
    private void updateLabels(String mode) {
        for (HexSquare hs : plateauMap.values()) {
            if (mode.equals("2D")) {
                hs.getCoordLabel().setText(hs.getCoordinate().to2DCoordinate().toString());
            } else {
                hs.getCoordLabel().setText(hs.getCoordinate().toString());
            }
        }
    }

    private void buildPlateau() {
        for (Coordinate coordinate : mainController.getModel().getBoard().keySet()) {
            HexSquare hs = new HexSquare(coordinate);
            bindColor(hs, coordinate); // lie la couleur + stocke la propriete pour resetColor
            hs.setPane(centralPane); // stocke le pane pour showAccessible/hideAccessible
            hs.setMainController(mainController); // branche les evenements souris
            hs.strokeWidthProperty().bind(mainController.getStrokeWidth()); // lie l'epaisseur au Slider
            hs.getCoordLabel().visibleProperty().bind(mainController.getShowCoordinatesBool()); // lie la visibilite des coordonnees a la CheckBox
            plateauMap.put(coordinate, hs);
            centralPane.getChildren().addAll(hs, hs.getCoordLabel()); // ajoute l'hexagone et son label au Pane
            hs.updateForm(mainController.getModel().getTokenAt(coordinate), centralPane); // dessine le token initial
        }
    }

    // bind la couleur a l'hexagone
    private void bindColor(HexSquare hs, Coordinate coordinate) {
        int colorIndex = 0;
        if (coordinate instanceof CoordinateCube) {
            CoordinateCube cc = (CoordinateCube) coordinate;
            colorIndex = ((cc.getQ() - cc.getR()) % 3 + 3) % 3; // faut avoir fait prepa pour capter ca
        }
        if (colorIndex == 0) { hs.fillProperty().bind(mainController.getHexagonColor1()); hs.setBaseColorProperty(mainController.getHexagonColor1()); }
        else if (colorIndex == 1) { hs.fillProperty().bind(mainController.getHexagonColor2()); hs.setBaseColorProperty(mainController.getHexagonColor2()); }
        else { hs.fillProperty().bind(mainController.getHexagonColor3()); hs.setBaseColorProperty(mainController.getHexagonColor3()); }
    }

    // permet aux autres controleurs de retrouver un HexSquare depuis une coordonnee
    public HashMap<Coordinate, HexSquare> getPlateauMap() {
        return plateauMap;
    }
}
