package fr.saegroupe8.iut.model.tokens;

import fr.saegroupe8.iut.model.Team;

public abstract class Token {

    private Team team;

    public Token(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team newTeam) {
        this.team = newTeam;
    }
}