package fr.saegroupe8.iut.model.tokens;

import fr.saegroupe8.iut.model.Team;

public class Pawn extends Token {

    public Pawn(Team team) {
        super(team);
    }

    public void flip() {
        changeTeam(getTeam().other());
    }
}