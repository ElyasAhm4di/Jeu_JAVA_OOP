package fr.saegroupe8.iut.IA;


import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.state.IState;

public interface AI {
    Action chooseMove(IState state);
}