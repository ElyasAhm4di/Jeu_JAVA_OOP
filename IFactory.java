package fr.saegroupe8.iut.model.factory;


import fr.saegroupe8.iut.model.state.IState;

public interface IFactory {
	IState testState();
	IState stateForBlackLineTest();
	IState stateForWhiteLineTest();
	IState emptyState();
	IState doubleLineStateTest();
}
