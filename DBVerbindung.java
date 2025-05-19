package Menschaergerdichnicht;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBVerbindung {
    private static final String DB_URL = "jdbc:sqlite:Menschaergerdichnicht.db";

    public static Connection verbinden() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DB_URL);
            System.out.println("Verbindung zur Datenbank erfolgreich.");
            return conn;
        } catch (Exception e) {
            System.out.println("Fehler beim Verbinden zur Datenbank:");
            e.printStackTrace();
            return null;
        }
    }
}
