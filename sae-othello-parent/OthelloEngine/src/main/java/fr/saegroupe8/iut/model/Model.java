package fr.saegroupe8.iut.model;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.tokens.Token;
import fr.saegroupe8.iut.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Model {

	private IState currentState;

	public Model(IState state) {
		this.currentState = state;
	}

	public void setCurrentState(IState state) {
		this.currentState = state;
	}

	public IState getCurrentState() {
		return currentState;
	}

	public Set<Coordinate> movesFrom(Coordinate from) {
		return currentState.availableMoves(from);
	}

	public void moveRing(Coordinate from, Coordinate to) throws Exception {
		currentState = currentState.move(new Move(from, to));
	}

	public void removeLine(Set<Coordinate> line, Coordinate ring) {
		currentState = currentState.removeLine(new RemoveLine(line, ring));
	}

	public List<Set<Coordinate>> getPawnsLines() {
		return currentState.lines();
	}

	public Map<Coordinate, Token> getBoard() {
		return currentState.board();
	}

	public Token getTokenAt(Coordinate c) {
		return currentState.board().get(c);
	}

	public boolean isInField(Coordinate c) {
		return currentState.board().containsKey(c);
	}

	public List<Coordinate> getRings(Team team) {
		return currentState.rings().get(team);
	}

	public List<Coordinate> getPawn(Team team) {
		List<Coordinate> teamPawns = new ArrayList<>();

		for (Map.Entry<Coordinate, Token> entry : currentState.board().entrySet()) {
			Token token = entry.getValue();

			if (token instanceof fr.saegroupe8.iut.model.tokens.Pawn && token.getTeam() == team) {
				teamPawns.add(entry.getKey());
			}
		}
		return teamPawns;
	}

	public Team getTurn() {
		return currentState.turn();
	}
}