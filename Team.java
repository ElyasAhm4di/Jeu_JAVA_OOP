package fr.saegroupe8.iut.model;

import java.awt.Color;

public enum Team {
	BLACK(Color.BLACK), WHITE(Color.WHITE);

	private final Color color;

	Team(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public Team other() {
		switch (this) {
		case BLACK:
			return WHITE;
		case WHITE:
			return BLACK;
		default:
			return null;
		}
	}
}