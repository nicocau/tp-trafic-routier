package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modele.Noeud;
import modele.ReseauRoutier;
import modele.Voiture;
/**
 * classe gérant le trafic routier
 * créer le réseau et les voitures
 * */
public class ControleTrafic
{
   /**les voitures*/
   List<Voiture> voitures;
   /**le hasard*/
   Random hasard;
   /**creation du reseau et des premieres voitures*/
   public ControleTrafic()
   {
      ReseauRoutier.creerReseau();
      voitures = new ArrayList<>();
      hasard= new Random();
      //on debute avec 2 voitures, l'une en délacement en est-ouest, l'autre en sud-nord
//      addVoiture(true);
//      addVoiture(false);
   }
   
   /**ajoute 1 voitures se deplacant soit  du sud au nord, soit de l'ouest vers l'est*/
   public Voiture addVoiture(boolean ligne)
   {
      Voiture v = null;
       int nb = (int) ReseauRoutier.getDimMax();
       Noeud debut = null;
       Noeud fin = null;
       int random = this.hasard.nextInt(nb-1);
       random +=1;
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

   /**retirer une voiture du circuit
   @param v la voiture a supprimer
   @return la nouvelle liste de voitures*/
   public List<Voiture> removeCar(Voiture v)
   {

      v.getNoeudCourant().removeCar(v);
      voitures.remove(v);
      return voitures;
   }

   
   /**demande à chaque voiture de calculer leurs noeuds suivants*/
   public void calculerPointsSuivants() {
      for(Voiture v:voitures)
      {
      //si la voiture pense etre dans un bouchon, lui demander de verifier
         if(v.isBouchon())v.verifBouchon();
         if(!v.isArrivee() && !v.isPause())
         {
            v.calculerProchainNoeud();
         }
      }
   }
   
   /**demande à chaque voiture de bouger (se retirer du noeud courant)*/
   public void bougerVoitures()
   {
      for(Voiture v:voitures)
      {
         if(!v.isPause())
         {
            v.allerAuProchainNoeud();
         }
      }
   }
   
   /**met en pause la voiture no i*/
   public void pauseVoiture(int i)
   {
      Voiture v = this.getVoitureById(i);
      v.setPause(!v.isPause());
   }
   
   /**@return  la liste des voitures*/
   public List<Voiture> getVoitures()
   {
      return voitures;
   }

   public Random getHasard() {
      return hasard;
   }

   private Voiture getVoitureById(int id){
      final Voiture[] res = new Voiture[1];
      this.voitures.forEach(voiture -> {
         if (voiture.getId() == id){
            res[0] = voiture;
         }
      });
      return res[0];
   }
}
