package fr.saegroupe8.iut.Coordinate;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.saegroupe8.iut.Exception.DifferentAxisException;

public class CoordinateDoubled extends Coordinate{

    private int x;
    private int y;

    public CoordinateDoubled(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Point to2DCoordinate() {
        return new Point(this.x, this.y);
    }

    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException {

        if (!(to instanceof CoordinateDoubled)) {
            throw new IllegalArgumentException("Le type de coordonnée n'est pas compatible.");
        }
        CoordinateDoubled target = (CoordinateDoubled) to;

        int dy = target.getY() - this.y;
        int dx = target.getX() - this.x;

        if (dy != 0 && Math.abs(dx) != Math.abs(dy)) {
            throw new DifferentAxisException("Les coordonnées doublées ne sont pas sur le même axe.");
        }

        int distance = (dy == 0) ? Math.abs(dx) / 2 : Math.abs(dy);

        List<Coordinate> path = new ArrayList<>();
        if (distance <= 1) return path;

        int stepY = dy / distance;
        int stepX = dx / distance;

        for (int i = 1; i < distance; i++) {
            path.add(new CoordinateDoubled(this.y + i * stepY, this.x + i * stepX));
        }

        return path;

    }

    @Override
    public Coordinate NO(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            return new CoordinateDoubled(this.y - 1, this.x - 1);
        } else {
            return new CoordinateDoubled(this.y - 1, this.x - 2);
        }
    }

    @Override
    public Coordinate NE(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            return new CoordinateDoubled(this.y - 1, this.x + 1);
        } else {
            return new CoordinateDoubled(this.y - 1, this.x + 2);
        }
    }

    @Override
    public Coordinate E(Mode mode) throws InvalidParameterException {
        if (mode == Mode.FLAT) {
            throw new InvalidParameterException("La direction Est n'existe pas en mode FLAT.");
        }
        return new CoordinateDoubled(this.y, this.x + 2);
    }

    @Override
    public Coordinate O(Mode mode) throws InvalidParameterException {
        if (mode == Mode.FLAT) {
            throw new InvalidParameterException("La direction Ouest n'existe pas en mode FLAT.");
        }
        return new CoordinateDoubled(this.y, this.x - 2);
    }

    @Override
    public Coordinate N(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            throw new InvalidParameterException("La direction Nord n'existe pas en mode POINTY.");
        }
        return new CoordinateDoubled(this.y - 2, this.x);
    }

    @Override
    public Coordinate S(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            throw new InvalidParameterException("La direction Sud n'existe pas en mode POINTY.");
        }
        return new CoordinateDoubled(this.y + 2, this.x);
    }

    @Override
    public Coordinate SO(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            return new CoordinateDoubled(this.y + 1, this.x - 1);
        } else {
            return new CoordinateDoubled(this.y + 1, this.x - 2);
        }
    }

    @Override
    public Coordinate SE(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            return new CoordinateDoubled(this.y + 1, this.x + 1);
        } else {
            return new CoordinateDoubled(this.y + 1, this.x + 2);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateDoubled that = (CoordinateDoubled) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
