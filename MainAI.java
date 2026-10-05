package fr.saegroupe8.iut.IA;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.Coordinate.CoordinateCube;
import fr.saegroupe8.iut.model.Model;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import fr.saegroupe8.iut.model.factory.FactoryCube;
import fr.saegroupe8.iut.model.state.IState;

import java.util.Scanner;
import java.util.Set;


// Le joueur a les blancs, l'IA a les noirs.
public class MainAI {

    public static void main(String[] args) {

        // on part d'un terrain de test deja rempli
        FactoryCube factory = new FactoryCube();
        Model model = new Model(factory.testState());

        // l'IA joue les noirs et regarde 3 coups en avance
        MinimaxAI ia = new MinimaxAI(3, Team.BLACK);

        Scanner scanner = new Scanner(System.in);
        IState etat = model.getCurrentState();

        // la partie continue tant que personne n'a gagne
        while (etat.winner() == null) {

            if (etat.turn() == Team.BLACK) {

                Action coup = ia.chooseMove(etat);
                try {
                    // le coup est soit un deplacement, soit un retrait de ligne
                    if (coup instanceof Move m) {
                        model.moveRing(m.getFrom(), m.getTo());
                    } else if (coup instanceof RemoveLine r) {
                        model.removeLine(r.getLine(), r.getRing());
                    }
                } catch (Exception e) {
                    System.out.println("Erreur IA : " + e.getMessage());
                }

            } else {

                System.out.println("Votre tour");

                try {
                    // si une ligne est presente, on doit d'abord retirer un anneau
                    if (!etat.lines().isEmpty()) {
                        System.out.println("Retirez un anneau (q r s) :");
                        Coordinate anneau = new CoordinateCube(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
                        Set<Coordinate> ligne = etat.lines().get(0);
                        model.removeLine(ligne, anneau);
                    } else {
                        // sinon on deplace un anneau : case de depart puis case d'arriveee
                        System.out.println("départ (q r s) :");
                        Coordinate from = new CoordinateCube(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
                        System.out.println("arrivée (q r s) :");
                        Coordinate to = new CoordinateCube(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
                        model.moveRing(from, to);
                    }
                } catch (Exception e) {
                    // coup refuse : on redemande sans changer de tour
                    System.out.println("coup invalide, réessayez.");
                    continue;
                }
            }


            etat = model.getCurrentState();
        }

        // la boucle est finie, donc quelqu'un a gagne : on l'affiche
        System.out.println(etat.winner() == Team.WHITE ? "Vous avez gagné !" : "L'IA a gagné !");
        scanner.close();
    }
}