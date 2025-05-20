package Menschaergerdichnicht;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SpeicherManager {

    public static void spielSpeichern(List<Spieler> spielerListe) {
        String url = "jdbc:sqlite:Menschaergerdichnicht.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String datum = LocalDateTime.now().toString();
                int spieleranzahl = spielerListe.size();
                StringBuilder daten = new StringBuilder();

                for (Spieler spieler : spielerListe) {
                    daten.append(spieler.getName()).append(":");
                    for (Figur figur : spieler.getFiguren()) {
                        daten.append(figur.getPosition()).append(",");
                    }
                    daten.append("|"); // Trenner zwischen Spielern
                }

                String sql = "INSERT INTO Spielstand (datum, spieleranzahl, daten) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, datum);
                    pstmt.setInt(2, spieleranzahl);
                    pstmt.setString(3, daten.toString());
                    pstmt.executeUpdate();
                }

                System.out.println("Spiel erfolgreich gespeichert.");
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Speichern des Spiels: " + e.getMessage());
        }
    }

    // NEU: Spielstand laden (den letzten Eintrag aus der Datenbank)
    public static String spielLaden() {
        String url = "jdbc:sqlite:Menschaergerdichnicht.db";
        String sql = "SELECT * FROM Spielstand ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String datum = rs.getString("datum");
                int spieleranzahl = rs.getInt("spieleranzahl");
                String daten = rs.getString("daten");

                System.out.println("Spielstand vom " + datum + " mit " + spieleranzahl + " Spielern geladen.");
                return daten;
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Laden des Spiels: " + e.getMessage());
        }

        return null;
    }
}
