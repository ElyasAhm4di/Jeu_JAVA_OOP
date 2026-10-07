package but.info.sae2_12.controller;

import javafx.scene.input.MouseEvent;

public abstract class InteractionMode {
	
	// "Cette derniere a un acces au controleur principal"
	private MainController mainController;

	// "et trois fonctions abstraites"
	public abstract void handleClick(MouseEvent mouseEvent, HexSquare hexSquare);
	public abstract void entered(MouseEvent mouseEvent, HexSquare hexSquare);
	public abstract void exited(MouseEvent mouseEvent, HexSquare hexSquare);
	
	// getters + setters for the controller
	public void setMainController(MainController controller) { this.mainController = controller;}
	public MainController getMainController() { return this.mainController;}
}
