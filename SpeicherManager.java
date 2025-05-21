package Menschaergerdichnicht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SpeicherManager {

	public static void spielSpeichern(List<Spieler> spielerListe, int spielfeldGroesse) {
	    try (PrintWriter writer = new PrintWriter("spielstand.txt")) {
	        writer.println(spielerListe.size());
	        writer.println(spielfeldGroesse);
	        for (Spieler spieler : spielerListe) {
	            writer.println(spieler.getName());
	        }
	        for (Spieler spieler : spielerListe) {
	            for (Figur figur : spieler.getFiguren()) {
	                writer.print(figur.getPosition() + " ");
	            }
	            writer.println();
	        }
	        System.out.println("Spiel erfolgreich gespeichert!");
	    } catch (Exception e) {
	        System.out.println("Fehler beim Speichern: " + e.getMessage());
	    }
	}

	// LADEN - liest alle Spieler wieder aus der Textdatei
	public static List<Spieler> spielLaden() {
	    List<Spieler> spielerListe = new ArrayList<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader("spielstand.txt"))) {
	        int anzahlSpieler = Integer.parseInt(reader.readLine());
	        int spielfeldGroesse = Integer.parseInt(reader.readLine());
	        List<String> namen = new ArrayList<>();
	        for (int i = 0; i < anzahlSpieler; i++) {
	            namen.add(reader.readLine());
	        }
	        List<int[]> figurenListen = new ArrayList<>();
	        for (int i = 0; i < anzahlSpieler; i++) {
	            String[] posStrings = reader.readLine().trim().split(" ");
	            int[] figuren = new int[4];
	            for (int j = 0; j < 4; j++) {
	                figuren[j] = Integer.parseInt(posStrings[j]);
	            }
	            figurenListen.add(figuren);
	        }
	        // Spieler-Objekte erzeugen (du brauchst dafür einen passenden Konstruktor in Spieler!)
	        for (int i = 0; i < anzahlSpieler; i++) {
	            spielerListe.add(new Spieler(namen.get(i), figurenListen.get(i)));
	        }
	        System.out.println("Spielstand erfolgreich geladen!");
	    } catch (Exception e) {
	        System.out.println("Fehler beim Laden: " + e.getMessage());
	    }
	    return spielerListe;
	}
}
