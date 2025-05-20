package Menschaergerdichnicht;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SpielfeldGUI extends JFrame {

   
    private int farbmodus = 1;

  
    private static final String[] SPIELER_FARBEN = {
        "gelb", "rot", "grün", "blau", "schwarz", "weiß", "lila", "türkis"
    };

 
    private static final Map<String, Color> NEON_COLORS = new HashMap<>();
    static {
        NEON_COLORS.put("gelb", new Color(255, 255, 0));
        NEON_COLORS.put("rot", new Color(255, 64, 64));
        NEON_COLORS.put("grün", new Color(0, 255, 128));
        NEON_COLORS.put("blau", new Color(0, 255, 255));
        NEON_COLORS.put("schwarz", new Color(80, 80, 80));
        NEON_COLORS.put("weiß", new Color(240, 240, 255));
        NEON_COLORS.put("lila", new Color(255, 0, 255));
        NEON_COLORS.put("türkis", new Color(0, 255, 200));
    }

    public SpielfeldGUI() {
        setTitle("Mensch ärgere dich nicht – Neon Edition");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        add(new SpielfeldPanel());

        setVisible(true);
    }

   
    class SpielfeldPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard((Graphics2D) g);
        }

        private void drawBoard(Graphics2D g2d) {
           
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            int spielfeldGroesse = 40;
            int radius = 300;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

      
            for (int i = 0; i < spielfeldGroesse; i++) {
                double angle = 2 * Math.PI * i / spielfeldGroesse;
                int x = (int) (centerX + Math.cos(angle) * radius);
                int y = (int) (centerY + Math.sin(angle) * radius);
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillOval(x - 20, y - 20, 40, 40);
                g2d.setColor(Color.GREEN.darker());
                g2d.drawOval(x - 20, y - 20, 40, 40);

             
                for (int j = 0; j < SPIELER_FARBEN.length; j++) {
                    if (Spieler.startPositionen != null && j < Spieler.startPositionen.length
                            && Spieler.startPositionen[j] == i) {
                        g2d.setColor(NEON_COLORS.get(SPIELER_FARBEN[j]));
                        g2d.drawOval(x - 24, y - 24, 48, 48);
                    }
                }
            }

          
            if (Spieler.zielFelder != null) {
                for (int sp = 0; sp < Spieler.zielFelder.length; sp++) {
                    for (int z = 0; z < Spieler.zielFelder[sp].length; z++) {
                        double angle = 2 * Math.PI * Spieler.startPositionen[sp] / spielfeldGroesse;
                        int r = radius - 60 - z * 30;
                        int x = (int) (centerX + Math.cos(angle) * r);
                        int y = (int) (centerY + Math.sin(angle) * r);
                        g2d.setColor(NEON_COLORS.get(SPIELER_FARBEN[sp]));
                        g2d.fillOval(x - 15, y - 15, 30, 30);
                        g2d.setColor(Color.BLACK);
                        g2d.drawOval(x - 15, y - 15, 30, 30);
                    }
                }
            }

          
            if (Spieler.spielerFiguren != null) {
                for (int sp = 0; sp < Spieler.spielerFiguren.length; sp++) {
                    for (int fi = 0; fi < Spieler.spielerFiguren[sp].length; fi++) {
                        int pos = Spieler.spielerFiguren[sp][fi];
                        Color farbe = NEON_COLORS.get(SPIELER_FARBEN[sp]);
                        g2d.setColor(farbe);
                        int x, y;

                      
                        if (pos >= 0 && pos < spielfeldGroesse) {
                            double angle = 2 * Math.PI * pos / spielfeldGroesse;
                            x = (int) (centerX + Math.cos(angle) * radius);
                            y = (int) (centerY + Math.sin(angle) * radius);
                        }
                       
                        else if (Spieler.zielFelder != null && sp < Spieler.zielFelder.length) {
                            for (int z = 0; z < Spieler.zielFelder[sp].length; z++) {
                                if (pos == Spieler.zielFelder[sp][z]) {
                                    double angle = 2 * Math.PI * Spieler.startPositionen[sp] / spielfeldGroesse;
                                    int r = radius - 60 - z * 30;
                                    x = (int) (centerX + Math.cos(angle) * r);
                                    y = (int) (centerY + Math.sin(angle) * r);
                                    g2d.fillOval(x - 12, y - 12, 24, 24);
                                    g2d.setColor(Color.WHITE);
                                    g2d.drawOval(x - 12, y - 12, 24, 24);
                                    continue;
                                }
                            }
                            continue;
                        }
                     
                        else {
                          
                            int[] hausX = {60, getWidth() - 60, getWidth() - 60, 60, 60, getWidth() - 60, getWidth() - 60, 60};
                            int[] hausY = {60, 60, getHeight() - 60, getHeight() - 60, 200, 200, getHeight() - 200, getHeight() - 200};
                            x = hausX[sp % hausX.length];
                            y = hausY[sp % hausY.length] + fi * 32;
                        }
                        g2d.fillOval(x - 12, y - 12, 24, 24);
                        g2d.setColor(Color.WHITE);
                        g2d.drawOval(x - 12, y - 12, 24, 24);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
      
        Spieler.spielerNamen = new String[]{"Kevin", "Alex", "Chris", "Sam"};
        Spieler.startPositionen = new int[]{0, 10, 20, 30};
        Spieler.zielFelder = new int[4][4];
        Spieler.spielerFiguren = new int[4][4];

        int spielfeldGroesse = 40;
        for (int i = 0; i < 4; i++) {
           
            for (int j = 0; j < 4; j++)
                Spieler.zielFelder[i][j] = spielfeldGroesse + ((Spieler.startPositionen[i] - 1 - j + spielfeldGroesse) % spielfeldGroesse);

      
            Spieler.spielerFiguren[i][0] = -1;
            Spieler.spielerFiguren[i][1] = Spieler.startPositionen[i];
            Spieler.spielerFiguren[i][2] = (Spieler.startPositionen[i] + 5) % spielfeldGroesse;
            Spieler.spielerFiguren[i][3] = Spieler.zielFelder[i][0];
        }

        new SpielfeldGUI();
    }
}