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

    public CodageBinaire() {
        setTitle("Représentation d'une trame en fonction de la méthode choisie ");

        setSize(1200, 800);

        nrzButton = new JRadioButton("NRZ");
        nrziButton = new JRadioButton("NRZI");
        manchButton = new JRadioButton("Manchester");
        manchDiffButton = new JRadioButton("Manchester Diff");
        millerButton = new JRadioButton("Miller");

        nrzButton.addActionListener(actionEvent -> dessiner());
        nrziButton.addActionListener(actionEvent -> dessiner());
        manchButton.addActionListener(actionEvent -> dessiner());
        manchDiffButton.addActionListener(actionEvent -> dessiner());
        millerButton.addActionListener(actionEvent -> dessiner());

        ButtonGroup bg = new ButtonGroup();
        bg.add(nrzButton);
        bg.add(nrziButton);
        bg.add(manchButton);
        bg.add(manchDiffButton);
        bg.add(millerButton);

        nrzButton.setSelected(true);

        JPanel selectMode = new JPanel();
        selectMode.add(nrzButton);
        selectMode.add(nrziButton);
        selectMode.add(manchButton);
        selectMode.add(manchDiffButton);
        selectMode.add(millerButton);

        selectMode.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Mode")));

        JPanel panelSaisie = new JPanel();
        JLabel lblCode = new JLabel("Code à tracer : ");
        panelSaisie.add(lblCode);

        codeAffiche = new JTextField(20);
        codeAffiche.addKeyListener(this);
        panelSaisie.add(codeAffiche);
        panelSaisie.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Saisie")));

        JPanel barreOutil = new JPanel();
        barreOutil.add(selectMode);
        barreOutil.add(panelSaisie);
        add(barreOutil, BorderLayout.NORTH);

        dessin = new PanelDessin("", "   ");
        add(dessin, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        codeAffiche.requestFocus();
    }

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

            remove(dessin);
            dessin = new PanelDessin(mode, code);
            add(dessin, BorderLayout.CENTER);
            repaint();
            revalidate();
        }

        setTitle("Représentation de la trame " + codeAffiche.getText() + " avec la méthode : " + item);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            dessiner();
        } else {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CodageBinaire::new);
    }
}
