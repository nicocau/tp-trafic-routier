package modele;

import java.util.ArrayList;
import java.util.List;

public class Arc {
    /**
     * noeuds secondaire entre les deux noeuds principaux
     */
    Noeud[] petitsNoeuds;
    /**
     * liste des voitures sur noeud
     */
    List<Voiture> cars = new ArrayList<>();
    /**
     * nom de l'arc
     */
    String name;
    /**
     * noeud de depart
     */
    private Noeud start;
    /**
     * noeud d'arrivee
     */
    private Noeud end;


    /**
     * cree un arc entre 2 noeuds
     *
     * @param start noeud de depart
     * @param end   noeud d'arrivee
     */
    public Arc(Noeud start, Noeud end) {
        super();
        this.start = start;
        this.end = end;
        start.addArcSortant(this);
        end.addArcEntrant(this);
        this.name = "" + start.id + "-" + end.id;
    }

    public Noeud getEnd() {
        return end;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        return sb.toString();
    }
}
