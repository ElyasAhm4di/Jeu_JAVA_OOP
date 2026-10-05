package fr.saegroupe8.iut.model.actions;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import java.util.HashSet;
import java.util.Set;

public class RemoveLine extends Action {

    private Set<Coordinate> line;
    private Coordinate ring;

    public RemoveLine(Set<Coordinate> line, Coordinate ring) {
        this.line = new HashSet<>(line);
        this.ring = ring;
    }

    public Set<Coordinate> getLine() { return line; }
    public Coordinate getRing() { return ring; }
    public void setLine(Set<Coordinate> line) { this.line = line; }
    public void setRing(Coordinate ring) { this.ring = ring; }
    public void addToLine(Coordinate coord) { this.line.add(coord); }
}