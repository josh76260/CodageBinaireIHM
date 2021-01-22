import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Interface graphique affichant les différentes manières de représenter
 * une trame binaire avec des courants électriques
 *
 * @author Joshua Galien
 */

public class CodageBinaire extends JFrame implements KeyListener {

    private PanelDessin dessin;

    private final JRadioButton nrzButton;
    private final JRadioButton nrziButton;
    private final JRadioButton manchButton;
    private final JRadioButton manchDiffButton;
    private final JRadioButton millerButton;

    private final JTextField codeAffiche;

    /**
     * Constructeur de la fenêtre principale
     */
    public CodageBinaire() {
        setTitle("Représentation d'une trame en fonction de la méthode choisie ");

        setSize(1200, 800);

        // Création des différents boutons correspondants au mode d'affichage

        nrzButton = new JRadioButton("NRZ");
        nrziButton = new JRadioButton("NRZI");
        manchButton = new JRadioButton("Manchester");
        manchDiffButton = new JRadioButton("Manchester Diff");
        millerButton = new JRadioButton("Miller");

        // Ajout d'une action quand on clique sur un des boutons
        nrzButton.addActionListener(actionEvent -> dessiner());
        nrziButton.addActionListener(actionEvent -> dessiner());
        manchButton.addActionListener(actionEvent -> dessiner());
        manchDiffButton.addActionListener(actionEvent -> dessiner());
        millerButton.addActionListener(actionEvent -> dessiner());

        // Ajout de tous les radio boutons à un même groupe de manière à ce qu'il y en ai qu'un seul de selectionné à la fois
        ButtonGroup bg = new ButtonGroup();
        bg.add(nrzButton);
        bg.add(nrziButton);
        bg.add(manchButton);
        bg.add(manchDiffButton);
        bg.add(millerButton);

        // Sélection d'un mode par défaut
        nrzButton.setSelected(true);

        // Ajout des radio boutons au panel de sélection
        JPanel selectMode = new JPanel();
        selectMode.add(nrzButton);
        selectMode.add(nrziButton);
        selectMode.add(manchButton);
        selectMode.add(manchDiffButton);
        selectMode.add(millerButton);

        // Crétion d'une bordure autour du panel
        selectMode.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Mode")));

        // Création du panel de saisie de la trame binaire
        JPanel panelSaisie = new JPanel();
        JLabel lblCode = new JLabel("Code à tracer : ");
        panelSaisie.add(lblCode);

        // Création de la zone de texte pour la saisie de la trame
        codeAffiche = new JTextField(20);
        codeAffiche.addKeyListener(this);
        panelSaisie.add(codeAffiche);

        // Crétion d'une bordure autour du panel
        panelSaisie.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Saisie")));

        // Création de la barre d'outil où seront nos deux panel crées précedemment
        JPanel barreOutil = new JPanel();
        barreOutil.add(selectMode);
        barreOutil.add(panelSaisie);
        add(barreOutil, BorderLayout.NORTH);

        // Création du panel où l'on dessinera la trame
        dessin = new PanelDessin("", "   ");
        add(dessin, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // Pour que le curseur soit directement dans la zone de texte
        codeAffiche.requestFocus();
    }

    /**
     * Fonction qui est en charge d'appeler le constructeur de PanelDessin
     * avec les bons paramètres
     */
    private void dessiner() {
        String item = "";
        if (!codeAffiche.getText().equals("")) {
            String mode = "", code = codeAffiche.getText();
            if (nrzButton.isSelected()) {
                mode = "NR";
                item = nrzButton.getText();
            }
            if (nrziButton.isSelected()) {
                mode = "NI";
                item = nrziButton.getText();
            }
            if (manchButton.isSelected()) {
                mode = "MA";
                item = manchButton.getText();
            }
            if (manchDiffButton.isSelected()) {
                mode = "MD";
                item = manchDiffButton.getText();
            }
            if (millerButton.isSelected()) {
                mode = "MI";
                item = millerButton.getText();
            }

            // Mise à jour du panel de dessin
            remove(dessin);
            dessin = new PanelDessin(mode, code);
            add(dessin, BorderLayout.CENTER);
            repaint();
            revalidate();
        }

        // Changement du titre de la fenêtre principale
        setTitle("Représentation de la trame " + codeAffiche.getText() + " avec la méthode : " + item);
    }

    /**
     * Méthode d'écoute sur les évènements liés au clavier
     * @param e l'évènement clavier
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Test si c'est la source est la touche entrée
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            dessiner();
        } else {
            // Traitement de l'évènement : si c'est autre chose que 0 ou 1 en remplace par un caractère vide
            if (e.getKeyChar() != '0' && e.getKeyChar() != '1') {
                e.setKeyChar(Character.MIN_VALUE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Main qui lance notre fenêtre principale
     * @param args le tableau d'argument du main
     */
    public static void main(String... args) {
        SwingUtilities.invokeLater(CodageBinaire::new);
    }
}
