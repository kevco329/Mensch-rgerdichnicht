package Menschaergerdichnicht;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBVerbindung.verbinden();
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
