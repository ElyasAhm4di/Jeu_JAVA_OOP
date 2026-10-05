package fr.saegroupe8.iut.Coordinate;

public enum Direction {
	NO, N, NE, E, SE, S, SO, O;

	public Direction opposite() {
		switch (this) {
		case NO:
			return SE;
		case N:
			return S;
		case NE:
			return SO;
		case E:
			return O;
		case SE:
			return NO;
		case S:
			return N;
		case SO:
			return NE;
		case O:
			return E;
		default:
			return null;
		}
	}
}

/*
public record Point(int x, int y) {

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}*/