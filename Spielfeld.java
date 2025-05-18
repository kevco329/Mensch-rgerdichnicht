package Menschaergerdichnicht;

import java.util.Scanner;

public class Spielfeld {
    public static int spielfeldGroesse = 40;

    public static void zeigeBeweglicheFiguren(int[] figuren) {
        System.out.println("Bewegliche Figuren:");
        for (int i = 0; i < figuren.length; i++) {
            if (figuren[i] >= 0 && figuren[i] < spielfeldGroesse) {
                System.out.println("Figur " + (i + 1) + " ist auf Feld " + figuren[i]);
            } else if (figuren[i] == -1) {
                System.out.println("Figur " + (i + 1) + " ist im Haus");
            }
        }
    }

    public static void zeigePositionenAllerFiguren(int[] eigene, int[][] gegner) {
        System.out.println("Eigene Figuren:");
        for (int i = 0; i < eigene.length; i++) {
            if (eigene[i] == -1) {
                System.out.println("Figur " + (i + 1) + ": Im Haus");
            } else {
                System.out.println("Figur " + (i + 1) + ": Feld " + eigene[i]);
            }
        }

        System.out.println("\nGegnerische Figuren:");
        int gegnerIndex = 0;
        for (int[] gegnerFiguren : gegner) {
            String name = Spieler.getSpielerName(getGlobalIndex(gegnerIndex));
            System.out.println(name + ":");
            for (int j = 0; j < gegnerFiguren.length; j++) {
                if (gegnerFiguren[j] == -1) {
                    System.out.println("  Figur " + (j + 1) + ": Im Haus");
                } else {
                    System.out.println("  Figur " + (j + 1) + ": Feld " + gegnerFiguren[j]);
                }
            }
            gegnerIndex++;
        }
    }

    public static boolean figurBewegen(int[] figuren, Scanner scanner, int wurf, int spielerIndex) {
        System.out.print("Welche Figur willst du bewegen? (1–4): ");
        int auswahl = -1;

        try {
            auswahl = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe.");
            return false;
        }

        if (auswahl < 0 || auswahl >= figuren.length) {
            System.out.println("Ungültige Figurenauswahl.");
            return false;
        }

        int aktuellePosition = figuren[auswahl];
        int neuePosition;

        if (aktuellePosition == -1) {
            if (wurf != 6) {
                System.out.println("Du brauchst eine 6, um eine Figur aus dem Haus zu bewegen.");
                return false;
            }

            neuePosition = Spieler.getStartposition(spielerIndex);

           
            for (int i = 0; i < figuren.length; i++) {
                if (i != auswahl && figuren[i] == neuePosition) {
                    System.out.println("Eine eigene Figur steht bereits auf dem Startfeld. Zug nicht möglich.");
                    return false;
                }
            }

            
            for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
                if (i != spielerIndex) {
                    for (int j = 0; j < Spieler.spielerFiguren[i].length; j++) {
                        if (Spieler.spielerFiguren[i][j] == neuePosition) {
                            Spieler.spielerFiguren[i][j] = -1;
                            System.out.println("Eine gegnerische Figur von " + Spieler.getSpielerName(i) + " wurde geschlagen!");
                        }
                    }
                }
            }

            figuren[auswahl] = neuePosition;
            return true;

        } else {
            neuePosition = (aktuellePosition + wurf) % spielfeldGroesse;

     
            for (int i = 0; i < figuren.length; i++) {
                if (i != auswahl && figuren[i] == neuePosition) {
                    System.out.println("Du kannst deine eigene Figur nicht schlagen. Zug nicht möglich.");
                    return false;
                }
            }

          
            for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
                if (i != spielerIndex) {
                    for (int j = 0; j < Spieler.spielerFiguren[i].length; j++) {
                        if (Spieler.spielerFiguren[i][j] == neuePosition) {
                            Spieler.spielerFiguren[i][j] = -1;
                            System.out.println("Eine gegnerische Figur von " + Spieler.getSpielerName(i) + " wurde geschlagen!");
                        }
                    }
                }
            }

            figuren[auswahl] = neuePosition;
            return true;
        }
    }


    private static int getGlobalIndex(int gegnerIndex) {
        int aktuellerIndex = Spieler.getAktuellerSpielerIndex();
        int globalIndex = 0;
        for (int i = 0, count = 0; i < Spieler.spielerFiguren.length; i++) {
            if (i != aktuellerIndex) {
                if (count == gegnerIndex) {
                    globalIndex = i;
                    break;
                }
                count++;
            }
        }
        return globalIndex;
    }
}
