package Menschaergerdichnicht;
import java.util.Scanner;
public class Menue {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Spiel starten");
        System.out.println("2.Spiel laden");
        System.out.println("3.Einstellungen");
        System.out.println("4.Spiel beenden");
        
        String eingabe = scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", "");

        
        switch (eingabe) {
        	case "1":
        	case "spielstarten":
        	
        		System.out.println("Spiel wird gestartet");
        		System.out.println("Wie viele Spieler?");
        		int SpielerAnzahl = scanner.nextInt();
        		System.out.println("Es spielen " + SpielerAnzahl + " mit.");
        		break;
        	case "2":
        	case "spielladen":
        		System.out.println("Spiel wird geladen");
        		break;
        	case "3":
        	case "einstellungen":
        		System.out.println("Einstellungen werden geöffnet");
        		break;
        	case "4":
        	case "spielbeenden":
        		System.out.println("Spiel wird beendet");
        		break;
        	default:
        		System.out.println("Ungültige Eingabe");
        }
        scanner.close();
	}
}