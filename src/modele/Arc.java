package modele;

import java.util.ArrayList;
import java.util.List;

public class Arc
{
   /**noeud de depart*/
   private Noeud start;
   /**noeud d'arrivee*/
   private Noeud end;
   /**noeuds secondaire entre les deux noeuds principaux*/
   Noeud[] petitsNoeuds;
   /**liste des voitures sur noeud*/
   List<Voiture> cars = new ArrayList<>();
   /**nom de l'arc*/
   String name;

   public Arc() {   }


   /**
    * cree un arc entre 2 noeuds
    * @param start noeud de depart
    * @param end noeud d'arrivee
    * */
   public Arc(Noeud start, Noeud end) {
      super();
      this.start = start;
      this.end = end;
      start.addArcSortant(this);
      end.addArcEntrant(this);
      this.name = ""+start.id+"-"+end.id;
   }

   /**
    * cree un arc entre 2 noeuds, et des petits noeuds intermediaires
    * @param start noeud de depart
    * @param end noeud d'arrivee
    * @param nbPetitsNoeuds nb de petits noeuds intermediaires
    * */
   public Arc(Noeud start, Noeud end, int nbPetitsNoeuds) {
      super();
      this.start = start;
      this.end = start;
      petitsNoeuds = new Noeud[nbPetitsNoeuds];
      int pasX = (end.x-start.x)/(nbPetitsNoeuds+1);
      int pasY = (end.y-start.y)/(nbPetitsNoeuds+1);
      Noeud noeudAvant = start;
      for(int i=1; i<=nbPetitsNoeuds; i++)
      {
         int coorX = start.x + i*pasX;
         int coorY = start.y + i*pasY;
         Noeud petitsNoeud = new Noeud(coorX, coorY, false);
         petitsNoeuds[i-1] = petitsNoeud;
         Arc arc = new Arc(noeudAvant, petitsNoeuds[i-1]);
         ReseauRoutier.addArc(arc);            
         ReseauRoutier.addNoeud(petitsNoeuds[i-1] );
         noeudAvant = petitsNoeuds[i-1];
      }
      Arc arc = new Arc(noeudAvant, end);
      ReseauRoutier.addArc(arc);
      this.name = ""+start.id+"-"+end.id;
   }    
   
   /**@return the ieme petit noeud*/
   public Noeud getPetitNoeud(int i)
   {return petitsNoeuds[i];}
   
   public Noeud getStart() { return start; }
   public Noeud getEnd() { return end; }

   public void setStart(Noeud start) { this.start = start; }
   public void setEnd(Noeud end) { this.end = end; }

   public String toString()
   {
      StringBuilder sb = new StringBuilder(name);
      return sb.toString();
   }
}
