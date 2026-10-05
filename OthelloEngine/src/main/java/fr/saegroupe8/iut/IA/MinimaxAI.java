package fr.saegroupe8.iut.IA;

import fr.saegroupe8.iut.Coordinate.Coordinate;
import fr.saegroupe8.iut.model.Team;
import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.actions.Move;
import fr.saegroupe8.iut.model.actions.RemoveLine;
import fr.saegroupe8.iut.model.state.IState;
import fr.saegroupe8.iut.model.tokens.Pawn;
import fr.saegroupe8.iut.model.tokens.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*Role :
    L'IA du jeu. On lui donne une situation, elle renvoie le meilleur coup a jouer.
    Elle utilise l'algorithme Minimax avec elagage Alpha-Beta.

Attributs :
    - profondeurMax (int) : combien de coups a l'avance l'IA reflechit
        Exemple : profondeurMax = 2 → l'IA simule son coup + la reponse de l'adversaire
        Plus c'est grand, plus l'IA est forte mais plus c'est lent
    - equipeIA (Team) : l'equipe de l'IA (BLACK ou WHITE)*/

public class MinimaxAI implements AI {

    private final int profondeurMax;
    private final Team equipeIA;

    public MinimaxAI(int profondeurMax, Team equipeIA) {
        this.profondeurMax = profondeurMax;
        this.equipeIA = equipeIA;
    }

   /* chooseMove(etat)
    → Point d'entree. On donne la situation actuelle.
    → L'IA genere tous les coups possibles.
    → Pour chaque coup, elle appelle minimax() pour avoir un score.
    → Elle renvoie le coup avec le meilleur score.*/
    @Override
    public Action chooseMove(IState etat) {
        Node racine = new Node(etat, null, null);
        List<Node> enfants = genererEnfants(racine);

        Action meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        for (Node enfant : enfants) {
            int score = minimax(enfant, profondeurMax - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = enfant.getAction();
            }
        }

        return meilleurCoup;
    }

    /*minimax(noeud, profondeur, alpha, beta, maximise)
    → Fonction recursive qui explore l'arbre des coups.
    → Si maximise = true (tour de l'IA) : on cherche le score le plus HAUT
    → Si maximise = false (tour adversaire) : on cherche le score le plus BAS
    → On alterne a chaque niveau de profondeur
    → Alpha-Beta : si on sait qu'une branche est moins bonne qu'une autre,
      on arrete de l'explorer (beta <= alpha → break)*/
   
    private int minimax(Node noeud, int profondeur, int alpha, int beta, boolean maximise) {
        IState etat = noeud.getState();

        if (profondeur == 0 || etat.winner() != null) {
            return evaluer(etat);
        }

        List<Node> enfants = genererEnfants(noeud);

        if (enfants.isEmpty()) {
            return evaluer(etat);
        }

        if (maximise) {
            int meilleur = Integer.MIN_VALUE;
            for (Node enfant : enfants) {
                int score = minimax(enfant, profondeur - 1, alpha, beta, false);
                meilleur = Math.max(meilleur, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) break;
            }
            return meilleur;
        } else {
            int pire = Integer.MAX_VALUE;
            for (Node enfant : enfants) {
                int score = minimax(enfant, profondeur - 1, alpha, beta, true);
                pire = Math.min(pire, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) break;
            }
            return pire;
        }
    }
/*genererEnfants(parent)
    → Genere tous les coups possibles depuis une situation.
    → Deux cas :
        a) Il y a des lignes de 5 → les coups sont des RemoveLine
        b) Pas de ligne → les coups sont des Move (deplacer un anneau)
    → Pour chaque coup, on cree le nouvel etat et on le met dans un Node.
*/
    private List<Node> genererEnfants(Node parent) {
        List<Node> enfants = new ArrayList<>();
        IState etat = parent.getState();
        Team equipeCourante = etat.turn();

        if (!etat.lines().isEmpty()) {
            List<Set<Coordinate>> lignesEquipe = new ArrayList<>();
            for (Set<Coordinate> ligne : etat.lines()) {
                Coordinate premier = ligne.iterator().next();
                Token t = etat.board().get(premier);
                if (t != null && t.getTeam() == equipeCourante) {
                    lignesEquipe.add(ligne);
                }
            }

            if (lignesEquipe.isEmpty()) {
                lignesEquipe = etat.lines();
            }

            List<Coordinate> anneaux = etat.rings().get(equipeCourante);

            for (Set<Coordinate> ligne : lignesEquipe) {
                for (Coordinate anneau : anneaux) {
                    try {
                        RemoveLine action = new RemoveLine(ligne, anneau);
                        IState nouvelEtat = etat.removeLine(action);
                        enfants.add(new Node(nouvelEtat, parent, action));
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            List<Coordinate> anneaux = etat.rings().get(equipeCourante);

            for (Coordinate anneau : anneaux) {
                Set<Coordinate> casesAccessibles = etat.availableMoves(anneau);
                for (Coordinate destination : casesAccessibles) {
                    try {
                        Move action = new Move(anneau, destination);
                        IState nouvelEtat = etat.move(action);
                        enfants.add(new Node(nouvelEtat, parent, action));
                    } catch (Exception e) {
                    }
                }
            }
        }

        return enfants;
    }
/*evaluer(etat)
    → Donne une note a une situation. C'est le "cerveau" de l'IA.
    → Score positif = bien pour l'IA, negatif = bien pour l'adversaire
    → Criteres :
        Victoire         → +100 000
        Defaite          → -100 000
        Anneau retire    → +1 000 par anneau d'avance
        Mobilite         → +0.5 par case accessible en plus que l'adversaire
        Nombre de pions  → +2 par pion d'avance*/
  
    private int evaluer(IState etat) {
        int score = 0;
        Team ennemi = equipeIA.other();

        Team gagnant = etat.winner();
        if (gagnant == equipeIA) return 100000;
        if (gagnant == ennemi) return -100000;

        int anneauxIARetires = 5 - etat.rings().get(equipeIA).size();
        int anneauxEnnemiRetires = 5 - etat.rings().get(ennemi).size();
        score += (anneauxIARetires - anneauxEnnemiRetires) * 1000;

        int mobiliteIA = 0;
        for (Coordinate anneau : etat.rings().get(equipeIA)) {
            mobiliteIA += etat.availableMoves(anneau).size();
        }
        int mobiliteEnnemi = 0;
        for (Coordinate anneau : etat.rings().get(ennemi)) {
            mobiliteEnnemi += etat.availableMoves(anneau).size();
        }
        score += (mobiliteIA - mobiliteEnnemi) / 2;

        int pionsIA = 0;
        int pionsEnnemi = 0;
        for (Map.Entry<Coordinate, Token> entree : etat.board().entrySet()) {
            if (entree.getValue() instanceof Pawn) {
                if (entree.getValue().getTeam() == equipeIA) {
                    pionsIA++;
                } else {
                    pionsEnnemi++;
                }
            }
        }
        score += (pionsIA - pionsEnnemi) * 2;

        return score;
    }
}
/* MinimaxAI ia = new MinimaxAI(2, Team.BLACK);
    Action coup = ia.chooseMove(etatActuel);
    // coup est soit un Move soit un RemoveLine*/