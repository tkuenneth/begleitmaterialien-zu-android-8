package com.thomaskuenneth.tierkreiszeichen;

import android.util.SparseArray;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Diese Klasse bildet die 12 Tierkreiszeichen
 * in eine {@link HashMap} ab,
 * deren Schlüssel der Monat ist. Die Datumsangaben
 * stellen Mittelwerte dar und beziehen sich auf ein
 * Jahr mit 365 Tagen. Schaltjahre sind in den Datierungen
 * der Tierkreiszeichen also nicht berücksichtigt.
 *
 * @author Thomas Künneth
 */
final class Zodiak {

    private static final Zodiak INSTANCE = new Zodiak();

    private final SparseArray<Tierkreiszeichen> a;

    private Zodiak() {
        a = new SparseArray<>();
        a.put(Calendar.JANUARY,
                new Tierkreiszeichen(21, Calendar.JANUARY,
                        R.string.aquarius));
        a.put(Calendar.FEBRUARY,
                new Tierkreiszeichen(20, Calendar.FEBRUARY,
                        R.string.pisces));
        a.put(Calendar.MARCH, new Tierkreiszeichen(21, Calendar.MARCH,
                R.string.aries));
        a.put(Calendar.APRIL, new Tierkreiszeichen(21, Calendar.APRIL,
                R.string.taurus));
        a.put(Calendar.MAY, new Tierkreiszeichen(22, Calendar.MAY,
                R.string.gemini));
        a.put(Calendar.JUNE, new Tierkreiszeichen(22, Calendar.JUNE,
                R.string.cancer));
        a.put(Calendar.JULY,
                new Tierkreiszeichen(24, Calendar.JULY,
                        R.string.leo));
        a.put(Calendar.AUGUST,
                new Tierkreiszeichen(24, Calendar.AUGUST,
                        R.string.virgo));
        a.put(Calendar.SEPTEMBER, new Tierkreiszeichen(24,
                Calendar.SEPTEMBER, R.string.libra));
        a.put(Calendar.OCTOBER,
                new Tierkreiszeichen(24, Calendar.OCTOBER,
                        R.string.scorpius));
        a.put(Calendar.NOVEMBER,
                new Tierkreiszeichen(23, Calendar.NOVEMBER,
                        R.string.sagittarius));
        a.put(Calendar.DECEMBER,
                new Tierkreiszeichen(22, Calendar.DECEMBER,
                        R.string.capricornus));
    }

    /**
     * Liefert eine Referenz auf ein Element der
     * Hashtable, die das Sternzeichen des übergebenen
     * Monats repräsentiert.
     *
     * @param monat Monat, zum Beispiel {@code Calendar.JUNE}
     * @return Instanz eines {@link Tierkreiszeichen}s
     */
    static Tierkreiszeichen getTierkreiszeichenFuerMonat(
            int monat) {
        return INSTANCE.a.get(monat);
    }
}
