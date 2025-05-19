package Menschaergerdichnicht;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class MausBeweger {

    private static volatile boolean laufend = true;

    public static void main(String[] args) {
        // GUI zum Stoppen per ESC
        JFrame frame = new JFrame("MausBeweger – Drücke ESC zum Stoppen");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // zentriert
        JLabel label = new JLabel("Drücke ESC zum Stoppen...", SwingConstants.CENTER);
        frame.add(label);
        frame.setVisible(true);

        // ESC-Taste erkennen
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    laufend = false;
                    System.out.println("Programm wird gestoppt...");
                    frame.dispose();
                }
            }
        });

        // Fokus setzen
        frame.requestFocus();

        // Separater Thread für Mausbewegung
        new Thread(() -> {
            try {
                Robot robot = new Robot();
                Random rand = new Random();

                while (laufend) {
                    int x = rand.nextInt(1920); // Bildschirmbreite ggf. anpassen
                    int y = rand.nextInt(1080); // Bildschirmhöhe ggf. anpassen
                    robot.mouseMove(x, y);
                    Thread.sleep(500); // 0.5 Sekunde Pause
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
