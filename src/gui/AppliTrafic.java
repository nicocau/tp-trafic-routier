package gui;

import application.ControleTrafic;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.Arc;
import modele.Noeud;
import modele.ReseauRoutier;
import modele.Voiture;

import java.util.ArrayList;
import java.util.List;

public class AppliTrafic extends Application implements EventHandler<MouseEvent> {

    /**
     * controle du trafic
     */
    ControleTrafic control;
    /**
     * decalage avant dessin reseau
     */
    double decalage;
    /**
     * nb pixels utilise en hauteur pour le dessin effectif du reseau
     */
    double height;
    /***int dimMax rencontree dans les noeuds*/
    double maxDim;
    /**
     * scene de jeu
     */
    Scene scene;
    /**
     * vitesse de l'animation
     */
    long tempo = 500;
    /**
     * troupe des acteurs
     */
    Group troupe;
    /**
     * nb pixels utilise en largeur pour le dessin effectif du reseau
     */
    double width;
    /**
     * les dessins des voitures
     */
    List<DessinVoiture> dessinsVoitures;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * lancement automatique de l'application graphique
     */
    public void start(Stage primaryStage) {
        decalage = 50d;
        width = 500;
        height = 400;
        dessinsVoitures = new ArrayList<>();
        control = new ControleTrafic();
        maxDim = ReseauRoutier.getDimMax();
        construirePlateauJeu(primaryStage);
    }

    /**
     * construction du theatre et de la scene
     */
    void construirePlateauJeu(Stage primaryStage) {
        // definir la scene principale
        troupe = new Group();
        scene = new Scene(troupe, 2 * decalage + width, 2 * decalage + height, Color.ANTIQUEWHITE);
        // definir les acteurs et les habiller
        scene.setFill(Color.DARKGRAY);
        dessinEnvironnement(troupe);

        primaryStage.setTitle("Trafic Routier...");
        primaryStage.setScene(scene);
        // afficher le theatre
        primaryStage.show();
        //-----lancer le timer pour faire vivre la matrice
        int[] nbTop = {0};
        Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo),
                event -> {
                    animDeplacement();
                    nbTop[0]++;
                    if (nbTop[0] % 2 == 0) {
                        Voiture v = this.control.addVoiture(this.control.getHasard().nextBoolean());
                        v.setDessinVoiture(addDessinVoiture(troupe, decalage, v));
                    }
                }));
        littleCycle.setCycleCount(Timeline.INDEFINITE);
        littleCycle.play();
    }

    /**
     * creation des dessins des routes, carrefours et voitures
     */
    void dessinEnvironnement(Group troupe) {
        List<Noeud> noeuds = ReseauRoutier.getNoeuds();

        // creation des carrefours
        double radius = 1 * decalage / 3;
        for (Noeud noeud : noeuds) {
            double cx = decalage + noeud.getX() * width / maxDim;
            double cy = decalage + noeud.getY() * height / maxDim;
            Circle c = null;
            if (noeud.isPrincipal()) {
                c = new Circle(cx, cy, radius);
                c.setFill(Color.BLACK);
                c.setOpacity(0.2);
            } else {
                c = new Circle(cx, cy, radius / 3);
                c.setFill(Color.YELLOW);
            }
            troupe.getChildren().add(c);
            c.setSmooth(true);
        }
        // creation des routes
        for (Noeud noeud : noeuds) {
            double ox = decalage + noeud.getX() * width / maxDim;
            double oy = decalage + noeud.getY() * height / maxDim;
            for (Arc arc : noeud.getArcSortants()) {
                Noeud dest = arc.getEnd();
                double dx = decalage + dest.getX() * width / maxDim;
                double dy = decalage + dest.getY() * height / maxDim;
                Line l = new Line(ox, oy, dx, dy);
                l.setStrokeWidth(6);
                l.setStroke(Color.DARKGOLDENROD);
                troupe.getChildren().add(l);
            }
        }
        List<Voiture> voitures = control.getVoitures();
        //dessin des voitures
        for (Voiture v : voitures) {
            v.setDessinVoiture(addDessinVoiture(troupe, decalage, v));

        }


    }

    /**
     * ajoute un dessin pour la voiture v
     *
     * @param troupe
     * @param decalage
     * @param v
     * @return
     */
    private DessinVoiture addDessinVoiture(Group troupe, double decalage, Voiture v) {
        double cx = decalage + v.getX() * width / maxDim;
        double cy = decalage + v.getY() * height / maxDim;
        DessinVoiture dv = new DessinVoiture(cx, cy, 0.5 * width / maxDim, v.getId());
        troupe.getChildren().add(dv);
        dv.setSmooth(true);
        dv.setOnMouseClicked(this);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        dv.setEffect(dropShadow);
        dessinsVoitures.add(dv);
        return dv;
    }

    /**
     * reponse aux evenements de souris
     */
    public void handle(MouseEvent me) {

        Object o = me.getSource();
        if (o instanceof DessinVoiture) {
            DessinVoiture dv = (DessinVoiture) o;
            dv.switchSelected();
            control.pauseVoiture(dessinsVoitures.indexOf(dv));
        }
    }

    /**
     * realise l'animation pour le deplacement du jeton sélectionné
     */
    private void animDeplacement() {
        List<Voiture> voitures = control.getVoitures();
        List<Voiture> voituresAOter = new ArrayList<>();
        this.control.calculerPointsSuivants();
        this.control.bougerVoitures();
        int nbVoitures = voitures.size();
        for (int i = 0; i < nbVoitures; i++) {
            Voiture v = voitures.get(i);
            DessinVoiture dv = v.getDessinVoiture();
            if (!v.isArrivee() && !v.isPause()) {
                Noeud n = v.getProchainNoeud();
                if (n != null) {
                    if (!dv.selected) {
                        Timeline timeline = new Timeline();
                        double xdest = decalage + n.getX() * width / maxDim;
                        double ydest = decalage + n.getY() * height / maxDim;
                        KeyFrame bougeVoiture = new KeyFrame(new Duration(tempo),
                                new KeyValue(dv.centerXProperty(), xdest),
                                new KeyValue(dv.centerYProperty(), ydest));
                        timeline.getKeyFrames().add(bougeVoiture);
                        timeline.play();
                        dv.setAnimation(timeline);
                    }
                }
            } else if (v.isAccident()) {
                dv.setFill(Color.BLACK);
                v.incrementeTpsPanne();
            }
            if (v.isArrivee() || v.isARemorquer()) {
                voituresAOter.add(v);
                this.troupe.getChildren().remove(dv);
            }
        }
        voituresAOter.forEach(voiture -> this.control.removeCar(voiture));
    }

}
