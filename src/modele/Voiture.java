package modele;

import java.util.ArrayList;
import java.util.List;

public class Voiture
{
   /**identifiant de la voiture*/
   private int id;
   /**coordonnée x de la voiture*/
   double x;
   /**coordonnée y de la voiture*/
   double y;
   /**liste des noeuds du trajet prevu*/
   private List<Noeud> trajet;
   /**liste des restant a parcourir*/
   private List<Noeud> routeRestante;
   /**noeud de depart*/
   private Noeud origine;
   /**noeud de destination finale*/
   private Noeud destination;
   /**dernier noeud suivi*/
   //TODO : nico : noeux actuel et ajoute le prochain noeux (car
   private Noeud noeudSuivi;
   /**pause pendant le parcours*/
   private boolean pause;
   /**indique si la voiture est arrivee*/
   private boolean arrivee;
   /**accident pendant le parcours*/
   private boolean accident;
   /**indique si la voiture est dans un bouchon*/
   private boolean bouchon;
   /**tps avant remorquage*/
   final static int REMORQUAGE = 10;
   /**temps de panne*/
   private int tpsPanne = 0;
   
   
   Voiture(){}
   /**construit une voiture
    * @param id identifiant de la voiture
    * */
   Voiture(int id){
      this.id = id;
      trajet = new ArrayList<>();
      routeRestante = new ArrayList<>();
      }
   /**construit une voiture
    * @param id identifiant de la voiture
    * @param origine noeud de depart
    * @param destination noeud d'arrivee
    * */
   public Voiture(int no, Noeud origine, Noeud destination) {
      this(no);
      this.origine = origine;
      this.noeudSuivi = this.origine;
      x = origine.x;
      y = origine.y;
      this.destination = destination;
      calculerRoute();
      for(Noeud n:trajet)routeRestante.add(n);
   }
   /**construit une voiture
    * @param id identifiant de la voiture
    * @param xo yo coordonnees du noeud d'origine
    * @param xd yd coordonnees du noeud de destinatin
    * */
   public Voiture(int no, int xo, int yo, int xd, int yd) {
      this(no);
      this.origine = ReseauRoutier.getNoeud(xo, yo);
      this.noeudSuivi = this.origine;
      x = origine.x;
      y = origine.y;
      this.destination = ReseauRoutier.getNoeud(xd, yd);
      calculerRoute();
      for(Noeud n:trajet)routeRestante.add(n);
   }
   
   /**calcul la route entre origine et destination 
    * (suppose que ces points soient aient une meme abscisse ou ordonnee)*/
   public void calculerRoute()
   {
      boolean enLigne = (origine.getY()==destination.getY());
      Noeud suivant = origine;
      while (!suivant.equals(destination))
      {
         List<Arc> arcSortants = suivant.getArcSortants();
         if(!arcSortants.isEmpty())
         {
            for(Arc arc:arcSortants)
            {
               suivant = arc.getEnd();
               if(suivant==null) System.err.println("pb de suivant : " + arc);
               if (enLigne && suivant.getY()==destination.getY()) break;
               if(!enLigne && suivant.getX()==destination.getX()) break;
            }
            trajet.add(suivant);
         }
         else
         {
            System.err.println("pb avec ce noeud " + suivant);
            break;
         }
      }
   }

   //TODO : Nico : commencerAvencerVerPointSuivant Vérifie les statut(vérifie si arriver, si pas dans les bouchon
    //TODO : nico : finirAvencerVerPointSuivant déplacer la voiture en fonction du statut

   /** retourne le prochain noeud de la route (depile routeRestante)
    */
   //TODO : nico : Remplacer par commencerAvencerVerPointSuivant
   public void calculerProchainNoeud()
   {
      Noeud suivant;
      //si la voiture n'a pas ete mise en pause
      // si elle n'est pas impliquée dans un accident
      //s'il lui reste du chemin à faire
      if(!pause && !accident && !routeRestante.isEmpty())
      {
         //verifier que la route devant est libre
         verifBouchon();
         //si la voiture n'est pas dans un bouchon
         if(!bouchon)
         {
             //TODO : nico :
            noeudSuivi = routeRestante.remove(0);
            if(noeudSuivi.equals(destination)) arrivee = true;
         }
      }
   }
   
  /**verifie si la voiture est dans un bouchon*/
  public void verifBouchon()
   {
     //s'il reste de la route à faire
      if(!routeRestante.isEmpty())
      {
         Noeud suivant = routeRestante.get(0);
         if(suivant.getNbCars()==0) setBouchon(false);
         else setBouchon(true);
      }
   }
   
   /**se detacher du noeud precedent et se declarer au noeud suivant*/
   //TODO : nico : finirAvencerVerPointSuivant
   public void allerAuProchainNoeud()
   {
      //si la voiture n'a pas ete mise en pause
      // si elle n'est pas impliquée dans un accident
      //s'il lui reste du chemin à faire      
       if(!pause || !bouchon|| !accident) {
          //aller au prochain noeud calculet
           noeudCourant.removeCar(this);
           noeudSuivi.addCar(this);
           noeudCourant = noeudSuivi;
       }
   }
      
   /**retourne une chaine de caracteres contenant l'identfiant de la voiture et son chemin prevu*/
   public String toString()
   {
      StringBuilder sb = new StringBuilder("voiture ").append(id);
      sb.append(". chemin = ");
      for(Noeud n:trajet) sb.append(n).append("-");
      return sb.toString();      
   }

   public int getX() { return x; }
   public int getY() { return y; }
   public boolean isPause() {return pause; }
   public boolean isArrivee() { return arrivee; }
   public void setPause(boolean pause) 
   { 
      this.pause = pause; 
      if(pause && noeudSuivi!=null) noeudSuivi.addCar(this);
      if(!pause && noeudSuivi!=null) noeudSuivi.removeCar(this);
   }
   public Noeud getNoeudSuivi() { return noeudSuivi; }
   public boolean isAccident() { return accident; }
   public void setAccident(boolean accident)
   {
      this.accident = accident;
      if (accident)this.pause = true;
   }
   public int getId() { return id; }
   public boolean isBouchon() { return bouchon; }
   public void setBouchon(boolean bouchon) { this.bouchon = bouchon; }
   
   public void incrementeTpsPanne() {tpsPanne++; }
   public boolean isARemorquer() {
      //TODO: le vehicule est a remorquer si le tps de panne depasse le seuil
      return false;}  
}
