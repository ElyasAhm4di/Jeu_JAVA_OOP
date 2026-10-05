package fr.saegroupe8.iut.model.actions;

import fr.saegroupe8.iut.Coordinate.Coordinate;

public class Move extends Action {

    private Coordinate from;
    private Coordinate to;

    public Move(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    public Coordinate getFrom() { return from; }
    public Coordinate getTo() { return to; }
    public void setFrom(Coordinate from) { this.from = from; }
    public void setTo(Coordinate to) { this.to = to; }
}