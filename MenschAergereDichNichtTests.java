package Menschaergerdichnicht;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

class MenschAergereDichNichtTests {

    // ---- Spielfeld.java ----
    @Test
    void Spielfeld_hatSpielerGewonnen_true() {
        int[] figuren = {40, 39, 38, 37};
        int[] zielFelder = {40, 39, 38, 37};
        // Annahme: ZielFelder werden so gesetzt
        Menschaergerdichnicht.Spieler.zielFelder = new int[][]{zielFelder};
        assertTrue(Menschaergerdichnicht.Spielfeld.hatSpielerGewonnen(figuren, 0));
    }

    @Test
    void Spielfeld_hatSpielerGewonnen_false() {
        int[] figuren = {40, -1, 38, 37};
        int[] zielFelder = {40, 39, 38, 37};
        Menschaergerdichnicht.Spieler.zielFelder = new int[][]{zielFelder};
        assertFalse(Menschaergerdichnicht.Spielfeld.hatSpielerGewonnen(figuren, 0));
    }

    // ---- Spieler.java ----
    @Test
    void Spieler_wuerfelnImBereich() {
        for (int i = 0; i < 100; i++) {
            int wurf = Menschaergerdichnicht.Spieler.wuerfeln();
            assertTrue(wurf >= 1 && wurf <= 6);
        }
    }

    @Test
    void Spieler_getStartposition() {
        Menschaergerdichnicht.Spieler.startPositionen = new int[]{0, 10, 20};
        assertEquals(10, Menschaergerdichnicht.Spieler.getStartposition(1));
    }

    // ---- Wuerfel.java ----
    @Test
    void Wuerfel_wuerfelnImBereich() {
        for (int i = 0; i < 100; i++) {
            int wurf = Menschaergerdichnicht.Wuerfel.wuerfeln();
            assertTrue(wurf >= 1 && wurf <= 6);
        }
    }

    // ---- SpeicherManager.java ----
    @Test
    void SpeicherManager_speichernUndLaden() {
        List<Menschaergerdichnicht.Spieler> spielerListe = Arrays.asList(
            new Menschaergerdichnicht.Spieler("A", new int[]{-1, -1, -1, -1}),
            new Menschaergerdichnicht.Spieler("B", new int[]{0, 1, 2, 3})
        );
        int groesse = 40;
        int aktueller = 1;
        Menschaergerdichnicht.SpeicherManager.spielSpeichern(spielerListe, groesse, aktueller);

        Menschaergerdichnicht.SpeicherManager.LoadedGame geladen = Menschaergerdichnicht.SpeicherManager.spielLaden();
        assertEquals(groesse, geladen.spielfeldGroesse);
        assertEquals(aktueller, geladen.aktuellerSpielerIndex);
        assertEquals(2, geladen.spielerListe.size());
        assertEquals("A", geladen.spielerListe.get(0).getName());
        assertEquals(-1, geladen.spielerListe.get(0).getFiguren().get(0).getPosition());
    }

    // ---- Menue.java ----
    // UI-Logik ist schwer automatisiert zu testen
    // Hier kann man höchstens prüfen, ob Spielstart & Laden Methoden ohne Fehler aufrufbar sind
    // oder mit Mocks arbeiten (fortgeschritten).

}