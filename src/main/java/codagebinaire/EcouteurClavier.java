package codagebinaire;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe servant d'écouteur clavier sur le textfield de CodageBinaire.
 */
public class EcouteurClavier extends KeyAdapter {

  /**
   * Sauvegarde de la fenêtre parente.
   */
  private final CodageBinaire frame;

  /**
   * Constructeur de la classe EcouteurClavier.
   *
   * @param frame la fenêtre parent
   */
  public EcouteurClavier(CodageBinaire frame) {
    this.frame = frame;
  }

  /**
   * Méthode de réaction à chaque frappe de clavier.
   *
   * @param e l'évènement lié au clavier
   */
  @Override
  public void keyTyped(KeyEvent e) {
    // Test si c'est la source est la touche entrée
    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
      frame.dessiner();
    } else {
      // Traitement de l'évènement : si c'est autre chose que 0 ou 1
      // on remplace par un caractère vide
      if (e.getKeyChar() != '0' && e.getKeyChar() != '1') {
        e.setKeyChar(Character.MIN_VALUE);
      }
    }
  }
}
