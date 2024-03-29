package gui;


import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;

public class DessinVoiture extends Circle {
    /**
     * couleur pour voiture en mouvement
     */
    public static Color couleur = Color.RED;
    /**
     * couleur pour voiture  selectionnee
     */
    public static Color couleurSelected = Color.GAINSBORO;
    /**
     * animation du deplacement
     */
    Timeline animation;
    /**
     * couleur courante de la voiture
     */
    Color cj = couleur;
    /**
     * id
     */
    int no;
    /**
     * vrai si la voiture est actuellement selectionnee
     */
    boolean selected;
    /**
     * ancienne position de la voiture
     */
    private Point anciennePosition;
    /**
     * position de la voiture
     */
    private Point position;

    /**
     * constructeur
     *
     * @param centerX coordonnee X du centre du disque
     * @param centerY coordonnee Y du centre du disque
     * @param radius  taille en pixel du rayon du disque
     */
    public DessinVoiture(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        selected = false;
        getCouleur();
        setFill(cj);
    }

    /**
     * constructeur
     *
     * @param centerX coordonnee X du centre du disque
     * @param centerY coordonnee Y du centre du disque
     * @param radius  taille en pixel du rayon du disque
     * @param no      numero de la voiture associee
     */
    public DessinVoiture(double centerX, double centerY, double radius, int no) {
        this(centerX, centerY, radius);
        this.no = no;
    }

    /**
     * active ou desactive la selection du jeton
     */
    public void switchSelected() {
        selected = !selected;
        getCouleur();
        colorerVoiture();
    }

    /**
     * definit la bonne couleur pour la voiture en fonction de son etat selectionnee ou non
     *
     * @return la couleur de la voiture
     */
    public Color getCouleur() {
        cj = (selected ? couleurSelected : couleur);
        return cj;
    }

    /**
     * remplit le disque avec la couleur courante
     */
    public void colorerVoiture() {
        setFill(cj);
    }

    /**
     * @return le string représentent le desin de la voiture
     */
    public String toString() {
        return "(" + position.x + "," + position.y + ") avant en " + "(" + anciennePosition.x + "," + anciennePosition.y + ") ";
    }

    /**
     * @param animation
     */
    public void setAnimation(Timeline animation) {
        this.animation = animation;
    }
}
