package Menschaergerdichnicht;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Spieler {
    public static int[][] spielerFiguren;
    static String[] spielerNamen;
    static Random random = new Random();
    static int[] startPositionen;
    static int[][] zielFelder;
    static int spielfeldGroesse = 40;

    public static void spielSpeichern(int aktuellerSpieler) {
        try (PrintWriter writer = new PrintWriter("spielstand.txt")) {
            writer.println(spielerNamen.length);
            writer.println(spielfeldGroesse);
            writer.println(aktuellerSpieler); 
            for (String name : spielerNamen) {
                writer.println(name);
            }
            for (int i = 0; i < spielerNamen.length; i++) {
                for (int figur : spielerFiguren[i]) {
                    writer.print(figur + " ");
                }
                writer.println();
            }
            System.out.println("Spiel erfolgreich gespeichert!");
        } catch (Exception e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    public static int spielLaden() {
        int aktuellerSpieler = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("spielstand.txt"))) {
            int anzahlSpieler = Integer.parseInt(reader.readLine());
            spielfeldGroesse = Integer.parseInt(reader.readLine());
            aktuellerSpieler = Integer.parseInt(reader.readLine());
            spielerNamen = new String[anzahlSpieler];
            spielerFiguren = new int[anzahlSpieler][4];
            for (int i = 0; i < anzahlSpieler; i++) {
                spielerNamen[i] = reader.readLine();
            }
            for (int i = 0; i < anzahlSpieler; i++) {
                String[] posStrings = reader.readLine().trim().split(" ");
                for (int j = 0; j < 4; j++) {
                    spielerFiguren[i][j] = Integer.parseInt(posStrings[j]);
                }
            }
           
            startPositionen = new int[anzahlSpieler];
            zielFelder = new int[anzahlSpieler][4];
            for (int i = 0; i < anzahlSpieler; i++) {
                if (anzahlSpieler == 2) {
                    startPositionen[i] = (i == 0) ? 0 : 20;
                } else {
                    startPositionen[i] = i * (spielfeldGroesse / anzahlSpieler);
                }
                int start = startPositionen[i];
                for (int j = 0; j < 4; j++) {
                    int zielfeld = (start - 1 - j + spielfeldGroesse) % spielfeldGroesse;
                    zielFelder[i][j] = spielfeldGroesse + zielfeld;
                }
            }
            System.out.println("Spiel erfolgreich geladen!");
        } catch (Exception e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }
        return aktuellerSpieler;
    }

    public static void spielStarten(Scanner scanner) {
        System.out.print("Wie viele Spieler? (2–8): ");
        int anzahlSpieler = 0;

        while (anzahlSpieler < 2 || anzahlSpieler > 8) {
            try {
                anzahlSpieler = Integer.parseInt(scanner.nextLine());
                if (anzahlSpieler < 2 || anzahlSpieler > 8) {
                    System.out.print("Ungültige Zahl. Bitte 2–8 eingeben: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ungültige Eingabe. Bitte eine Zahl zwischen 2 und 8 eingeben: ");
            }
        }

        spielerFiguren = new int[anzahlSpieler][4];
        spielerNamen = new String[anzahlSpieler];
        startPositionen = new int[anzahlSpieler];
        zielFelder = new int[anzahlSpieler][4];

        spielfeldGroesse = (anzahlSpieler <= 4) ? 40 : anzahlSpieler * 10;
        Spielfeld.spielfeldGroesse = spielfeldGroesse;

        for (int i = 0; i < anzahlSpieler; i++) {
            System.out.print("Name für Spieler " + (i + 1) + ": ");
            spielerNamen[i] = scanner.nextLine();
            for (int j = 0; j < 4; j++) {
                spielerFiguren[i][j] = -1;
            }
        }

        for (int i = 0; i < anzahlSpieler; i++) {
            if (anzahlSpieler == 2) {
                startPositionen[i] = (i == 0) ? 0 : 20;
            } else {
                startPositionen[i] = i * (spielfeldGroesse / anzahlSpieler);
            }
            int start = startPositionen[i];
            for (int j = 0; j < 4; j++) {
                int zielfeld = (start - 1 - j + spielfeldGroesse) % spielfeldGroesse;
                zielFelder[i][j] = spielfeldGroesse + zielfeld;
            }
        }

        int aktuellerSpieler = 0;
        spielSchleife(aktuellerSpieler, scanner);
    }

    public static void spielFortsetzen(int aktuellerSpieler, Scanner scanner) {
        System.out.println(">> Spiel wird fortgesetzt, Spieler: " + aktuellerSpieler);
        spielSchleife(aktuellerSpieler, scanner);
    }

   
    private static void spielSchleife(int aktuellerSpieler, Scanner scanner) {
        while (true) {
            String name = spielerNamen[aktuellerSpieler];
            System.out.println("\n========== " + name + " ist am Zug ==========");

            // --- NEU: 3x würfeln, wenn keine Figur auf dem Feld, aber eine im Haus ---
            int imHaus = 0;
            int aufDemFeld = 0;
            int imZiel = 0;
            for (int pos : spielerFiguren[aktuellerSpieler]) {
                if (pos == -1) imHaus++;
                else if (pos >= spielfeldGroesse) imZiel++;
                else aufDemFeld++;
            }
            if (aufDemFeld == 0 && imHaus > 0) {
                boolean sechsGeworfen = false;
                int wurf = 0;
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Drücke Enter für " + i + ". Wurf...");
                    scanner.nextLine();
                    wurf = wuerfeln();
                    System.out.println("Wurf " + i + ": " + wurf);
                    if (wurf == 6) {
                        sechsGeworfen = true;
                        break;
                    }
                }
                if (!sechsGeworfen) {
                    System.out.println("Leider keine 6 gewürfelt. Nächster Spieler ist dran.");
                    aktuellerSpieler = (aktuellerSpieler + 1) % spielerNamen.length;
                    continue;
                }
                if (!existierenBeweglicheFiguren(spielerFiguren[aktuellerSpieler], 6, aktuellerSpieler)) {
                    System.out.println("Kein Zug möglich. Nächster Spieler ist dran.");
                    aktuellerSpieler = (aktuellerSpieler + 1) % spielerNamen.length;
                    continue;
                }
                boolean gueltigerZug = false;
                while (!gueltigerZug) {
                    System.out.println("\nDu hast eine 6 gewürfelt!");
                    System.out.println("Wähle eine Aktion:");
                    System.out.println("1: Bewegliche Figuren anzeigen");
                    System.out.println("2: Positionen aller Figuren anzeigen");
                    System.out.println("3: Figur auswählen und bewegen");
                    System.out.println("4: Spiel speichern und beenden");
                    System.out.print("Eingabe: ");
                    String auswahl = scanner.nextLine();

                    switch (auswahl) {
                        case "1":
                            Spielfeld.zeigeBeweglicheFiguren(spielerFiguren[aktuellerSpieler], 6, aktuellerSpieler);
                            break;
                        case "2":
                            int[][] gegnerDaten = getGegnerFiguren(aktuellerSpieler);
                            Spielfeld.zeigePositionenAllerFiguren(spielerFiguren[aktuellerSpieler], gegnerDaten, aktuellerSpieler);
                            break;
                        case "3":
                            boolean bewegt = Spielfeld.figurBewegen(spielerFiguren[aktuellerSpieler], scanner, 6, aktuellerSpieler);
                            if (bewegt) gueltigerZug = true;
                            else {
                                // NEU: Nach Fehlversuch prüfen, ob noch ein Zug möglich ist!
                                if (!existierenBeweglicheFiguren(spielerFiguren[aktuellerSpieler], 6, aktuellerSpieler)) {
                                    System.out.println("Kein Zug möglich. Nächster Spieler ist dran.");
                                    gueltigerZug = true;
                                } else {
                                    System.out.println("Ungültiger Versuch. Bitte erneut auswählen.");
                                }
                            }
                            break;
                        case "4":
                            spielSpeichern(aktuellerSpieler);
                            return;
                        default:
                            System.out.println("Ungültige Eingabe.");
                    }
                }
                System.out.println("Du darfst erneut würfeln!");
                continue;
            }
            // --- ENDE NEU ---

            int wurf = 0;
            boolean gueltigerZug = false;

            if (!figurAufFeldOderImZiel(spielerFiguren[aktuellerSpieler])) {
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Drücke Enter für " + i + ". Wurf...");
                    scanner.nextLine();
                    wurf = wuerfeln();
                    System.out.println("Wurf " + i + ": " + wurf);

                    if (wurf == 6) {
                        if (!existierenBeweglicheFiguren(spielerFiguren[aktuellerSpieler], wurf, aktuellerSpieler)) {
                            System.out.println("Kein Zug möglich. Nächster Spieler ist dran.");
                            aktuellerSpieler = (aktuellerSpieler + 1) % spielerNamen.length;
                            gueltigerZug = true;
                            break;
                        }
                        while (!gueltigerZug) {
                            System.out.println("\nDu hast eine 6 gewürfelt!");
                            System.out.println("Wähle eine Aktion:");
                            System.out.println("1: Bewegliche Figuren anzeigen");
                            System.out.println("2: Positionen aller Figuren anzeigen");
                            System.out.println("3: Figur auswählen und bewegen");
                            System.out.println("4: Spiel speichern und beenden");
                            System.out.print("Eingabe: ");
                            String auswahl = scanner.nextLine();

                            switch (auswahl) {
                                case "1":
                                    Spielfeld.zeigeBeweglicheFiguren(spielerFiguren[aktuellerSpieler], wurf, aktuellerSpieler);
                                    break;
                                case "2":
                                    int[][] gegnerDaten = getGegnerFiguren(aktuellerSpieler);
                                    Spielfeld.zeigePositionenAllerFiguren(spielerFiguren[aktuellerSpieler], gegnerDaten, aktuellerSpieler);
                                    break;
                                case "3":
                                    boolean bewegt = Spielfeld.figurBewegen(spielerFiguren[aktuellerSpieler], scanner, wurf, aktuellerSpieler);
                                    if (bewegt) gueltigerZug = true;
                                    else {
                                        if (!existierenBeweglicheFiguren(spielerFiguren[aktuellerSpieler], wurf, aktuellerSpieler)) {
                                            System.out.println("Kein Zug möglich. Nächster Spieler ist dran.");
                                            gueltigerZug = true;
                                        } else {
                                            System.out.println("Ungültiger Versuch. Bitte erneut auswählen.");
                                        }
                                    }
                                    break;
                                case "4":
                                    spielSpeichern(aktuellerSpieler);
                                    return;
                                default:
                                    System.out.println("Ungültige Eingabe.");
                            }
                        }
                        System.out.println("Du darfst erneut würfeln!");
                        break;
                    }
                }

                if (!gueltigerZug) {
                    System.out.println("Leider keine 6 gewürfelt. Nächster Spieler ist dran.");
                    aktuellerSpieler = (aktuellerSpieler + 1) % spielerNamen.length;
                    continue;
                }
            }

            int chancen = 0;
            zugSchleife:
            while (true) {
                System.out.println("Drücke Enter zum Würfeln...");
                scanner.nextLine();
                wurf = wuerfeln();
                System.out.println(name + " hat eine " + wurf + " gewürfelt!");

                // Vor jedem Zug prüfen, ob überhaupt ein Zug möglich ist!
                if (!existierenBeweglicheFiguren(spielerFiguren[aktuellerSpieler], wurf, aktuellerSpieler)) {
                    System.out.println("Kein Zug möglich. Nächster Spieler ist dran.");
                    break zugSchleife;
                }

                if (wurf == 6) {
                    chancen = 0;
                    System.out.println("Du darfst erneut würfeln!");
                } else {
                    chancen++;
                }

                boolean zugGemacht = false;
                while (!zugGemacht) {
                    System.out.println("\nLetzter Wurf: " + wurf);
                    System.out.println("Wähle eine Aktion:");
                    System.out.println("1: Bewegliche Figuren anzeigen");
                    System.out.println("2: Positionen aller Figuren anzeigen");
                    System.out.println("3: Figur auswählen und bewegen");
                    System.out.println("4: Spiel speichern und beenden");
                    System.out.print("Eingabe: ");
                    String auswahl = scanner.nextLine();

                    switch (auswahl) {
                        case "1":
                            Spielfeld.zeigeBeweglicheFiguren(spielerFiguren[aktuellerSpieler], wurf, aktuellerSpieler);
                            break;
                        case "2":
                            int[][] gegnerDaten = getGegnerFiguren(aktuellerSpieler);
                            Spielfeld.zeigePositionenAllerFiguren(spielerFiguren[aktuellerSpieler], gegnerDaten, aktuellerSpieler);
                            break;
                        case "3":
                            boolean bewegt = Spielfeld.figurBewegen(spielerFiguren[aktuellerSpieler], scanner, wurf, aktuellerSpieler);
                            if (bewegt) zugGemacht = true;
                            else {
                                if (!existierenBeweglicheFiguren(spielerFiguren[aktuellerSpieler], wurf, aktuellerSpieler)) {
                                    System.out.println("Kein Zug möglich. Nächster Spieler ist dran.");
                                    zugGemacht = true; // Schleife verlassen!
                                } else {
                                    System.out.println("Ungültiger Versuch. Bitte erneut auswählen.");
                                }
                            }
                            break;
                        case "4":
                            spielSpeichern(aktuellerSpieler);
                            return;
                        default:
                            System.out.println("Ungültige Eingabe.");
                    }
                }

                if (wurf != 6 || chancen >= 3) break zugSchleife;
            }

            if (hatGewonnen(aktuellerSpieler)) {
                System.out.println("🎉 Spieler " + name + " hat gewonnen! 🎉");
                break;
            }

            aktuellerSpieler = (aktuellerSpieler + 1) % spielerNamen.length;
        }
    }

    // Fügt ans Ende der Klasse Spieler (außerhalb von spielSchleife) diese Hilfsmethode ein:
    private static boolean existierenBeweglicheFiguren(int[] figuren, int wurf, int spielerIndex) {
        int[] zielFelder = getZielfelder(spielerIndex);
        int start = getStartposition(spielerIndex);
        int spielfeldGroesse = getSpielfeldGroesse();
        for (int i = 0; i < figuren.length; i++) {
            int pos = figuren[i];
            if (pos == -1 && wurf == 6 && !Spielfeld.eigeneFigurAufFeld(figuren, start)) return true;
            else if (pos >= 0 && pos < spielfeldGroesse) {
                int neuePos = (pos + wurf) % spielfeldGroesse;
                if (!Spielfeld.eigeneFigurAufFeld(figuren, neuePos)) return true;
                // Möglichkeit ins Ziel
                int felderBisStart = (start - pos - 1 + spielfeldGroesse) % spielfeldGroesse;
                if (wurf > felderBisStart && wurf <= felderBisStart + 4) {
                    int zielfeldIndex = wurf - felderBisStart - 1;
                    if (zielfeldIndex >= 0 && zielfeldIndex < 4 && !Spielfeld.feldBelegtZiel(zielFelder[zielfeldIndex], figuren)) return true;
                }
            } else {
                for (int j = 0; j < zielFelder.length; j++) {
                    if (pos == zielFelder[j]) {
                        if (j + wurf < zielFelder.length && !Spielfeld.feldBelegtZiel(zielFelder[j + wurf], figuren)) return true;
                    }
                }
            }
        }
        return false;
    }
    public static int wuerfeln() {
        return random.nextInt(6) + 1;
    }

    public static boolean figurAufFeldOderImZiel(int[] figuren) {
        int aufDemFeld = 0;
        int imHaus = 0;
        int imZiel = 0;

        for (int pos : figuren) {
            if (pos == -1) imHaus++;
            else if (pos >= spielfeldGroesse) imZiel++;
            else aufDemFeld++;
        }

        if (imHaus == 3 && imZiel == 1) {
            int zielEnde = -1;
            for (int i = 0; i < spielerFiguren.length; i++) {
                if (spielerFiguren[i] == figuren) {
                    zielEnde = zielFelder[i][3]; 
                    break;
                }
            }
            for (int pos : figuren) {
                if (pos == zielEnde) {
                    return false;
                }
            }
        }
        return aufDemFeld > 0 || imZiel > 0;
    }

    private static int[][] getGegnerFiguren(int aktuellerSpieler) {
        int gegnerAnzahl = spielerFiguren.length - 1;
        int[][] gegnerDaten = new int[gegnerAnzahl][];
        int index = 0;
        for (int i = 0; i < spielerFiguren.length; i++) {
            if (i != aktuellerSpieler) {
                gegnerDaten[index++] = spielerFiguren[i];
            }
        }
        return gegnerDaten;
    }

    public static int getStartposition(int spielerIndex) {
        return startPositionen[spielerIndex];
    }

    public static int[] getZielfelder(int spielerIndex) {
        return zielFelder[spielerIndex];
    }

    public static String getSpielerName(int index) {
        return spielerNamen[index];
    }

    public static int getSpielfeldGroesse() {
        return spielfeldGroesse;
    }

    private String name;
    private ArrayList figuren;
    public Spieler(String name, int[] figurenPositionen) {
        this.name = name;
        this.figuren = new ArrayList<>();
        for (int pos : figurenPositionen) {
            this.figuren.add(new Figur(pos));
        }
    }

    public String getName() {
        return name;
    }

    public List<Figur> getFiguren() {
        return figuren;
    }

    public static boolean hatGewonnen(int spielerIndex) {
        for (int i = 0; i < 4; i++) {
            if (spielerFiguren[spielerIndex][i] != zielFelder[spielerIndex][3 - i]) {
                return false;
            }
        }
        return true;
    }

    
}