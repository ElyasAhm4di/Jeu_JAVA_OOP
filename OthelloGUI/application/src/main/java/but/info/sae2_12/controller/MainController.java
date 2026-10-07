package but.info.sae2_12.controller;

import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import coordinates.Coordinate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class MainController {

	private SimpleBooleanProperty showCoordinatesBool = new SimpleBooleanProperty();
	private SimpleDoubleProperty strokeWidth = new SimpleDoubleProperty(1.0); // epaisseur des bordures (1 a 5)
	
	// 3 colors so I need either a tab or 3 different attributs, I think 3 attributs would be easier to read
	private SimpleObjectProperty<Color> hexagonColor1 = new SimpleObjectProperty<Color>(Color.GRAY);
	private SimpleObjectProperty<Color> hexagonColor2 = new SimpleObjectProperty<Color>(Color.DARKGRAY);
	private SimpleObjectProperty<Color> hexagonColor3 = new SimpleObjectProperty<Color>(Color.LIGHTGRAY);
	
	private SimpleBooleanProperty winnerBool = new SimpleBooleanProperty();
	private SimpleBooleanProperty editionBool = new SimpleBooleanProperty();
	private SimpleStringProperty coordinateDisplayMode = new SimpleStringProperty("Cubique"); // mode d'affichage par defaut
	private Model model;
	
	// interactionMode used inside the binding
	private SimpleObjectProperty<InteractionMode> interactionMode = new SimpleObjectProperty<InteractionMode>();
	
	// linking Robin centralController
	private @FXML CentralController centralController;
	// linking Elyas gameController
	private @FXML GameController gameController;
	// linking Enzo menuController
	private @FXML MenuController menuController;
	// linking Enzo bottomController
	private @FXML BottomController bottomController;
	// linking Enzo aiController
	private @FXML AIController aIController;
	
	// controller
	public MainController() {
		// not sure on what the controller needs to init
		// probably a model, but I don't really know what
		
		// generate a random game by default
		this.model = new Model(new FactoryCube().randomGame());
		
		interactionMode.bind(Bindings.<InteractionMode>createObjectBinding(() -> {
			InteractionMode mode;
			if (editionBool.getValue()) mode = new Edition();
			else if (model.getCurrentState() != null && !model.getPawnsLines().isEmpty()) mode = new RemoveLineMode();
			else mode = new ClassicMode();
			mode.setMainController(this); // chaque nouveau mode a besoin du mainController
			return mode;
		}, editionBool, model.currentStateProperty()));
		
		// winning pop-up
		winnerBool.addListener((event, oldValue, newValue) -> {
			
			// generate the pop up here!
			if(newValue) {
				Alert winnerAlert = new Alert(AlertType.INFORMATION);
				winnerAlert.setTitle("Victoire !");
				winnerAlert.setHeaderText("Nous avons un gagnant !");
				winnerAlert.setContentText("Bravo a toi " + model.getCurrentState().winner() + " !");
				winnerAlert.show();
			}
		});
		
		model.currentStateProperty().addListener((event, oldValue, newValue) -> {
			if(oldValue != newValue && newValue.winner() != null) {
				winnerBool.set(true);
			}
		});
	}
	
	public void initialize() {
		centralController.setMainController(this);
		gameController.setMainController(this);
		menuController.setMainController(this);
		aIController.setMainController(this);
		bottomController.setMainController(this);
	}
	
	public Team getSelectedTeam() {
	    return gameController.getSelectedTeam();
	}

	public Class<?> getSelectedTokenClass() {
	    return gameController.getSelectedTokenClass();
	}

	public HexSquare getHexSquare(Coordinate c) {
	    return centralController.getPlateauMap().get(c);
	}
	
	// auto-generated getters/setters by eclipse:
	
	public SimpleBooleanProperty getShowCoordinatesBool() {
		return showCoordinatesBool;
	}

	public SimpleDoubleProperty getStrokeWidth() {
		return strokeWidth;
	}

	public SimpleObjectProperty<InteractionMode> getInteractionMode() {
		return interactionMode;
	}

	public void setShowCoordinatesBool(SimpleBooleanProperty showCoordinatesBool) {
		this.showCoordinatesBool = showCoordinatesBool;
	}

	public SimpleObjectProperty<Color> getHexagonColor1() {
		return hexagonColor1;
	}

	public void setHexagonColor1(SimpleObjectProperty<Color> hexagonColor1) {
		this.hexagonColor1 = hexagonColor1;
	}

	public SimpleObjectProperty<Color> getHexagonColor2() {
		return hexagonColor2;
	}

	public void setHexagonColor2(SimpleObjectProperty<Color> hexagonColor2) {
		this.hexagonColor2 = hexagonColor2;
	}

	public SimpleObjectProperty<Color> getHexagonColor3() {
		return hexagonColor3;
	}

	public void setHexagonColor3(SimpleObjectProperty<Color> hexagonColor3) {
		this.hexagonColor3 = hexagonColor3;
	}

	public SimpleBooleanProperty getWinnerBool() {
		return winnerBool;
	}

	public void setWinnerBool(SimpleBooleanProperty winnerBool) {
		this.winnerBool = winnerBool;
	}

	public SimpleBooleanProperty getEditionBool() {
		return editionBool;
	}

	public void setEditionBool(SimpleBooleanProperty editionBool) {
		this.editionBool = editionBool;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.winnerBool.set(false);
		this.model.setCurrentState(model.getCurrentState()); // on garde le meme modele pour ne pas casser les listeners
	}

	public SimpleStringProperty getCoordinateDisplayMode() {
		return coordinateDisplayMode;
	}

	public void setCoordinateDisplayMode(SimpleStringProperty coordinateDisplayMode) {
		this.coordinateDisplayMode = coordinateDisplayMode;
	}

	public SimpleObjectProperty<InteractionMode> interactionModeProperty() {
		return interactionMode;
	}
}

