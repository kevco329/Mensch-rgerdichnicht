package Menschaergerdichnicht;

import java.util.Random;

public class Wuerfel {
    public static int wuerfeln() {
        Random rand = new Random();
        return rand.nextInt(6) + 1;
    }
}