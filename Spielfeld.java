package Menschaergerdichnicht;

import java.util.Scanner;

public class Spielfeld {
    public static int spielfeldGroesse = 40;

    public static void zeigeBeweglicheFiguren(int[] figuren) {
        System.out.println("Bewegliche Figuren:");
        for (int i = 0; i < figuren.length; i++) {
            if (figuren[i] != -1 && figuren[i] < spielfeldGroesse + 32) {
                System.out.println("Figur " + (i + 1) + ": Feld " + figuren[i]);
            }
        }
    }

    public static void zeigePositionenAllerFiguren(int[] eigeneFiguren, int[][] gegnerFiguren) {
        System.out.println("=== Eigene Figuren ===");
        for (int i = 0; i < eigeneFiguren.length; i++) {
            System.out.println("Figur " + (i + 1) + ": " + feldName(eigeneFiguren[i]));
        }

        System.out.println("=== Gegnerische Figuren ===");
        for (int i = 0; i < gegnerFiguren.length; i++) {
            int gegnerIndex = (i >= Spieler.getAktuellerSpielerIndex()) ? i + 1 : i;
            if (gegnerIndex >= Spieler.spielerNamen.length) continue;
            System.out.println(Spieler.getSpielerName(gegnerIndex) + ":");
            for (int j = 0; j < gegnerFiguren[i].length; j++) {
                System.out.println("  Figur " + (j + 1) + ": " + feldName(gegnerFiguren[i][j]));
            }
        }
    }

    public static String feldName(int pos) {
        if (pos == -1) return "im Haus";
        if (pos <= -100 && pos >= -103) return "Ziel: z" + (-100 - pos);
        if (pos >= spielfeldGroesse) return "im Ziel (Feld " + (pos - spielfeldGroesse + 1) + ")";
        return "Feld " + pos;
    }

    public static boolean figurBewegen(int[] figuren, Scanner scanner, int wurf, int spielerIndex) {
        int startPos = Spieler.getStartposition(spielerIndex);
        int[] zielfelder = Spieler.getZielfelder(spielerIndex);

        System.out.println("Welche Figur möchtest du bewegen? (1–4)");
        int auswahl;
        try {
            auswahl = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe.");
            return false;
        }

        if (auswahl < 0 || auswahl >= figuren.length) {
            System.out.println("Ungültige Figurennummer.");
            return false;
        }

        int aktuellePos = figuren[auswahl];

        // === Fall 1: Figur im Haus ===
        if (aktuellePos == -1) {
            if (wurf != 6) {
                System.out.println("Nur mit einer 6 darf eine Figur das Haus verlassen.");
                return false;
            }

            if (istBelegt(startPos, spielerIndex)) {
                if (istGegner(startPos, spielerIndex)) {
                    kickeFigur(startPos, spielerIndex);
                    System.out.println("Gegnerfigur auf Startfeld geschlagen!");
                } else {
                    System.out.println("Startfeld ist blockiert.");
                    return false;
                }
            }

            figuren[auswahl] = startPos;
            System.out.println("Figur auf Startfeld gesetzt.");
            return true;
        }

        // === Fall 2: Figur im Zielbereich ===
        if (aktuellePos <= -100 && aktuellePos >= -103) {
            int zielIndex = -100 - aktuellePos;
            int neuesZielIndex = zielIndex + wurf;

            if (neuesZielIndex > 3) {
                System.out.println("Zu hoher Wurf für das Ziel. Kein Zug möglich.");
                return false;
            }

            int zielFeldNeu = zielfelder[neuesZielIndex];
            if (istBelegt(zielFeldNeu, spielerIndex)) {
                System.out.println("Ziel-Feld bereits belegt.");
                return false;
            }

            figuren[auswahl] = zielFeldNeu;
            System.out.println("Figur im Ziel weitergezogen (z" + (neuesZielIndex) + ").");
            return true;
        }

        // === Sonderfall: Eine Figur steht alleine im Ziel ===
        if (nurEineImZielUndAlleAnderenImHaus(figuren)) {
            if (wurf != 6) {
                System.out.println("Nur mit einer 6 darf gezogen werden (nur eine Figur im Ziel und Rest im Haus).");
                return false;
            }
        }

        // === Fall 3: Figur steht auf Ziel-Einstiegspunkt ===
        int zielEinstieg = (startPos + spielfeldGroesse - 1) % spielfeldGroesse;

        if (aktuellePos == zielEinstieg) {
            if (wurf > 4) {
                System.out.println("Wurf überschreitet Ziel. Kein Zug möglich.");
                return false;
            }

            int zielFeld = zielfelder[wurf - 1];
            if (istBelegt(zielFeld, spielerIndex)) {
                System.out.println("Ziel-Feld belegt.");
                return false;
            }

            figuren[auswahl] = zielFeld;
            System.out.println("Figur ins Ziel gezogen (z" + (wurf - 1) + ").");
            return true;
        }

        // === Fall 4: Normale Bewegung auf dem Spielfeld ===
        int neuePos = (aktuellePos + wurf) % spielfeldGroesse;

        if (istBelegt(neuePos, spielerIndex)) {
            if (istGegner(neuePos, spielerIndex)) {
                kickeFigur(neuePos, spielerIndex);
                System.out.println("Gegner auf Feld " + neuePos + " geschlagen!");
            } else {
                System.out.println("Ziel-Feld ist durch eigene Figur blockiert.");
                return false;
            }
        }

        figuren[auswahl] = neuePos;
        System.out.println("Figur auf Feld " + neuePos + " gezogen.");
        return true;
    }

    public static boolean istBelegt(int pos, int aktuellerSpieler) {
        for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
            for (int j = 0; j < Spieler.spielerFiguren[i].length; j++) {
                if (Spieler.spielerFiguren[i][j] == pos) return true;
            }
        }
        return false;
    }

    public static boolean istGegner(int pos, int aktuellerSpieler) {
        for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
            if (i == aktuellerSpieler) continue;
            for (int j = 0; j < Spieler.spielerFiguren[i].length; j++) {
                if (Spieler.spielerFiguren[i][j] == pos) return true;
            }
        }
        return false;
    }

    public static void kickeFigur(int pos, int aktuellerSpieler) {
        for (int i = 0; i < Spieler.spielerFiguren.length; i++) {
            if (i == aktuellerSpieler) continue;
            for (int j = 0; j < Spieler.spielerFiguren[i].length; j++) {
                if (Spieler.spielerFiguren[i][j] == pos) {
                    Spieler.spielerFiguren[i][j] = -1;
                    System.out.println("Figur von " + Spieler.getSpielerName(i) + " wurde ins Haus geschickt!");
                }
            }
        }
    }

    // ✅ Zusatzmethode: prüft, ob eine Position ein Zielfeld ist (z0 bis z3)
    public static boolean istZielfeldPosition(int pos) {
        return pos <= -100 && pos >= -103;
    }

    // ✅ Zusatzmethode: prüft Sonderregel "nur eine Figur im Ziel, Rest im Haus"
    public static boolean nurEineImZielUndAlleAnderenImHaus(int[] figuren) {
        int imZiel = 0;
        int imHaus = 0;

        for (int pos : figuren) {
            if (istZielfeldPosition(pos)) imZiel++;
            if (pos == -1) imHaus++;
        }

        return imZiel == 1 && imHaus == 3;
    }
}
