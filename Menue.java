package Menschaergerdichnicht;

import java.util.List;
import java.util.Scanner;

public class Menue {
    public static void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Spiel starten");
            System.out.println("2. Spiel laden");
            System.out.println("3. Einstellungen");
            System.out.println("4. Spiel beenden");

            String eingabe = scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", "");

            switch (eingabe) {
                case "1":
                case "spielstarten":
                    Spieler.spielStarten(scanner);
                    return; // nach Spielstart nicht wieder ins Menü
                case "2":
                case "spielladen":
                    System.out.println("Spielstand wird geladen...");
                    List<Spieler> daten = SpeicherManager.spielLaden();
                    if (daten != null) {
                        System.out.println("Geladene Daten: " + daten);
                        // Hier später Logik zum Wiederherstellen der Spieler/Figuren einbauen
                    } else {
                        System.out.println("Kein gespeicherter Spielstand gefunden.");
                    }
                    break;

                case "3":
                case "einstellungen":
                    Einstellung.oeffneEinstellungen(scanner);
                    // danach wird die Schleife erneut ausgeführt → Menü wird wieder gezeigt
                    break;
                case "4":
                case "spielbeenden":
                    System.out.println("Spiel wird beendet.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ungültige Eingabe.");
            }
        }
    }
}
