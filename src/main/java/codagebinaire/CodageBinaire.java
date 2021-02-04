package codagebinaire;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * Interface graphique affichant les différentes manières de représenter
 * une trame binaire avec des courants électriques.
 *
 * @author Joshua Galien
 */

public class CodageBinaire extends JFrame {

  /**
   * Le panel sur lequel sera dessiné le graphique.
   */
  private PanelDessin dessin;

  /**
   * Panel de selection du mode.
   */
  private JPanel selectMode;

  /**
   * Panel servant à la saisie de la trame binaire.
   */
  private JPanel panelSaisie;

  /**
   * Bouton déclenchant le dessin de la trame avec la méthode NRZ.
   */
  private JRadioButton nrzButton;

  /**
   * Bouton déclenchant le dessin de la trame avec la méthode NRZI.
   */
  private JRadioButton nrziButton;

  /**
   * Bouton déclenchant le dessin de la trame avec la méthode Manchester.
   */
  private JRadioButton manchButton;

  /**
   * Bouton déclenchant le dessin de la trame avec la méthode Manchester différentiel.
   */
  private JRadioButton manchDiffButton;

  /**
   * Bouton déclenchant le dessin de la trame avec la méthode Miller.
   */
  private JRadioButton millerButton;

  /**
   * La zone de texte dans laquelle la trame sera saisie.
   */
  private JTextField codeAffiche;

  /**
   * Mode de dessin définit via une enum.
   */
  private EnumMode mode;

  /**
   * Constructeur de la fenêtre principale.
   */
  public CodageBinaire() {
    setTitle("Représentation d'une trame en fonction de la méthode choisie ");

    setSize(1200, 800);

    // Création des différents boutons correspondants au mode d'affichage

    // Initialisation des boutons radio
    initRadioButton();

    // Initialisation du panel de sélection
    initPanelSelect();

    // Initialisation du panel de saisie
    initPanelSaisie();

    // Initialisation de la barre d'outil
    initBarreOutil();

    // Création du panel où l'on dessinera la trame
    dessin = new PanelDessin(EnumMode.VIDE, "   ");
    add(dessin, BorderLayout.CENTER);

    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);

    // Pour que le curseur soit directement dans la zone de texte
    codeAffiche.requestFocus();
  }

  /**
   * Initialise les boutons radio.
   */
  private void initRadioButton() {
    nrzButton = new JRadioButton("NRZ");
    nrziButton = new JRadioButton("NRZI");
    manchButton = new JRadioButton("Manchester");
    manchDiffButton = new JRadioButton("Manchester Diff");
    millerButton = new JRadioButton("Miller");

    // Ajout d'une action quand on clique sur un des boutons
    nrzButton.addActionListener(actionEvent -> {
      mode = EnumMode.NRZ;
      dessiner();
    });
    nrziButton.addActionListener(actionEvent -> {
      mode = EnumMode.NRZI;
      dessiner();
    });
    manchButton.addActionListener(actionEvent -> {
      mode = EnumMode.MAN;
      dessiner();
    });
    manchDiffButton.addActionListener(actionEvent -> {
      mode = EnumMode.MANDIFF;
      dessiner();
    });
    millerButton.addActionListener(actionEvent -> {
      mode = EnumMode.MILLER;
      dessiner();
    });

    // Ajout de tous les radio boutons à un même groupe de manière à ce
    // qu'il y en ai qu'un seul de selectionné à la fois
    ButtonGroup bg = new ButtonGroup();
    bg.add(nrzButton);
    bg.add(nrziButton);
    bg.add(manchButton);
    bg.add(manchDiffButton);
    bg.add(millerButton);

    // Sélection d'un mode par défaut
    nrzButton.setSelected(true);
    mode = EnumMode.NRZ;
  }

  /**
   * Initialise le panel de sélection.
   */
  private void initPanelSelect() {
    // Ajout des radio boutons au panel de sélection
    selectMode = new JPanel();
    selectMode.add(nrzButton);
    selectMode.add(nrziButton);
    selectMode.add(manchButton);
    selectMode.add(manchDiffButton);
    selectMode.add(millerButton);

    // Crétion d'une bordure autour du panel
    selectMode.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Mode")));
  }

  /**
   * Initialise le panel de saisie de la trame.
   */
  private void initPanelSaisie() {
    // Création du panel de saisie de la trame binaire
    panelSaisie = new JPanel();
    JLabel lblCode = new JLabel("Code à tracer : ");
    panelSaisie.add(lblCode);

    // Création de la zone de texte pour la saisie de la trame
    codeAffiche = new JTextField(20);
    codeAffiche.addKeyListener(new EcouteurClavier(this));
    codeAffiche.setToolTipText("Caractères acceptés : 0 ou 1. "
            + "Vous pouvez taper sur ENTRÉE pour valider.");
    panelSaisie.add(codeAffiche);

    // Crétion d'une bordure autour du panel
    panelSaisie.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Saisie")));
  }

  /**
   * Initialisation de la barre d'outil.
   */
  private void initBarreOutil() {
    // Création de la barre d'outil où seront nos deux panel crées précedemment
    JPanel barreOutil = new JPanel();
    barreOutil.add(selectMode);
    barreOutil.add(panelSaisie);
    add(barreOutil, BorderLayout.NORTH);
  }

  /**
   * Fonction qui est en charge d'appeler le constructeur de PanelDessin
   * avec les bons paramètres.
   */
  protected void dessiner() {
    if (!codeAffiche.getText().equals("")) {
      String code = codeAffiche.getText();

      // Mise à jour du panel de dessin
      remove(dessin);
      dessin = new PanelDessin(mode, code);
      add(dessin, BorderLayout.CENTER);
      repaint();
      revalidate();
    }

    // Changement du titre de la fenêtre principale
    setTitle("Représentation de la trame " + codeAffiche.getText()
            + " avec la méthode : " + mode.getValeur());
  }

  /**
   * Main qui lance notre fenêtre principale.
   *
   * @param args le tableau d'argument du main
   */
  public static void main(String... args) {
    SwingUtilities.invokeLater(CodageBinaire::new);
  }
}
