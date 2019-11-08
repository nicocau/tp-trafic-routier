package modele;

import gui.DessinVoiture;

import java.util.ArrayList;
import java.util.List;

public class Voiture
{
   /**identifiant de la voiture*/
   private int id;
   /**coordonnée x de la voiture*/
   int x;
   /**coordonnée y de la voiture*/
   int y;
   /**liste des noeuds du trajet prevu*/
   private List<Noeud> trajet;
   /**liste des restant a parcourir*/
   private List<Noeud> routeRestante;
   /**noeud de depart*/
   private Noeud origine;
   /**noeud de destination finale*/
   private Noeud destination;
   /**noeud noeudCourant*/
   private Noeud noeudCourant;
   /**prochain noeud*/
   private Noeud prochainNoeud;
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
   /**temps de panne*/
   public static int conte_id_voiture = 0;
    /** Représentation */
   private DessinVoiture dessinVoiture;
   

   /**construit une voiture
    * @param id identifiant de la voiture
    * */
   Voiture(int id){
      this.id = id;
      trajet = new ArrayList<>();
      routeRestante = new ArrayList<>();
      }
   /**construit une voiture
    * @param no identifiant de la voiture
    * @param origine noeud de depart
    * @param destination noeud d'arrivee
    * */
   public Voiture(int no, Noeud origine, Noeud destination) {
      this(no);
      this.origine = origine;
      this.noeudCourant = this.origine;
      x = origine.x;
      y = origine.y;
      this.destination = destination;
      calculerRoute();
      for(Noeud n:trajet)routeRestante.add(n);
       this.prochainNoeud = this.noeudCourant;
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


   /** retourne le prochain noeud de la route (depile routeRestante)
    */
   public void calculerProchainNoeud()
   {
      //si la voiture n'a pas ete mise en pause
      // si elle n'est pas impliquée dans un accident
      //s'il lui reste du chemin à faire
       if (!pause && !accident && !arrivee) {
           //verifier que la route devant est libre
           verifBouchon();
           //si la voiture n'est pas dans un bouchon
           if (!bouchon) {
               if (noeudCourant.equals(destination)) arrivee = true;
               if (prochainNoeud.equals(destination)) arrivee = true;
           }
       }
   }
   
  /**verifie si la voiture est dans un bouchon*/
  public void verifBouchon()
   {
     //s'il reste de la route à faire
      if(!routeRestante.isEmpty())
      {
         if(this.prochainNoeud.getNbCars()==0) setBouchon(false);
         else setBouchon(true);
      }
   }
   
   /**se detacher du noeud precedent et se declarer au noeud suivant*/
   public void allerAuProchainNoeud()
   {
      //si la voiture n'a pas ete mise en pause
      // si elle n'est pas impliquée dans un accident
      //s'il lui reste du chemin à faire      
       if ((!pause && !bouchon && !accident) && !routeRestante.isEmpty()) {
           System.out.println("La voiture n°" + this.id + " est sur le noeud (" + this.noeudCourant.x + "," + this.noeudCourant.y + ") est va sur le noeud (" + this.prochainNoeud.x + "," + this.prochainNoeud.y + ")");
          //aller au prochain noeud calculet
           if (noeudCourant != prochainNoeud) {
               this.noeudCourant.removeCar(this);
           }
           this.prochainNoeud.addCar(this);
           this.noeudCourant = this.prochainNoeud;
           this.prochainNoeud = this.routeRestante.get(0);
           this.routeRestante.remove(0);
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
   }

    public Noeud getProchainNoeud() {
        return prochainNoeud;
    }

    public Noeud getNoeudCourant() { return noeudCourant; }

    public boolean isAccident() { return accident; }
   public void setAccident(boolean accident)
   {
      this.accident = accident;
      if (accident)this.pause = true;
   }
   public int getId() { return id; }
   public void setBouchon(boolean bouchon) { this.bouchon = bouchon; }
   
   public void incrementeTpsPanne() {tpsPanne++; }
   public boolean isARemorquer() {
       if (this.tpsPanne > Voiture.REMORQUAGE) {
          return true;
      }
       return false;
   }

    public DessinVoiture getDessinVoiture() {
        return dessinVoiture;
    }

    public void setDessinVoiture(DessinVoiture dessinVoiture) {
        this.dessinVoiture = dessinVoiture;
    }
}
