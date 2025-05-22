package Menschaergerdichnicht;

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
                    return; 

                case "2":
                case "spielladen":
                    System.out.println("Spielstand wird geladen...");
                    int aktuellerSpieler = Spieler.spielLaden();
                    Spieler.spielFortsetzen(aktuellerSpieler, scanner);
                    return; 
                case "3":
                case "einstellungen":
                    Einstellung.oeffneEinstellungen(scanner);
                   
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