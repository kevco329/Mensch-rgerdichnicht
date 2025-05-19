package Menschaergerdichnicht;

import java.util.Scanner;

public class Spielfeld {
    public static int spielfeldGroesse = 40;

    public static void zeigePositionenAllerFiguren(int[] eigeneFiguren, int[][] gegnerFiguren, int spielerIndex) {
        System.out.println("\n--- Deine Figuren ---");
        for (int i = 0; i < eigeneFiguren.length; i++) {
            System.out.println("Figur " + (i + 1) + ": " + getFeldName(eigeneFiguren[i], spielerIndex));
        }

        System.out.println("\n--- Gegnerische Figuren ---");
        int gegnerIndex = 0;
        for (int i = 0; i < Spieler.spielerNamen.length; i++) {
            if (i == spielerIndex) continue;
            System.out.println(Spieler.getSpielerName(i) + ":");
            for (int j = 0; j < gegnerFiguren[gegnerIndex].length; j++) {
                System.out.println("  Figur " + (j + 1) + ": " + getFeldName(gegnerFiguren[gegnerIndex][j], i));
            }
            gegnerIndex++;
        }
    }

    public static void zeigeBeweglicheFiguren(int[] figuren, int wurf, int spielerIndex) {
        System.out.println("\n--- Bewegliche Figuren ---");
        int[] zielFelder = Spieler.getZielfelder(spielerIndex);
        int start = Spieler.getStartposition(spielerIndex);

        for (int i = 0; i < figuren.length; i++) {
            int pos = figuren[i];
            boolean beweglich = false;

            if (pos == -1 && wurf == 6) {
                if (!feldBelegt(start, spielerIndex)) {
                    beweglich = true;
                }
            } else if (pos >= 0 && pos < spielfeldGroesse) {
                int neuePos = pos + wurf;
                if (neuePos < spielfeldGroesse && !eigeneFigurAufFeld(figuren, neuePos % spielfeldGroesse)) {
                    beweglich = true;
                } else if (neuePos >= spielfeldGroesse) {
                    int differenz = neuePos - (spielfeldGroesse - 1);
                    if (differenz >= 1 && differenz <= 4) {
                        int zielfeld = zielFelder[differenz - 1];
                        if (!feldBelegtZiel(zielfeld, figuren)) {
                            beweglich = true;
                        }
                    }
                }
            } else {
                for (int j = 0; j < zielFelder.length; j++) {
                    if (pos == zielFelder[j]) {
                        if (j + wurf < zielFelder.length) {
                            int ziel = zielFelder[j + wurf];
                            if (!feldBelegtZiel(ziel, figuren)) {
                                beweglich = true;
                            }
                        }
                        break;
                    }
                }
            }

            if (beweglich) {
                System.out.println("Figur " + (i + 1) + " auf " + getFeldName(pos, spielerIndex) + " ist beweglich.");
            }
        }
    }

    private static String getFeldName(int feld, int spielerIndex) {
        if (feld == -1) return "Haus";
        int[] zielFelder = Spieler.getZielfelder(spielerIndex);
        for (int i = 0; i < zielFelder.length; i++) {
            if (feld == zielFelder[i]) {
                return "z" + (i + 1);
            }
        }
        return "Feld " + feld;
    }

    public static boolean figurBewegen(int[] figuren, Scanner scanner, int wurf, int spielerIndex) {
        System.out.print("Welche Figur willst du bewegen? (1–4): ");
        int auswahl;
        try {
            auswahl = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe.");
            return false;
        }

        if (auswahl < 0 || auswahl > 3) {
            System.out.println("Ungültige Figurennummer.");
            return false;
        }

        int aktuellePos = figuren[auswahl];

        if (aktuellePos == -1) {
            if (wurf == 6) {
                int start = Spieler.getStartposition(spielerIndex);
                if (!feldBelegt(start, spielerIndex)) {
                    figuren[auswahl] = start;
                    System.out.println("Figur " + (auswahl + 1) + " wurde auf das Feld " + start + " gesetzt.");
                    return true;
                } else {
                    System.out.println("Startfeld ist belegt. Kein Zug möglich.");
                    return false;
                }
            } else {
                System.out.println("Du brauchst eine 6, um aus dem Haus zu kommen.");
                return false;
            }
        }

        int[] zielFelder = Spieler.getZielfelder(spielerIndex);
        for (int i = 0; i < zielFelder.length; i++) {
            if (aktuellePos == zielFelder[i]) {
                int naechstesZielfeld = (i + wurf < 4) ? zielFelder[i + wurf] : -1;
                if (naechstesZielfeld != -1) {
                    if (!feldBelegtZiel(naechstesZielfeld, figuren)) {
                        figuren[auswahl] = naechstesZielfeld;
                        System.out.println("Figur " + (auswahl + 1) + " zieht auf Zielfeld z" + (i + wurf + 1));
                        return true;
                    } else {
                        System.out.println("Ziel-Feld belegt. Kein Zug möglich.");
                        return false;
                    }
                } else {
                    System.out.println("Du brauchst einen exakten Wurf, um ins Ziel zu ziehen.");
                    return false;
                }
            }
        }

        int zielStart = zielFelder[0];
        int spielfeldEnde = Spieler.getSpielfeldGroesse();
        int neuePos = (aktuellePos + wurf);

        if (aktuellePos < spielfeldEnde && neuePos >= spielfeldEnde) {
            int differenz = neuePos - (spielfeldEnde - 1);
            if (differenz >= 1 && differenz <= 4) {
                int zielfeld = zielFelder[differenz - 1];
                if (!feldBelegtZiel(zielfeld, figuren)) {
                    figuren[auswahl] = zielfeld;
                    System.out.println("Figur " + (auswahl + 1) + " zieht auf Zielfeld z" + differenz);
                    return true;
                } else {
                    System.out.println("Zielfeld z" + differenz + " ist belegt. Kein Zug möglich.");
                    return false;
                }
            } else {
                System.out.println("Du brauchst einen exakten Wurf, um ins Ziel zu ziehen.");
                return false;
            }
        }

        int ziel = (aktuellePos + wurf) % spielfeldEnde;
        if (eigeneFigurAufFeld(figuren, ziel)) {
            System.out.println("Dort steht bereits deine eigene Figur. Kein Zug möglich.");
            return false;
        }

        int geschlagenerSpieler = findeGegnerAufFeld(ziel, spielerIndex);
        if (geschlagenerSpieler != -1) {
            int[] gegnerFiguren = Spieler.spielerFiguren[geschlagenerSpieler];
            for (int i = 0; i < gegnerFiguren.length; i++) {
                if (gegnerFiguren[i] == ziel) {
                    gegnerFiguren[i] = -1;
                    System.out.println("Du hast eine Figur von " + Spieler.getSpielerName(geschlagenerSpieler) + " geschlagen!");
                }
            }
        }

        figuren[auswahl] = ziel;
        System.out.println("Figur " + (auswahl + 1) + " wurde auf Feld " + ziel + " bewegt.");
        return true;
    }

    private static boolean feldBelegt(int feld, int aktuellerSpieler) {
        for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
            for (int pos : Spieler.spielerFiguren[i]) {
                if (pos == feld) return true;
            }
        }
        return false;
    }

    private static boolean feldBelegtZiel(int zielfeld, int[] eigeneFiguren) {
        for (int pos : eigeneFiguren) {
            if (pos == zielfeld) {
                return true;
            }
        }
        return false;
    }

    private static boolean eigeneFigurAufFeld(int[] figuren, int ziel) {
        for (int pos : figuren) {
            if (pos == ziel) return true;
        }
        return false;
    }

    private static int findeGegnerAufFeld(int feld, int aktuellerSpieler) {
        for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
            if (i == aktuellerSpieler) continue;
            for (int pos : Spieler.spielerFiguren[i]) {
                if (pos == feld) return i;
            }
        }
        return -1;
    }

    // ✅ NEUE ERGÄNZUNG: Gewinnprüfung
    public static boolean hatSpielerGewonnen(int[] figuren, int spielerIndex) {
        int[] zielFelder = Spieler.getZielfelder(spielerIndex);
        for (int zielfeld : zielFelder) {
            boolean gefunden = false;
            for (int pos : figuren) {
                if (pos == zielfeld) {
                    gefunden = true;
                    break;
                }
            }
            if (!gefunden) {
                return false;
            }
        }
        return true;
    }
}
