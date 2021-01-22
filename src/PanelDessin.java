import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelDessin extends JPanel {

    private String code, mode;

    public PanelDessin(String mode, String code) {
        this.code = code;
        this.mode = mode;
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Font f = g2.getFont();

        g2.setColor(Color.black);
        int uniteX = getWidth() / (code.length() + 1);
        int uniteY = getHeight() / 4;
        for (int i = 1; i < 4; i++) {
            g2.drawLine(50, uniteY * i, getWidth() - 50, uniteY * i);
        }
        for (int i = 1; i < code.length() + 1; i++) {
            g2.drawLine(uniteX * i, 50, uniteX * i, getHeight() - 50);
        }

        g2.setFont(new Font("C", Font.PLAIN, 22));
        g2.drawString(" n V", uniteX / 3, uniteY);
        g2.drawString(" 0 V", uniteX / 3, uniteY * 2);
        g2.drawString("-n V", uniteX / 3, uniteY * 3);

        if (!code.trim().equals(""))
            dessinerGraph(g2, uniteX, uniteY);

        g2.setFont(f);
    }

    private void dessinerGraph(Graphics2D g2, int uniteX, int uniteY) {
        ArrayList<Point> lPoint = new ArrayList<>();
        char previous = Character.MIN_VALUE;
        boolean estEnHaut = false;
        Stroke s = g2.getStroke();

        for (int i = 0; i < code.length(); i++) {
            g2.drawString(code.charAt(i) + "", uniteX * (i + 2) - uniteX / 2, uniteY / 2);
        }

        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(5));

        switch (mode) {
            case "NR":
                for (int i = 0; i < code.length(); i++) {
                    char c = code.charAt(i);
                    if (previous != c && i != 0) {
                        ajouterHautVersBas(uniteX, uniteY, lPoint, i);
                    }
                    if (c == '1') {
                        ajouterTraitHorizontal(uniteX, uniteY, lPoint, i);
                    } else {
                        ajouterTraitHorizontal(uniteX, uniteY * 3, lPoint, i);
                    }
                    previous = c;
                }
                break;

            case "NI":
                for (int i = 0; i < code.length(); i++) {
                    char c = code.charAt(i);
                    if (previous != c && i != 0) {
                        ajouterHautVersBas(uniteX, uniteY, lPoint, i);
                    }
                    if (c == '0') {
                        ajouterTraitHorizontal(uniteX, uniteY, lPoint, i);
                    } else {
                        ajouterTraitHorizontal(uniteX, uniteY * 3, lPoint, i);
                    }
                    previous = c;
                }
                break;
            case "MA":
                for (int i = 0; i < code.length(); i++) {
                    char c = code.charAt(i);
                    if (previous == c && i != 0) {
                        ajouterHautVersBas(uniteX, uniteY, lPoint, i);
                    }
                    if (c == '1') {
                        transitionDemiTempshautVersBas(uniteX, uniteY, lPoint, i);
                    } else {
                        transitionDemiTempsBasVersHaut(uniteX, uniteY, lPoint, i);
                    }
                    previous = c;
                }
                break;

            case "MD":
                for (int i = 0; i < code.length(); i++) {
                    char c = code.charAt(i);
                    if (c == '0' && i != 0) {
                        if (estEnHaut) {
                            ajouterHautVersBas(uniteX, uniteY, lPoint, i);
                            estEnHaut = false;
                        } else {
                            lPoint.add(new Point(uniteX * (i + 1), uniteY * 3));
                            lPoint.add(new Point(uniteX * (i + 1), uniteY));
                            estEnHaut = true;
                        }
                    }
                    if (estEnHaut) {
                        transitionDemiTempshautVersBas(uniteX, uniteY, lPoint, i);
                        estEnHaut = false;
                    } else {
                        transitionDemiTempsBasVersHaut(uniteX, uniteY, lPoint, i);
                        estEnHaut = true;
                    }
                }
                break;

            case "MI":
                for (int i = 0; i < code.length(); i++) {
                    char c = code.charAt(i);
                    if (c == '0' && previous == '0') {
                        if (estEnHaut) {
                            ajouterHautVersBas(uniteX, uniteY, lPoint, i);
                            estEnHaut = false;
                        } else {
                            lPoint.add(new Point(uniteX * (i + 1), uniteY * 3));
                            lPoint.add(new Point(uniteX * (i + 1), uniteY));
                            estEnHaut = true;
                        }
                    }
                    if (estEnHaut) {
                        if (c == '0') {
                            ajouterTraitHorizontal(uniteX, uniteY, lPoint, i);
                        } else {
                            transitionDemiTempshautVersBas(uniteX, uniteY, lPoint, i);
                            estEnHaut = false;
                        }


                    } else {
                        if (c == '0') {
                            ajouterTraitHorizontal(uniteX, uniteY * 3, lPoint, i);
                        } else {
                            transitionDemiTempsBasVersHaut(uniteX, uniteY, lPoint, i);
                            estEnHaut = true;
                        }

                    }
                    previous = c;
                }
                break;

            default:
                break;
        }

        Point prev = lPoint.remove(0);
        for (Point p : lPoint) {
            g2.drawLine(prev.getX(), prev.getY(), p.getX(), p.getY());
            prev = p;
        }

        g2.setStroke(s);
    }

    private void transitionDemiTempsBasVersHaut(int uniteX, int uniteY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(uniteX * (i + 1), uniteY * 3));
        lPoint.add(new Point(uniteX * (i + 2) - uniteX / 2, uniteY * 3));
        lPoint.add(new Point(uniteX * (i + 2) - uniteX / 2, uniteY));
        lPoint.add(new Point(uniteX * (i + 2), uniteY));
    }

    private void transitionDemiTempshautVersBas(int uniteX, int uniteY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(uniteX * (i + 1), uniteY));
        lPoint.add(new Point(uniteX * (i + 2) - uniteX / 2, uniteY));
        lPoint.add(new Point(uniteX * (i + 2) - uniteX / 2, uniteY * 3));
        lPoint.add(new Point(uniteX * (i + 2), uniteY * 3));
    }

    private void ajouterTraitHorizontal(int uniteX, int uniteY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(uniteX * (i + 1), uniteY));
        lPoint.add(new Point(uniteX * (i + 2), uniteY));
    }

    private void ajouterHautVersBas(int uniteX, int uniteY, ArrayList<Point> lPoint, int i) {
        lPoint.add(new Point(uniteX * (i + 1), uniteY));
        lPoint.add(new Point(uniteX * (i + 1), uniteY * 3));
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}

