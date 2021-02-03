package codagebinaire;

import java.util.ArrayList;

public enum EnumMode {
    NRZ("NRZ") {
        public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel) {
            char previous = Character.MIN_VALUE;
            for (int i = 0; i < trame.length(); i++) {
                char bit = trame.charAt(i);

                // Si le précedent est différent et que c'est pas le premier bit
                if (previous != bit && i != 0) {
                    panel.transitionHautVersBas(posX, posY, lPoint, i); // On ajoute une transition de haut vers bas
                }

                // Si le bit est à 1
                if (bit == '1') {
                    panel.ajouterTraitHorizontal(posX, posY, lPoint, i); // On ajoute en haut
                } else {
                    panel.ajouterTraitHorizontal(posX, posY * 3, lPoint, i); // On ajoute en bas
                }
                previous = bit;
            }
        }
    }, NRZI("NRZI") {
        public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel) {
            char previous = Character.MIN_VALUE;
            for (int i = 0; i < trame.length(); i++) {
                char bit = trame.charAt(i);
                if (previous != bit && i != 0) {
                    panel.transitionHautVersBas(posX, posY, lPoint, i);
                }
                if (bit == '0') {
                    panel.ajouterTraitHorizontal(posX, posY, lPoint, i);
                } else {
                    panel.ajouterTraitHorizontal(posX, posY * 3, lPoint, i);
                }
                previous = bit;
            }
        }
    }, MAN("Manchester") {
        public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel) {
            char previous = Character.MIN_VALUE;
            for (int i = 0; i < trame.length(); i++) {
                char bit = trame.charAt(i);

                // Si il y a deux bits identiques qui se suivent et qu'on est pas sur le premier bit
                if (previous == bit && i != 0) {
                    panel.transitionHautVersBas(posX, posY, lPoint, i);
                }

                // Si le bit est à 1, on effectue une transition sur un demi temps d'horloge
                if (bit == '1') {
                    // On ajoute une transition vers le bas sur un demi temps d'horloge
                    panel.transitionDemiTempsHautVersBas(posX, posY, lPoint, i);
                } else {
                    // On ajoute une transition vers le haut sur un demi temps d'horloge
                    panel.transitionDemiTempsBasVersHaut(posX, posY, lPoint, i);
                }
                previous = bit;
            }
        }
    }, MANDIFF("Manchester différentiel") {
        public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel) {
            boolean estEnHaut = false;
            for (int i = 0; i < trame.length(); i++) {
                char bit = trame.charAt(i);

                // Si le bit est à 0 et que ce n'est pas le premier
                if (bit == '0' && i != 0) {

                    // Si le dernier point ajouté était en haut
                    if (estEnHaut) {
                        panel.transitionHautVersBas(posX, posY, lPoint, i);
                        estEnHaut = false;
                    } else {
                        panel.transitionBasVersHaut(posX, posY, lPoint, i);
                        estEnHaut = true;
                    }
                }

                // Si le dernier point ajouté était en haut
                if (estEnHaut) {
                    panel.transitionDemiTempsHautVersBas(posX, posY, lPoint, i);
                    estEnHaut = false;
                } else {
                    panel.transitionDemiTempsBasVersHaut(posX, posY, lPoint, i);
                    estEnHaut = true;
                }
            }
        }
    }, MILLER("Miller") {
        public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel) {
            boolean estEnHaut = false;
            char previous = Character.MIN_VALUE;
            for (int i = 0; i < trame.length(); i++) {
                char bit = trame.charAt(i);

                // Si 2 bits à 0 se suivent
                if (bit == '0' && previous == '0') {

                    // Si le dernier point ajouté était en haut
                    if (estEnHaut) {
                        panel.transitionHautVersBas(posX, posY, lPoint, i);
                        estEnHaut = false;
                    } else {
                        panel.transitionBasVersHaut(posX, posY, lPoint, i);
                        estEnHaut = true;
                    }
                }

                // Si le dernier point ajouté était en haut
                if (estEnHaut) {

                    // Si le bit est à 0
                    if (bit == '0') {
                        panel.ajouterTraitHorizontal(posX, posY, lPoint, i);
                    } else {
                        panel.transitionDemiTempsHautVersBas(posX, posY, lPoint, i);
                        estEnHaut = false;
                    }
                } else {

                    // Si le bit est à 0
                    if (bit == '0') {
                        panel.ajouterTraitHorizontal(posX, posY * 3, lPoint, i);
                    } else {
                        panel.transitionDemiTempsBasVersHaut(posX, posY, lPoint, i);
                        estEnHaut = true;
                    }
                }
                previous = bit;
            }
        }
    },
    VIDE(""){
        @Override
        public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel) {

        }
    };

    private final String valeur;

    EnumMode(String valeur) {
        this.valeur = valeur;
    }

    public abstract void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> lPoint, String trame, PanelDessin panel);

    public String getValeur() {
        return valeur;
    }
}