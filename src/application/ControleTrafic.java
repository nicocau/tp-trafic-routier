package application;

import modele.Noeud;
import modele.ReseauRoutier;
import modele.Voiture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * classe gérant le trafic routier
 * créer le réseau et les voitures
 */
public class ControleTrafic {
    /**
     * les voitures
     */
    List<Voiture> voitures;
    /**
     * le hasard
     */
    Random hasard;

    /**
     * creation du reseau et des premieres voitures
     */
    public ControleTrafic() {
        ReseauRoutier.creerReseau();
        voitures = new ArrayList<>();
        hasard = new Random();
    }

    /**
     * ajoute 1 voitures se deplacant soit  du sud au nord, soit de l'ouest vers l'est
     */
    public Voiture addVoiture(boolean ligne) {
        Voiture v = null;
        int nb = (int) ReseauRoutier.getDimMax();
        Noeud debut = null;
        Noeud fin = null;
        int random = this.hasard.nextInt((nb / 4) - 1);
        random = (random + 1) * 4;
        if (ligne) {
            debut = ReseauRoutier.getNoeud(0, random);
            fin = ReseauRoutier.getNoeud(nb, random);
        } else {
            debut = ReseauRoutier.getNoeud(random, nb);
            fin = ReseauRoutier.getNoeud(random, 0);
        }
        v = new Voiture(Voiture.conte_id_voiture++, debut, fin);
        voitures.add(v);
        return v;
    }

    /**
     * retirer une voiture du circuit
     *
     * @param v la voiture a supprimer
     * @return la nouvelle liste de voitures
     */
    public List<Voiture> removeCar(Voiture v) {

        v.getNoeudCourant().removeCar(v);
        voitures.remove(v);
        return voitures;
    }


    /**
     * demande à chaque voiture de calculer leurs noeuds suivants
     */
    public void calculerPointsSuivants() {
        for (Voiture v : voitures) {
            //si la voiture pense etre dans un bouchon, lui demander de verifier
            v.verifBouchon();
            if (!v.isArrivee() && !v.isPause()) {
                v.calculerProchainNoeud();
            }
        }
    }

    /**
     * demande à chaque voiture de bouger (se retirer du noeud courant)
     */
    public void bougerVoitures() {
        for (Voiture v : voitures) {
            if (!v.isPause()) {
                v.allerAuProchainNoeud();
            }
        }
    }

    /**
     * met en pause la voiture no i
     */
    public void pauseVoiture(int i) {
        Voiture v = this.getVoitureById(i);
        v.setPause(!v.isPause());
    }

    /**
     * @return la liste des voitures
     */
    public List<Voiture> getVoitures() {
        return voitures;
    }

    /**
     * @return le générateur aléatoir
     */
    public Random getHasard() {
        return hasard;
    }

    /**
     * @param id
     * @return roturne la voiture avec le bon id
     */
    private Voiture getVoitureById(int id) {
        final Voiture[] res = new Voiture[1];
        this.voitures.forEach(voiture -> {
            if (voiture.getId() == id) {
                res[0] = voiture;
            }
        });
        return res[0];
    }
}
