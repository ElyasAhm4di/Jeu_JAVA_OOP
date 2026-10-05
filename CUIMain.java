package fr.saegroupe8.iut;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateDoubled;
import fr.saegroupe8.iut.Coordinate.Direction;
import fr.saegroupe8.iut.Coordinate.Mode;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.factory.FactoryDoubled;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.state.State;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Ring;
import fr.saegroupe8.iut.model.tokens.Token;

import java.util.*;

public class CUIMain {

    public void displayBoard(IState state, Coordinate selection) {

        Map<Coordinate, Token> board = state.board();


        for (int y = 0; y < 11; y++) {
            for (int x = 0; x <= 18; x++) {

                Coordinate coord = new CoordinateDoubled(y, x);

                // Si la case n'existe pas on va afficher un espace à la place
                if (!board.containsKey(coord)) System.out.print("  ");

                else {
                    Token t = board.get(coord);

                    if (selection != null && coord.equals(selection))
                        System.out.print("* ");
                    else if (t == null)
                        System.out.print("_ ");
                    else if (t instanceof Pawn)
                        System.out.print((t.getTeam() == Team.BLACK ? "x " : ". "));
                    else if (t instanceof Ring)
                        System.out.print((t.getTeam() == Team.WHITE ? "o " : "O "));
                }
            }
            System.out.println();
        }

    }

    private IState autoInit() {
        IState state = new FactoryDoubled().emptyState();

        List<Coordinate> allowedCoords = new ArrayList<>(state.board().keySet());
        Collections.shuffle(allowedCoords);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Coordinate coord = allowedCoords.get(i);
            state = state.toggleToken(coord, (i < 5 ? Team.BLACK : Team.WHITE), Ring.class);
        }

        return state;
    }

    private IState manualInit() {
        IState state = new FactoryDoubled().emptyState();
        Map<Coordinate, Token> board = state.board();

        int dir;
        Team turn = Team.BLACK;

        Scanner scanner = new Scanner(System.in);

        Coordinate selection = new CoordinateDoubled(6,6);
        Coordinate futureCoord;

        for (int i = 0; i < 10; i++) {
            boolean loop = true;

            while (loop) {
                displayBoard(state, selection);

                System.out.println("Appuyez sur une touche (Z,Q,S,D) pour vous déplacer");
                System.out.println("Appuyez sur X pour placer votre anneau");

                String input = scanner.nextLine().toUpperCase();

                switch (input) {
                    case "Z":
                        futureCoord = selection.toDir(Mode.POINTY, Direction.NO);
                        if (board.containsKey(futureCoord))
                            selection = futureCoord;
                        continue;
                    case "S":
                        futureCoord = selection.toDir(Mode.POINTY, Direction.SO);
                        if (board.containsKey(futureCoord))
                            selection = futureCoord;
                        continue;
                    case "Q":
                        futureCoord = selection.toDir(Mode.POINTY, Direction.O);
                        if (board.containsKey(futureCoord))
                            selection = futureCoord;
                        continue;
                    case "D":
                        futureCoord = selection.toDir(Mode.POINTY, Direction.E);
                        if (board.containsKey(futureCoord))
                            selection = futureCoord;
                        continue;
                    case "X":
                        state = state.toggleToken(selection, turn, Ring.class);
                        turn = turn.other();
                        loop = false;
                        break;
                }
            }
        }

        return state;
    }

    public IState initGame(boolean automatic) {
        if (automatic) return autoInit();
        return manualInit();
    }

    public IState initGame() {
        return autoInit();
    }

    public static void main(String[] args) {
        CUIMain cui = new CUIMain();
        IState state = cui.initGame(false);

        cui.displayBoard(state, null);
    }

}