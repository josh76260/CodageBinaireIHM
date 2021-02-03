package codagebinaire;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe permettant de dessiner le graphique correspondant à notre trame binaire
 *
 * @author Joshua Galien
 */

public class PanelDessin extends JPanel {

    /**
     * Trame binaire qui va être desinnée
     */
    private final String trame;

    /**
     * Méthode avec laquelle la trame sera desinnée
     */
    private final EnumMode mode;

    /**
     * Constructeur de la classe PanelDessin
     *
     * @param mode mode de dessin de la trame
     * @param trame la trame à dessiner
     */
    public PanelDessin(EnumMode mode, String trame) {
        this.trame = trame;
        this.mode = mode;
        setVisible(true);
    }

    /**
     * Surcharge de la méthode {@link javax.swing.JPanel#paintComponent(Graphics)}
     *
     * @param g le contexte graphique du panel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Font f = g2.getFont(); // Sauvrgarde de la police afin de la réinitialiser ensuite

        g2.setColor(Color.black);

        // Calcul des valeurs de référence pour faire les dessins en fonction de la taille de la fenêtre
        int posX = getWidth() / (trame.length() + 1);
        int posY = getHeight() / 4;

        // Dessin du quadrillage
        for (int i = 1; i < 4; i++) {
            g2.drawLine(50, posY * i, getWidth() - 50, posY * i);
        }
        for (int i = 1; i < trame.length() + 1; i++) {
            g2.drawLine(posX * i, 50, posX * i, getHeight() - 50);
        }

        // Dessin des unités sur le côté gauche
        g2.setFont(new Font("C", Font.PLAIN, 22));
        g2.drawString(" n V", posX / 3, posY);
        g2.drawString(" 0 V", posX / 3, posY * 2);
        g2.drawString("-n V", posX / 3, posY * 3);

        // Dessine le graphique
        if (!trame.trim().equals(""))
            dessinerGraph(g2, posX, posY);

        // Rétablit la police de base
        g2.setFont(f);
    }

    /**
     * Dessine le graphique en fonction de la taille de la fenêtre
     *
     * @param g2 le contexte graphique du panel
     * @param posX position x de référence pour le dessin du graphique
     * @param posY position y de référence pour le dessin du graphique
     */
    private void dessinerGraph(Graphics2D g2, int posX, int posY) {
        ArrayList<Point> lPoint = new ArrayList<>();
        char previous = Character.MIN_VALUE;
        boolean estEnHaut = false;
        Stroke s = g2.getStroke();

        // Dessine la trame au-dessus du graphique de sorte que la représentation d'un bit soit en dessous de celui-ci
        for (int i = 0; i < trame.length(); i++) {
            g2.drawString(trame.charAt(i) + "", posX * (i + 2) - posX / 2, posY / 2);
        }

        // Cahngement de couleur et la grosseur du trait pour que le graphique soit bien visible
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(5));

        // Dessin du graph en fonction du mode passé à la construction
        mode.dessiner(posX, posY, lPoint, trame, this);

        // Dessin du graphique point par point
        Point prev = lPoint.remove(0);
        for (Point p : lPoint) {
            g2.drawLine(prev.getX(), prev.getY(), p.getX(), p.getY());
            prev = p;
        }

        // Rétablissement des attributs par défaut de g2
        g2.setStroke(s);
    }

    /**
     * Ajoute les points nécessaires pour faire une transition du bas vers le haut
     * @param posX la position x de départ
     * @param posY la position y de départ
     * @param lPoint la liste de points dans laquelle ou va ajouter les nouveaux
     * @param i la position dans la trame qui va permettre de placer correctement nos points
     */
    protected void transitionBasVersHaut(int posX, int posY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(posX * (i + 1), posY * 3));
        lPoint.add(new Point(posX * (i + 1), posY));
    }

    /**
     * Ajoute les points nécessaires pour faire une transition sur un demi temps d'horloge du bas vers le haut
     * @param posX la position x de départ
     * @param posY la position y de départ
     * @param lPoint la liste de points dans laquelle ou va ajouter les nouveaux
     * @param i la position dans la trame qui va permettre de placer correctement nos points
     */
    protected void transitionDemiTempsBasVersHaut(int posX, int posY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(posX * (i + 1), posY * 3));
        lPoint.add(new Point(posX * (i + 2) - posX / 2, posY * 3));
        lPoint.add(new Point(posX * (i + 2) - posX / 2, posY));
        lPoint.add(new Point(posX * (i + 2), posY));
    }

    /**
     * Ajoute les points nécessaires pour faire une transition du haut vers le bas
     * @param posX la position x de départ
     * @param posY la position y de départ
     * @param lPoint la liste de points dans laquelle ou va ajouter les nouveaux
     * @param i la position dans la trame qui va permettre de placer correctement nos points
     */
    protected void transitionDemiTempsHautVersBas(int posX, int posY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(posX * (i + 1), posY));
        lPoint.add(new Point(posX * (i + 2) - posX / 2, posY));
        lPoint.add(new Point(posX * (i + 2) - posX / 2, posY * 3));
        lPoint.add(new Point(posX * (i + 2), posY * 3));
    }

    /**
     * Ajoute les points nécessaires pour faire un trait horizontal
     * @param posX la position x de départ
     * @param posY la position y de départ
     * @param lPoint la liste de points dans laquelle ou va ajouter les nouveaux
     * @param i la position dans la trame qui va permettre de placer correctement nos points
     */
    protected void ajouterTraitHorizontal(int posX, int posY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(posX * (i + 1), posY));
        lPoint.add(new Point(posX * (i + 2), posY));
    }

    /**
     * Ajoute les points nécessaires pour faire une transition du haut vers le bas
     * @param posX la position x de départ
     * @param posY la position y de départ
     * @param lPoint la liste de points dans laquelle ou va ajouter les nouveaux
     * @param i la position dans la trame qui va permettre de placer correctement nos points
     */
    protected void transitionHautVersBas(int posX, int posY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(posX * (i + 1), posY));
        lPoint.add(new Point(posX * (i + 1), posY * 3));
    }

    /**
     * Classe privée représentant les points de notre graphique
     */
    static class Point {

        /**
         * Coordonnée x du point
         */
        int x;

        /**
         * Coordonnée y du point
         */
        int y;

        /**
         * Contructeur de la classe Point
         * @param x position x du point
         * @param y position y du point
         */
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Accesseur sur la position x du point
         * @return la position x du point
         */
        public int getX() {
            return x;
        }

        /**
         * Accesseur sur la position y du point
         * @return la position y du point
         */
        public int getY() {
            return y;
        }
    }



}

