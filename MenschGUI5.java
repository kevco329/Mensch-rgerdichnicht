package Menschaergerdichnicht;

import javax.swing.*;
import java.awt.*;

public class MenschGUI5 extends JFrame {
    private static final int SIZE = 15; // Grid-Größe, anpassbar
    private final JButton[][] fields = new JButton[SIZE][SIZE];
    // 5 Farben für die Spieler
    private final Color[] playerColors = {
        new Color(220, 20, 60),      // Rot
        new Color(255, 215, 0),      // Gelb
        new Color(0, 200, 70),       // Grün
        new Color(30, 144, 255),     // Blau
        new Color(148, 0, 211)       // Lila
    };

    public MenschGUI5() {
        setTitle("Mensch ärgere dich nicht - 5 Spieler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE, 2, 2));
        panel.setBackground(Color.DARK_GRAY);

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                JButton btn = new JButton();
                btn.setOpaque(true);
                btn.setBorderPainted(true);
                btn.setEnabled(false);

                // Beispielhafte Häuser-Positionen für 5 Spieler
                if (isHouse(x, y)) {
                    btn.setBackground(getHouseColor(x, y));
                }
                // Startfelder (je Spieler 1, farbig, auf Rundkurs)
                else if (isStartField(x, y)) {
                    btn.setBackground(getStartColor(x, y));
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    btn.setEnabled(true);
                }
                // Zielfelder (4 pro Spieler, farbig, Richtung Mitte)
                else if (isGoalField(x, y)) {
                    btn.setBackground(getGoalColor(x, y));
                }
                // Rundkurs (nur die 50 Felder, ansonsten leer)
                else if (isPathField(x, y)) {
                    btn.setBackground(Color.BLACK);
                    btn.setEnabled(true);
                }
                // Leere Felder
                else {
                    btn.setBackground(new Color(220, 220, 220));
                    btn.setBorderPainted(false);
                }

                fields[y][x] = btn;
                panel.add(btn);
            }
        }
        add(panel);
        setVisible(true);
    }

    // Dummy-Logik für Häuser (5 Ecken, je ein 2x2 Bereich)
    private boolean isHouse(int x, int y) {
        // Du kannst die Positionen anpassen für ein schöneres Layout
        return (x <= 2 && y <= 2) || (x >= 12 && y <= 2) || (x >= 12 && y >= 12) ||
               (x <= 2 && y >= 12) || (x == 7 && y <= 2);
    }

    private Color getHouseColor(int x, int y) {
        if (x <= 2 && y <= 2) return playerColors[0];   // Rot
        if (x >= 12 && y <= 2) return playerColors[1];  // Gelb
        if (x >= 12 && y >= 12) return playerColors[2]; // Grün
        if (x <= 2 && y >= 12) return playerColors[3];  // Blau
        if (x == 7 && y <= 2) return playerColors[4];   // Lila
        return Color.GRAY;
    }

    // Dummy-Logik für Startfelder (am Rand des Rundkurses, hier als Beispiel)
    private boolean isStartField(int x, int y) {
        return (x == 3 && y == 1) || (x == 13 && y == 3) ||
               (x == 11 && y == 13) || (x == 1 && y == 11) ||
               (x == 7 && y == 0);
    }

    private Color getStartColor(int x, int y) {
        if (x == 3 && y == 1) return playerColors[0];
        if (x == 13 && y == 3) return playerColors[1];
        if (x == 11 && y == 13) return playerColors[2];
        if (x == 1 && y == 11) return playerColors[3];
        if (x == 7 && y == 0) return playerColors[4];
        return Color.GRAY;
    }

    // Dummy-Logik für Zielfelder (je Farbe 4 Felder Richtung Mitte)
    private boolean isGoalField(int x, int y) {
        // Beispielhaft: 4 Felder von Rundkurs Richtung Mitte je Spieler
        return (x == 4 && y >= 3 && y <= 6) || // Rot
               (y == 4 && x >= 8 && x <= 11) || // Gelb
               (x == 10 && y >= 8 && y <= 11) || // Grün
               (y == 10 && x >= 3 && x <= 6) || // Blau
               (x == 7 && y >= 4 && y <= 7); // Lila (Mitte oben nach unten)
    }

    private Color getGoalColor(int x, int y) {
        if (x == 4 && y >= 3 && y <= 6) return playerColors[0];
        if (y == 4 && x >= 8 && x <= 11) return playerColors[1];
        if (x == 10 && y >= 8 && y <= 11) return playerColors[2];
        if (y == 10 && x >= 3 && x <= 6) return playerColors[3];
        if (x == 7 && y >= 4 && y <= 7) return playerColors[4];
        return Color.GRAY;
    }

    // Dummy-Logik für Rundkurs (50 Felder, als Pentagon - stark vereinfacht für Beispiel!)
    private boolean isPathField(int x, int y) {
        // Du solltest die 50 Felder exakt verteilen. Hier nur grob für das Beispiel:
        return ((y == 1 && x >= 3 && x <= 11) ||         // Oben waagerecht
                (x == 13 && y >= 3 && y <= 11) ||        // Rechts senkrecht
                (y == 13 && x >= 3 && x <= 11) ||        // Unten waagerecht
                (x == 1 && y >= 3 && y <= 11) ||         // Links senkrecht
                (x + y == 14 && x >= 3 && x <= 11));     // Diagonal von links unten nach rechts oben (für Pentagon-Optik)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenschGUI5::new);
    }
}