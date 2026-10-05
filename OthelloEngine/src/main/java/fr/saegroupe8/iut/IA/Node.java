package fr.saegroupe8.iut.IA;

import fr.saegroupe8.iut.model.actions.Action;
import fr.saegroupe8.iut.model.state.IState;

/*Attributs :
    - state (IState)  : la situation de jeu a ce noeud (plateau + tour + lignes)
    - parent (Node)   : le noeud qui a mene a celui-ci (null pour la racine)
    - action (Action) : le coup qui a ete joue pour arriver ici (null pour la racine)*/
public class Node {

    private final IState state;
    private final Node parent;
    private final Action action;

    public Node(IState state, Node parent, Action action) {
        this.state = state;
        this.parent = parent;
        this.action = action;
    }
    /* L'IA explore un arbre. Chaque branche est un coup possible.
    Node permet de stocker chaque situation et de remonter l'arbre
    pour retrouver quel coup a mene a la meilleure situation.*/

    public IState getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public Action getAction() {
        return action;
    }
}