package but.info.sae2_12.controller;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import coordinates.Coordinate;
import coordinates.Point;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class HexSquare extends Polygon {

    public static final double LARGEUR = 60; // Dimensions d'une cellule, valeurs de test
    public static final double HAUTEUR = 70;
    
    public static final double RAYON_TOKEN = LARGEUR / 4;     // rayon des pions et anneaux sur la cellule
    public static final double RAYON_ACCESSIBLE = LARGEUR / 6; // rayon pour indiquer qu'un deplacement est possible

    private final Coordinate coordinate;
    private final Label label;
    private final double cx;
    private final double cy;
    private Node form;                                     // pion, anneau ou indicateur de deplacement (null si vide)
    private Pane pane;                                     // reference au pane central, posee via setPane()
    private SimpleObjectProperty<Color> baseColorProperty; // couleur de base pour rebinder apres survol

    public HexSquare(Coordinate coordinate) {
        this.coordinate = coordinate;

        Point center = coordinate.to2DCoordinate();
        this.cx = center.x() * LARGEUR / 2.0;        // les colonnes sont doublees dans to2DCoordinate, donc on divise par 2
        this.cy = center.y() * HAUTEUR * 3.0 / 4.0;  // espacement vertical entre deux lignes d'hexagones pointy

        getPoints().addAll(
            cx - LARGEUR / 2, cy - HAUTEUR / 4,  // haut-gauche
            cx,               cy - HAUTEUR / 2,  // haut
            cx + LARGEUR / 2, cy - HAUTEUR / 4,  // haut-droite
            cx + LARGEUR / 2, cy + HAUTEUR / 4,  // bas-droite
            cx,               cy + HAUTEUR / 2,  // bas
            cx - LARGEUR / 2, cy + HAUTEUR / 4   // bas-gauche
        );

        setStroke(javafx.scene.paint.Color.BLACK); // contour noir par defaut

        label = new Label(coordinate.toString());
        label.setLayoutX(cx - LARGEUR / 2);                      // coin gauche = bord gauche de l'hexagone
        label.setMinWidth(LARGEUR);                               // le label fait exactement la largeur de l'hexagone
        label.setAlignment(javafx.geometry.Pos.CENTER);           // le texte est centre dans ce label
        label.setLayoutY(cy - label.getFont().getSize() / 2);    // centre verticalement
    }

    // Dessine le bon element visuel selon le token (Pawn, Ring, ou null pour vider)
    public void updateForm(Token token, Pane pane) {
        if (token == null) {
            removeForm(pane);
            return;
        }

        Color teamColor = (token.getTeam() == Team.BLACK) ? Color.BLACK : Color.WHITE;
        Circle circle = new Circle(cx, cy, RAYON_TOKEN);

        if (token instanceof Pawn) {
            circle.setFill(teamColor);         // disque plein = pion
            circle.setStroke(Color.GRAY);
            circle.setStrokeWidth(1);
        } else if (token instanceof Ring) {
            circle.setFill(Color.TRANSPARENT); // centre vide = anneau
            circle.setStroke(teamColor);
            circle.setStrokeWidth(4);
        }

        setForm(circle, pane);
    }

    // Appele depuis CentralController pour stocker la reference au pane
    public void setPane(Pane pane) { this.pane = pane; }

    // Stocke la propriete couleur pour pouvoir rebinder apres un survol
    public void setBaseColorProperty(SimpleObjectProperty<Color> colorProperty) {
        this.baseColorProperty = colorProperty;
    }

    // Branche les evenements souris vers l'InteractionMode courant
    public void setMainController(MainController mc) {
        setOnMouseClicked(e -> mc.getInteractionMode().getValue().handleClick(e, this));
        setOnMouseEntered(e -> mc.getInteractionMode().getValue().entered(e, this));
        setOnMouseExited(e -> mc.getInteractionMode().getValue().exited(e, this));
    }

    // Survol par defaut : couleur jaune clair
    public void highlight() { setHighlightColor(Color.LIGHTYELLOW); }

    // Survol avec une couleur specifique
    public void setHighlightColor(Color color) {
        fillProperty().unbind(); // delier avant de changer la couleur
        setFill(color);
    }

    // Remet la couleur de base en rebindant
    public void resetColor() {
        if (baseColorProperty != null) fillProperty().bind(baseColorProperty);
    }

    // Versions sans pane en parametre (utilisent this.pane stocke via setPane)
    public void showAccessible() { showAccessible(this.pane); }
    public void hideAccessible() { removeForm(this.pane); }

    // Affiche un petit indicateur pour signaler que la case est accessible
    public void showAccessible(Pane pane) {
        Circle indicator = new Circle(cx, cy, RAYON_ACCESSIBLE);
        indicator.setFill(Color.LIGHTGRAY);
        indicator.setOpacity(0.5);
        setForm(indicator, pane);
    }

    // Remplace la forme actuelle par la nouvelle, ou supprime si null
    public void setForm(Node newForm, Pane pane) {
        if (form != null) pane.getChildren().remove(form);
        form = newForm;
        if (form != null) pane.getChildren().add(form);
    }

    public void removeForm(Pane pane) {
        setForm(null, pane);
    }

    public Node getForm() {
        return form;
    }

    public double getCx() { return cx; }
    public double getCy() { return cy; }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Label getCoordLabel() {
        return label;
    }

    // Active ou desactive la visibilite du label
    public void setLabelVisible(boolean visible) {
        label.setVisible(visible);
    }

}
