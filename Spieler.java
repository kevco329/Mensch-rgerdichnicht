package Menschaergerdichnicht;

import java.util.Random;
import java.util.Scanner;

public class Spieler {
    public static int[][] spielerFiguren;
    static String[] spielerNamen;
    static Random random = new Random();
    static int[] startPositionen;
    static int spielfeldGroesse = 40;

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
                System.out.print("Ungültige Eingabe. Bitte eine Zahl zwischen 2 und 8: ");
            }
        }

        spielerFiguren = new int[anzahlSpieler][4];
        spielerNamen = new String[anzahlSpieler];
        startPositionen = new int[anzahlSpieler];

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
        }

        int aktuellerSpieler = 0;

        while (true) {
            String name = spielerNamen[aktuellerSpieler];
            System.out.println("\n========== " + name + " ist am Zug ==========");

            int wurf = 0;
            boolean gueltigerZug = false;

            if (!figurAufFeld(spielerFiguren[aktuellerSpieler])) {
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Drücke Enter für " + i + ". Wurf...");
                    scanner.nextLine();
                    wurf = wuerfeln();
                    System.out.println("Wurf " + i + ": " + wurf);

                    if (wurf == 6) {
                        while (!gueltigerZug) {
                            System.out.println("\nDu hast eine 6 gewürfelt!");
                            System.out.println("Wähle eine Aktion:");
                            System.out.println("1: Bewegliche Figuren anzeigen");
                            System.out.println("2: Positionen aller Figuren anzeigen");
                            System.out.println("3: Figur auswählen und bewegen");
                            System.out.println("4: Zurück zum Hauptmenü");
                            System.out.print("Eingabe: ");
                            String auswahl = scanner.nextLine();

                            switch (auswahl) {
                                case "1":
                                    Spielfeld.zeigeBeweglicheFiguren(spielerFiguren[aktuellerSpieler]);
                                    break;
                                case "2":
                                    int[][] gegnerDaten = getGegnerFiguren(aktuellerSpieler);
                                    Spielfeld.zeigePositionenAllerFiguren(spielerFiguren[aktuellerSpieler], gegnerDaten);
                                    break;
                                case "3":
                                    boolean bewegt = Spielfeld.figurBewegen(spielerFiguren[aktuellerSpieler], scanner, wurf, aktuellerSpieler);
                                    if (bewegt) {
                                        gueltigerZug = true;
                                    } else {
                                        System.out.println("Ungültiger Versuch. Bitte erneut auswählen.");
                                    }
                                    break;
                                case "4":
                                    System.out.println("Zurück zum Hauptmenü...");
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
                    System.out.println("4: Zurück zum Hauptmenü");
                    System.out.print("Eingabe: ");
                    String auswahl = scanner.nextLine();

                    switch (auswahl) {
                        case "1":
                            Spielfeld.zeigeBeweglicheFiguren(spielerFiguren[aktuellerSpieler]);
                            break;
                        case "2":
                            int[][] gegnerDaten = getGegnerFiguren(aktuellerSpieler);
                            Spielfeld.zeigePositionenAllerFiguren(spielerFiguren[aktuellerSpieler], gegnerDaten);
                            break;
                        case "3":
                            boolean bewegt = Spielfeld.figurBewegen(spielerFiguren[aktuellerSpieler], scanner, wurf, aktuellerSpieler);
                            if (bewegt) {
                                zugGemacht = true;
                            } else {
                                System.out.println("Ungültiger Versuch. Bitte erneut auswählen.");
                            }
                            break;
                        case "4":
                            System.out.println("Zurück zum Hauptmenü...");
                            return;
                        default:
                            System.out.println("Ungültige Eingabe.");
                    }
                }

                if (wurf != 6 || chancen >= 3) {
                    break zugSchleife;
                }
            }

            aktuellerSpieler = (aktuellerSpieler + 1) % spielerNamen.length;
        }
    }

    public static int wuerfeln() {
        return random.nextInt(6) + 1;
    }

    public static boolean figurAufFeld(int[] figuren) {
        for (int pos : figuren) {
            if (pos >= 0 && pos < spielfeldGroesse) {
                return true;
            }
        }
        return false;
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

    public static String getSpielerName(int index) {
        return spielerNamen[index];
    }

    public static int getAktuellerSpielerIndex() {
     
        return -1;
    }

}
