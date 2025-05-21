package Menschaergerdichnicht;

import javax.swing.*;
import java.awt.*;

public class MenschGUI extends JFrame {
    private static final int SIZE = 11;
    private final JButton[][] fields = new JButton[SIZE][SIZE];

    // Spielerfarben: Rot, Gelb, Grün, Blau
    private final Color RED = new Color(220, 20, 60);
    private final Color YELLOW = new Color(255, 215, 0);
    private final Color GREEN = new Color(0, 200, 70);
    private final Color BLUE = new Color(30, 144, 255);

    public MenschGUI() {
        setTitle("Mensch ärgere dich nicht - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE, 2, 2));
        panel.setBackground(Color.DARK_GRAY);

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                JButton btn = new JButton();
                btn.setOpaque(true);
                btn.setBorderPainted(true);
                btn.setEnabled(false);

                // Häuser (2x2 in den Ecken)
                if ((x == 1 || x == 2) && (y == 1 || y == 2)) { // Rot
                    btn.setBackground(RED);
                } else if ((x == 8 || x == 9) && (y == 1 || y == 2)) { // Gelb
                    btn.setBackground(YELLOW);
                } else if ((x == 1 || x == 2) && (y == 8 || y == 9)) { // Grün
                    btn.setBackground(GREEN);
                } else if ((x == 8 || x == 9) && (y == 8 || y == 9)) { // Blau
                    btn.setBackground(BLUE);
                }
                // Startfelder (bleiben wie gehabt, farbig und umrandet)
                else if (x == 4 && y == 10) { // Grün Startfeld
                    btn.setBackground(GREEN);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    btn.setEnabled(true);
                } else if (x == 0 && y == 4) { // Rot Startfeld
                    btn.setBackground(RED);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    btn.setEnabled(true);
                } else if (x == 6 && y == 0) { // Gelb Startfeld
                    btn.setBackground(YELLOW);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    btn.setEnabled(true);
                } else if (x == 10 && y == 6) { // Blau Startfeld
                    btn.setBackground(BLUE);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    btn.setEnabled(true);
                }
                // Zielfelder (je 4 pro Farbe)
                else if (x == 5 && y >= 1 && y <= 4) { // Gelb Ziel
                    btn.setBackground(YELLOW);
                } else if (x == 5 && y >= 6 && y <= 9) { // Grün Ziel
                    btn.setBackground(GREEN);
                } else if (y == 5 && x >= 1 && x <= 4) { // Rot Ziel
                    btn.setBackground(RED);
                } else if (y == 5 && x >= 6 && x <= 9) { // Blau Ziel
                    btn.setBackground(BLUE);
                }
                // RUNDKURS: Nur Felder, die direkt um die Zielfelder herum liegen
                else if (isTightPath(x, y)) {
                    btn.setBackground(Color.BLACK);
                    btn.setEnabled(true);
                }
                // Rest: leer
                else {
                    btn.setBackground(new Color(220, 220, 220));
                    btn.setBorderPainted(false);
                    btn.setEnabled(false);
                }

                fields[y][x] = btn;
                panel.add(btn);
            }
        }

        add(panel);
        setVisible(true);
    }

    // Gibt zurück, ob das Feld direkt um die Zielfelder "klebt"
    private boolean isTightPath(int x, int y) {
        // Oben um das gelbe Ziel herum
        if (y == 0 && x >= 4 && x <= 6) return true;
        if (y == 1 && (x == 4 || x == 6)) return true;
        if (y == 2 && (x == 4 || x == 6)) return true;
        if (y == 3 && (x == 4 || x == 6)) return true;
        if (y == 4 && (x == 4 || x == 6)) return true;

        // Rechts um das blaue Ziel herum
        if (x == 10 && y >= 4 && y <= 6) return true;
        if (x == 9 && (y == 4 || y == 6)) return true;
        if (x == 8 && (y == 4 || y == 6)) return true;
        if (x == 7 && (y == 4 || y == 6)) return true;
        if (x == 6 && (y == 4 || y == 6)) return true;

        // Unten um das grüne Ziel herum
        if (y == 10 && x >= 4 && x <= 6) return true;
        if (y == 9 && (x == 4 || x == 6)) return true;
        if (y == 8 && (x == 4 || x == 6)) return true;
        if (y == 7 && (x == 4 || x == 6)) return true;
        if (y == 6 && (x == 4 || x == 6)) return true;

        // Links um das rote Ziel herum
        if (x == 0 && y >= 4 && y <= 6) return true;
        if (x == 1 && (y == 4 || y == 6)) return true;
        if (x == 2 && (y == 4 || y == 6)) return true;
        if (x == 3 && (y == 4 || y == 6)) return true;
        if (x == 4 && (y == 4 || y == 6)) return true;

        // Startfelder ausnehmen, sie werden separat gefärbt!
        if ((x == 4 && y == 10) || (x == 0 && y == 4) || (x == 6 && y == 0) || (x == 10 && y == 6)) return false;

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenschGUI::new);
    }
}