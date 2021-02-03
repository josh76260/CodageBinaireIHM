package codagebinaire;

import java.util.ArrayList;

public enum EnumMode {
  /**
   * Constante représentant le mode de dessin NRZ.
   */
  NRZ("NRZ") {
    /**
     * Surcharge de la méthode abstraite de dessin.
     *
     * @param posX position x de référence
     * @param posY position x de référence
     * @param listePoint la liste de points qui va servir au dessin
     * @param trame la trame à dessiner
     * @param panel le panel sur lequel on va dessiner
     */
    @Override
    public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                         String trame, PanelDessin panel) {
      char previous = Character.MIN_VALUE;
      for (int i = 0; i < trame.length(); i++) {
        char bit = trame.charAt(i);

        // Si le précedent est différent et que c'est pas le premier bit
        if (previous != bit && i != 0) {
          // On ajoute une transition de haut vers bas
          panel.transitionHautVersBas(posX, posY, listePoint, i);
        }

        // Si le bit est à 1
        if (bit == '1') {
          panel.ajouterTraitHorizontal(posX, posY, listePoint, i); // On ajoute en haut
        } else {
          panel.ajouterTraitHorizontal(posX, posY * 3, listePoint, i); // On ajoute en bas
        }
        previous = bit;
      }
    }
  },

  /**
   * Constante représentant le mode de dessin NRZI.
   */
  NRZI("NRZI") {
    /**
     * Surcharge de la méthode abstraite de dessin.
     *
     * @param posX position x de référence
     * @param posY position x de référence
     * @param listePoint la liste de points qui va servir au dessin
     * @param trame la trame à dessiner
     * @param panel le panel sur lequel on va dessiner
     */
    @Override
    public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                         String trame, PanelDessin panel) {
      char previous = Character.MIN_VALUE;
      for (int i = 0; i < trame.length(); i++) {
        char bit = trame.charAt(i);
        if (previous != bit && i != 0) {
          panel.transitionHautVersBas(posX, posY, listePoint, i);
        }
        if (bit == '0') {
          panel.ajouterTraitHorizontal(posX, posY, listePoint, i);
        } else {
          panel.ajouterTraitHorizontal(posX, posY * 3, listePoint, i);
        }
        previous = bit;
      }
    }
  },

  /**
   * Constante représentant la méthode de dessin Manchester.
   */
  MAN("Manchester") {
    /**
     * Surcharge de la méthode abstraite de dessin.
     *
     * @param posX position x de référence
     * @param posY position x de référence
     * @param listePoint la liste de points qui va servir au dessin
     * @param trame la trame à dessiner
     * @param panel le panel sur lequel on va dessiner
     */
    @Override
    public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                         String trame, PanelDessin panel) {
      char previous = Character.MIN_VALUE;
      for (int i = 0; i < trame.length(); i++) {
        char bit = trame.charAt(i);

        // Si il y a deux bits identiques qui se suivent et qu'on est pas sur le premier bit
        if (previous == bit && i != 0) {
          panel.transitionHautVersBas(posX, posY, listePoint, i);
        }

        // Si le bit est à 1, on effectue une transition sur un demi temps d'horloge
        if (bit == '1') {
          // On ajoute une transition vers le bas sur un demi temps d'horloge
          panel.transitionDemiTempsHautVersBas(posX, posY, listePoint, i);
        } else {
          // On ajoute une transition vers le haut sur un demi temps d'horloge
          panel.transitionDemiTempsBasVersHaut(posX, posY, listePoint, i);
        }
        previous = bit;
      }
    }
  },

  /**
   * Constante représentant la méthode de dessin Manchester différentiel.
   */
  MANDIFF("Manchester différentiel") {
    /**
     * Surcharge de la méthode abstraite de dessin.
     *
     * @param posX position x de référence
     * @param posY position x de référence
     * @param listePoint la liste de points qui va servir au dessin
     * @param trame la trame à dessiner
     * @param panel le panel sur lequel on va dessiner
     */
    @Override
    public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                         String trame, PanelDessin panel) {
      boolean estEnHaut = false;
      for (int i = 0; i < trame.length(); i++) {
        char bit = trame.charAt(i);

        // Si le bit est à 0 et que ce n'est pas le premier
        if (bit == '0' && i != 0) {

          // Si le dernier point ajouté était en haut
          if (estEnHaut) {
            panel.transitionHautVersBas(posX, posY, listePoint, i);
            estEnHaut = false;
          } else {
            panel.transitionBasVersHaut(posX, posY, listePoint, i);
            estEnHaut = true;
          }
        }

        // Si le dernier point ajouté était en haut
        if (estEnHaut) {
          panel.transitionDemiTempsHautVersBas(posX, posY, listePoint, i);
          estEnHaut = false;
        } else {
          panel.transitionDemiTempsBasVersHaut(posX, posY, listePoint, i);
          estEnHaut = true;
        }
      }
    }
  },

  /**
   * Constante représentant la méthode de dessin Miller.
   */
  MILLER("Miller") {
    /**
     * Surcharge de la méthode abstraite de dessin.
     *
     * @param posX position x de référence
     * @param posY position x de référence
     * @param listePoint la liste de points qui va servir au dessin
     * @param trame la trame à dessiner
     * @param panel le panel sur lequel on va dessiner
     */
    @Override
    public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                         String trame, PanelDessin panel) {
      boolean estEnHaut = false;
      char previous = Character.MIN_VALUE;
      for (int i = 0; i < trame.length(); i++) {
        char bit = trame.charAt(i);

        // Si 2 bits à 0 se suivent
        if (bit == '0' && previous == '0') {

          // Si le dernier point ajouté était en haut
          if (estEnHaut) {
            panel.transitionHautVersBas(posX, posY, listePoint, i);
            estEnHaut = false;
          } else {
            panel.transitionBasVersHaut(posX, posY, listePoint, i);
            estEnHaut = true;
          }
        }

        // Si le dernier point ajouté était en haut
        if (estEnHaut) {

          // Si le bit est à 0
          if (bit == '0') {
            panel.ajouterTraitHorizontal(posX, posY, listePoint, i);
          } else {
            panel.transitionDemiTempsHautVersBas(posX, posY, listePoint, i);
            estEnHaut = false;
          }
        } else {

          // Si le bit est à 0
          if (bit == '0') {
            panel.ajouterTraitHorizontal(posX, posY * 3, listePoint, i);
          } else {
            panel.transitionDemiTempsBasVersHaut(posX, posY, listePoint, i);
            estEnHaut = true;
          }
        }
        previous = bit;
      }
    }
  },
  /**
   * Constante représentant le mode de dessin vide.
   */
  VIDE("") {
    @Override
    public void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                         String trame, PanelDessin panel) {

    }
  };

  private final String valeur;

  EnumMode(String valeur) {
    this.valeur = valeur;
  }

  /**
   * Méthode abstraite pour dessiner.
   *
   * @param posX       position x de référence
   * @param posY       position x de référence
   * @param listePoint la liste de points qui va servir au dessin
   * @param trame      la trame à dessiner
   * @param panel      le panel sur lequel on va dessiner
   */
  public abstract void dessiner(int posX, int posY, ArrayList<PanelDessin.Point> listePoint,
                                String trame, PanelDessin panel);

  /**
   * Accesseur sur l'attribut valeur.
   *
   * @return la valeur correspondant au mode
   */
  public String getValeur() {
    return valeur;
  }
}