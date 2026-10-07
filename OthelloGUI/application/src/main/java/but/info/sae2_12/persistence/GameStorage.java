package but.info.sae2_12.persistence;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.factory.FactoryDoubled;
import but.info.sae2_12.model.factory.IFactory;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import coordinates.Coordinate;
import coordinates.CoordinateCube;
import coordinates.CoordinateDoubled;
import coordinates.Point;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameStorage {
    private static final String MAGIC = "SAE212";

    public static void save(IState state, File file) throws IOException {
        Map<Coordinate, Token> board = state.board();

        try(DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
            output.writeBytes(MAGIC);

            if (state.turn() == Team.BLACK) {
                output.writeChar('B');
            } else {
                output.writeChar('W');
            }

            if (state.getLines().isEmpty()) {
                output.writeChar('M');
            } else {
                output.writeChar('L');
            }

            char system = 'C';
            for (Coordinate c : board.keySet()) {
                system = (c instanceof CoordinateCube) ? 'C' : 'D';
                break;
            }
            output.writeChar(system);

            int count = 0;
            for (Token t : board.values()) {
                if (t != null) {
                    count++;
                }
            }
            output.writeInt(count);

            for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
                Coordinate c = entry.getKey();
                Token t = entry.getValue();
                if (t == null) {
                    continue;
                }

                if (c instanceof CoordinateCube cube) {
                    output.writeInt(cube.getQ());
                    output.writeInt(cube.getR());
                    output.writeInt(cube.getS());
                } else {
                    Point p = c.to2DCoordinate(); // Point(x, y)
                    output.writeInt(p.x());
                    output.writeInt(p.y());
                }

                if (t instanceof Ring){
                    output.writeChar('R');
                } else {
                    output.writeChar('P');
                }

                if (t.getTeam() == Team.BLACK) {
                    output.writeChar('B');
                } else {
                    output.writeChar('W');
                }
            }


        }
    }

    public static IState load(File file) throws IOException {

        try(DataInputStream input = new DataInputStream(new FileInputStream(file))){

            byte[] magic = new byte[6];
            input.readFully(magic);
            if(!new String(magic).equals(MAGIC)){
                throw new IOException("Format de fichier invalide");
            }

            Team turn;
            if(input.readChar() == 'B'){
                turn = Team.BLACK;
            } else {
                turn = Team.WHITE;
            }

            input.readChar();

            char system = input.readChar();

            int count = input.readInt();

            Map<Coordinate, Token> board;
            if(system == 'C'){
                IFactory factory = new FactoryCube();
                board = new HashMap<>(factory.emptyState().board());
            } else {
                IFactory factory = new FactoryDoubled();
                board = new HashMap<>(factory.emptyState().board());
            }

            for (int i = 0; i < count; i++) {
                Coordinate c;
                if (system == 'C') {
                    int q = input.readInt();
                    int r = input.readInt();
                    int s = input.readInt();
                    c = new CoordinateCube(q, r, s);
                } else {
                    int x = input.readInt();
                    int y = input.readInt();
                    c = new CoordinateDoubled(y, x);
                }

                char type = input.readChar();

                Team team;
                if(input.readChar() == 'B'){
                    team = Team.BLACK;
                } else {
                    team = Team.WHITE;
                }

                Token token;
                if(type == 'R'){
                    token = new Ring(team);
                } else {
                    token = new Pawn(team);
                }

                board.put(c, token);
            }

            List<Set<Coordinate>> lines = IState.getPawnsLines(board);
            return new State(board, turn, lines);
        }
    }
}
