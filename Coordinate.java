package fr.saegroupe8.iut.Coordinate;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import fr.saegroupe8.iut.Exception.DifferentAxisException;


public abstract class Coordinate {

    public abstract Point to2DCoordinate();

    public Coordinate toDir(Mode mode, Direction direction) {
        return switch (direction) {
            case NO -> this.NO(mode);
            case NE -> this.NE(mode);
            case E -> this.E(mode);
            case O -> this.O(mode);
            case N -> this.N(mode);
            case S -> this.S(mode);
            case SO -> this.SO(mode);
            case SE -> this.SE(mode);
        };
    }

    public List<Coordinate> getNeighbors(Mode mode) {
        List<Coordinate> neighbors = new ArrayList<>();

        if (mode == Mode.POINTY) {
            neighbors.add(toDir(mode, Direction.NO));
            neighbors.add(toDir(mode, Direction.NE));
            neighbors.add(toDir(mode, Direction.O));
            neighbors.add(toDir(mode, Direction.E));
            neighbors.add(toDir(mode, Direction.SO));
            neighbors.add(toDir(mode, Direction.SE));
        } else {
            neighbors.add(toDir(mode, Direction.N));
            neighbors.add(toDir(mode, Direction.S));
            neighbors.add(toDir(mode, Direction.NO));
            neighbors.add(toDir(mode, Direction.NE));
            neighbors.add(toDir(mode, Direction.SO));
            neighbors.add(toDir(mode, Direction.SE));
        }

        return neighbors;
    }

    public abstract List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException;

    public abstract Coordinate NO(Mode mode) throws InvalidParameterException;
    public abstract Coordinate NE(Mode mode) throws InvalidParameterException;
    public abstract Coordinate E(Mode mode) throws InvalidParameterException;
    public abstract Coordinate O(Mode mode) throws InvalidParameterException;
    public abstract Coordinate N(Mode mode) throws InvalidParameterException;
    public abstract Coordinate S(Mode mode) throws InvalidParameterException;
    public abstract Coordinate SO(Mode mode) throws InvalidParameterException;
    public abstract Coordinate SE(Mode mode) throws InvalidParameterException;
}