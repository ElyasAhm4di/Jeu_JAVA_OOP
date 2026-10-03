package fr.saegroupe8.iut.Coordinate;

public record Point(int x, int y) {

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

}