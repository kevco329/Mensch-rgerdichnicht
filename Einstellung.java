package Menschaergerdichnicht;

import java.util.Scanner;

public class Einstellung {
    public static void oeffneEinstellungen(Scanner scanner) {
        System.out.println("Farbmodus einstellen:");
        System.out.println("1. Kein Farbmodus");
        System.out.println("2. Rot-Grün-Schwäche");
        System.out.println("3. Blau-Gelb-Schwäche");
        System.out.println("4. Totaler Farbverlust (Monochrom)");

        String auswahl = scanner.nextLine().trim();
        switch (auswahl) {
            case "1":
                System.out.println("Standardfarben werden verwendet.");
                break;
            case "2":
                System.out.println("Farben für Rot-Grün-Schwäche angepasst.");
                break;
            case "3":
                System.out.println("Farben für Blau-Gelb-Schwäche angepasst.");
                break;
            case "4":
                System.out.println("Monochrom-Modus aktiviert.");
                break;
            default:
                System.out.println("Ungültige Auswahl.");
        }
    }
}
