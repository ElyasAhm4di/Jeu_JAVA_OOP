package fr.saegroupe8.iut.Coordinate;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.saegroupe8.iut.Exception.DifferentAxisException;

public class CoordinateCube extends Coordinate {

    private int q;
    private int r;
    private int s;

    public CoordinateCube(int q, int r, int s) {

        if(q + r + s != 0)
        {
            throw new IllegalArgumentException("Les coordonnées remplis ne sont pas bonne");
        }

        super();
        this.q = q;
        this.r = r;
        this.s = s;
    }

    public int getQ()
    {
        return this.q;
    }

    public int getS()
    {
        return this.s;
    }

    public int getR()
    {
        return this.r;
    }

    @Override
    public Point to2DCoordinate() {
        int x = this.q;
        int y = this.r;
        return new Point(x, y);
    }

    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException {

        //TODO Faire la fonction between

        if(!(to instanceof CoordinateCube))
        {
            throw new IllegalArgumentException("Le type de coordonée n'est pas le bon.");
        }
        CoordinateCube target = (CoordinateCube) to;

        if (this.q != target.getQ() && this.r != target.getR() && this.s != target.getS()) {
            throw new DifferentAxisException("Les coordonnées cubiques ne sont pas sur le même axe.");
        }

        int distance = Math.max(Math.abs(this.q - target.getQ()),
                        Math.max(Math.abs(this.r - target.getR()),
                        Math.abs(this.s - target.getS())));

        List<Coordinate> path = new ArrayList<>();
        if (distance <= 1) return path;

        int stepQ = (target.getQ() - this.q) / distance;
        int stepR = (target.getR() - this.r) / distance;
        int stepS = (target.getS() - this.s) / distance;

        for (int i = 1; i < distance; i++) {
            path.add(new CoordinateCube(this.q + i * stepQ, this.r + i * stepR, this.s + i * stepS));
        }

        return path;
    }

    @Override
    public Coordinate NO(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            return new CoordinateCube(this.q, this.r - 1, this.s + 1);
        } else {
            return new CoordinateCube(this.q - 1, this.r, this.s + 1);
        }
    }

    @Override
    public Coordinate NE(Mode mode) throws InvalidParameterException {
        return new CoordinateCube(this.q + 1, this.r - 1, this.s);
    }

    @Override
    public Coordinate E(Mode mode) throws InvalidParameterException {
        if (mode == Mode.FLAT) {
            throw new InvalidParameterException("La direction Est n'existe pas en mode FLAT.");
        }
        return new CoordinateCube(this.q + 1, this.r, this.s - 1);
    }

    @Override
    public Coordinate O(Mode mode) throws InvalidParameterException {
        if (mode == Mode.FLAT) {
            throw new InvalidParameterException("La direction Ouest n'existe pas en mode FLAT.");
        }
        return new CoordinateCube(this.q - 1, this.r, this.s + 1);
    }

    @Override
    public Coordinate N(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            throw new InvalidParameterException("La direction Nord n'existe pas en mode POINTY.");
        }
        return new CoordinateCube(this.q, this.r - 1, this.s + 1);
    }

    @Override
    public Coordinate S(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            throw new InvalidParameterException("La direction Sud n'existe pas en mode POINTY.");
        }
        return new CoordinateCube(this.q, this.r + 1, this.s - 1);
    }

    @Override
    public Coordinate SO(Mode mode) throws InvalidParameterException {
        return new CoordinateCube(this.q - 1, this.r + 1, this.s);
    }

    @Override
    public Coordinate SE(Mode mode) throws InvalidParameterException {
        if (mode == Mode.POINTY) {
            return new CoordinateCube(this.q, this.r + 1, this.s - 1);
        } else {
            return new CoordinateCube(this.q + 1, this.r, this.s - 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateCube that = (CoordinateCube) o;
        return this.q == that.q && this.r == that.r && this.s == that.s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, s);
    }
}
