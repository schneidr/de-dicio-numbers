package org.dicio.numbers.lang.de;

import org.dicio.numbers.ParserFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.T;
import static org.junit.Assert.assertEquals;

public class PronounceNumberTest {

    private static ParserFormatter pf;

    @BeforeClass
    public static void setup() {
        pf = new ParserFormatter(new GermanFormatter(), null);
    }

    @Test
    public void smallIntegers() {
        assertEquals("null", pf.pronounceNumber(0).get());
        assertEquals("eins", pf.pronounceNumber(1).get());
        assertEquals("zehn", pf.pronounceNumber(10).get());
        assertEquals("fünfzehn", pf.pronounceNumber(15).get());
        assertEquals("zwanzig", pf.pronounceNumber(20).get());
        assertEquals("siebenundzwanzig", pf.pronounceNumber(27).get());
        assertEquals("dreißig", pf.pronounceNumber(30).get());
        assertEquals("dreiunddreißig", pf.pronounceNumber(33).get());
    }

    @Test
    public void negativeSmallIntegers() {
        assertEquals("minus eins", pf.pronounceNumber(-1).get());
        assertEquals("minus zehn", pf.pronounceNumber(-10).get());
        assertEquals("minus fünfzehn", pf.pronounceNumber(-15).get());
        assertEquals("minus zwanzig", pf.pronounceNumber(-20).get());
        assertEquals("minus siebenundzwanzig", pf.pronounceNumber(-27).get());
        assertEquals("minus dreißig", pf.pronounceNumber(-30).get());
        assertEquals("minus dreiunddreißig", pf.pronounceNumber(-33).get());
    }

    @Test
    public void decimals() {
        assertEquals("null Komma null fünf", pf.pronounceNumber(0.05).get());
        assertEquals("minus null Komma null fünf", pf.pronounceNumber(-0.05).get());
        assertEquals("eins Komma zwei drei vier", pf.pronounceNumber(1.234).get());
        assertEquals("einundzwanzig Komma zwei sechs vier", pf.pronounceNumber(21.264).places(5).get());
        assertEquals("einundzwanzig Komma zwei sechs vier", pf.pronounceNumber(21.264).places(4).get());
        assertEquals("einundzwanzig Komma zwei sechs vier", pf.pronounceNumber(21.264).places(3).get());
        assertEquals("einundzwanzig Komma zwei sechs", pf.pronounceNumber(21.264).places(2).get());
        assertEquals("einundzwanzig Komma drei", pf.pronounceNumber(21.264).places(1).get());
        assertEquals("einundzwanzig", pf.pronounceNumber(21.264).places(0).get());
        assertEquals("minus einundzwanzig Komma zwei sechs vier", pf.pronounceNumber(-21.264).places(5).get());
        assertEquals("minus einundzwanzig Komma zwei sechs vier", pf.pronounceNumber(-21.264).places(4).get());
        assertEquals("minus einundzwanzig Komma zwei sechs vier", pf.pronounceNumber(-21.264).places(3).get());
        assertEquals("minus einundzwanzig Komma zwei sechs", pf.pronounceNumber(-21.264).places(2).get());
        assertEquals("minus einundzwanzig Komma drei", pf.pronounceNumber(-21.264).places(1).get());
        assertEquals("minus einundzwanzig", pf.pronounceNumber(-21.264).places(0).get());
    }

    @Test
    public void roundingDecimals() {
        assertEquals("null", pf.pronounceNumber(0.05).places(0).get());
        assertEquals("null", pf.pronounceNumber(-0.4).places(0).get());
        assertEquals("minus zweiundzwanzig", pf.pronounceNumber(-21.7).places(0).get());
        assertEquals("neunundachtzig", pf.pronounceNumber(89.2).places(0).get());
        assertEquals("neunzig", pf.pronounceNumber(89.9).places(0).get());
        assertEquals("minus eins", pf.pronounceNumber(-0.5).places(0).get());
        assertEquals("null", pf.pronounceNumber(-0.4).places(0).get());
        assertEquals("sechs Komma drei", pf.pronounceNumber(6.28).places(1).get());
        assertEquals("minus drei Komma eins", pf.pronounceNumber(-3.14).places(1).get());
        // Beachten Sie: 3,15 ergibt nicht "drei Komma zwei", wegen der Fließkommavariationen
        assertEquals("drei Komma zwei", pf.pronounceNumber(3.150001).places(1).get());
        assertEquals("null Komma drei", pf.pronounceNumber(0.25).places(1).get());
        assertEquals("minus null Komma drei", pf.pronounceNumber(-0.25).places(1).get());
        assertEquals("neunzehn", pf.pronounceNumber(19.004).get());
    }

    @Test
    public void hundred() {
        assertEquals("einhundert", pf.pronounceNumber(100).get());
        assertEquals("sechshundertachtundsiebzig", pf.pronounceNumber(678).get());

        assertEquals("einhundertdrei Millionen, zweihundertvierundfünfzigtausendsechshundertvierundfünfzig",
                pf.pronounceNumber(103254654).get());
        assertEquals("eine Million, fünfhundertzwölftausendvierhundertsiebenundfünfzig",
                pf.pronounceNumber(1512457).get());
        assertEquals("zweihundertneuntausendneunhundertsechsundneunzig",
                pf.pronounceNumber(209996).get());
    }

    @Test
    public void year() {
        assertEquals("vierzehnhundertsechsundfünfzig", pf.pronounceNumber(1456).get());
        assertEquals("neunzehnhundertvierundachtzig", pf.pronounceNumber(1984).get());
        assertEquals("achtzehnhunderteins", pf.pronounceNumber(1801).get());
        assertEquals("elfhundert", pf.pronounceNumber(1100).get());
        assertEquals("zwölfhunderteins", pf.pronounceNumber(1201).get());
        assertEquals("fünfzehnhundertzehn", pf.pronounceNumber(1510).get());
        assertEquals("zehnhundertsechs", pf.pronounceNumber(1006).get());
        assertEquals("eintausend", pf.pronounceNumber(1000).get());
        assertEquals("zweitausend", pf.pronounceNumber(2000).get());
        assertEquals("zweitausendfünfzehn", pf.pronounceNumber(2015).get());
        assertEquals("vierthausendachthundertsiebenundzwanzig", pf.pronounceNumber(4827).get());
    }

    @Test
    public void scientificNotation() {
        assertEquals("null", pf.pronounceNumber(0.0).scientific(T).get());
        assertEquals("drei Komma drei mal zehn hoch eins",
                pf.pronounceNumber(33).scientific(T).get());
        assertEquals("zwei Komma neun neun mal zehn hoch acht",
                pf.pronounceNumber(299492458).scientific(T).get());
        assertEquals("zwei Komma neun neun sieben neun zwei fünf mal zehn hoch acht",
                pf.pronounceNumber(299792458).scientific(T).places(6).get());
        assertEquals("eins Komma sechs sieben zwei mal zehn hoch minus siebenundzwanzig",
                pf.pronounceNumber(1.672e-27).scientific(T).places(3).get());

        // Automatische wissenschaftliche Notation, wenn die Zahl zu groß ist, um ausgesprochen zu werden
        assertEquals("zwei Komma neun fünf mal zehn hoch vierundzwanzig",
                pf.pronounceNumber(2.9489e24).get());
    }

    private void assertShortLongScale(final double number,
                                      final String shortScale,
                                      final String longScale) {
        assertEquals(shortScale, pf.pronounceNumber(number).shortScale(T).get());
        assertEquals(longScale, pf.pronounceNumber(number).shortScale(F).get());
    }

    @Test
    public void largeNumbers() {
        assertShortLongScale(1001892,
                "eine Million, eintausendachthundertzweiundneunzig",
                "eine Million, eintausendachthundertzweiundneunzig");
        assertShortLongScale(299792458,
                "zweihundertneunundneunzig Millionen, siebenhundertzweiundneunzigtausendvierhundertachtundfünfzig",
                "zweihundertneunundneunzig Millionen, siebenhundertzweiundneunzigtausendvierhundertachtundfünfzig");
        assertShortLongScale(-100202133440.0,
                "minus einhundert Milliarden, zweihundertzwei Millionen, einhundertdreiunddreißigtausendvierhundertundvierzig",
                "minus einhunderttausendzweihundertzwei Millionen, einhundertdreiunddreißigtausendvierhundertundvierzig");
        assertShortLongScale(20102000987000.0,
                "zwanzig Billionen, einhundertzwei Milliarden, neunhundertsiebenundachtzigtausend",
                "zwanzig Milliarden, eintausendzwei Millionen, neunhundertsiebenundachtzigtausend");
        assertShortLongScale(-2061000560007060.0,
                "minus zwei Billiarden, einundsechzig Billionen, fünfhundertsechzig Millionen, siebentausendsechzig",
                "minus zweitausendneunundsechzig Milliarden, fünfhundertsechzig Millionen, siebentausendsechzig");
        assertShortLongScale(9111202032999999488.0, // Fließkommavariationen
                "neun Quintillionen, einhundertelf Billionen, zweihundertzwei Billionen, zweiunddreißig Milliarden, neunhundertneunundneunzig Millionen, neunhundertneunundneunzigtausend, vierhundertachtundachtzig",
                "neun Billionen, einhundertelftausendzweihundertzwei Milliarden, zweiunddreißigtausendneunhundertneunundneunzig Millionen, neunhundertneunundneunzigtausend, vierhundertachtundachtzig");

        assertShortLongScale(29000.0, "neunundzwanzigtausend", "neunundzwanzigtausend");
        assertShortLongScale(301000.0, "dreihunderteintausend", "dreihunderteintausend");
        assertShortLongScale(4000000.0, "vier Millionen", "vier Millionen");
        assertShortLongScale(50000000.0, "fünfzig Millionen", "fünfzig Millionen");
        assertShortLongScale(630000000.0, "sechshundertdreißig Millionen", "sechshundertdreißig Millionen");
        assertShortLongScale(7000000000.0, "sieben Milliarden", "siebentausend Millionen");
        assertShortLongScale(16000000000.0, "sechzehn Milliarden", "sechzehntausend Millionen");
        assertShortLongScale(923000000000.0, "neunhundertdreiundzwanzig Milliarden", "neunhundertdreiundzwanzigtausend Millionen");
        assertShortLongScale(1000000000000.0, "eine Billion", "eine Milliarde");
        assertShortLongScale(29000000000000.0, "neunundzwanzig Billionen", "neunundzwanzig Milliarden");
        assertShortLongScale(308000000000000.0, "dreihundertacht Billionen", "dreihundertacht Milliarden");
        assertShortLongScale(4000000000000000.0, "vier Billiarden", "viertausend Milliarden");
        assertShortLongScale(52000000000000000.0, "zweiundfünfzig Billiarden", "zweiundfünfzigtausend Milliarden");
        assertShortLongScale(640000000000000000.0, "sechshundertvierzig Billiarden", "sechshundertvierzigtausend Milliarden");
        assertShortLongScale(7000000000000000000.0, "sieben Quintillionen", "sieben Billionen");

        // TODO eventuell verbessern
        assertShortLongScale(1000001, "eine Million, eins", "eine Million, eins");
        assertShortLongScale(-2000000029, "minus zwei Milliarden, neunundzwanzig", "minus zweitausend Millionen, neunundzwanzig");
    }

    @Test
    public void ordinal() {
        // kleine Zahlen
        assertEquals("erste", pf.pronounceNumber(1).shortScale(T).ordinal(T).get());
        assertEquals("erste", pf.pronounceNumber(1).shortScale(F).ordinal(T).get());
        assertEquals("zehnte", pf.pronounceNumber(10).shortScale(T).ordinal(T).get());
        assertEquals("zehnte", pf.pronounceNumber(10).shortScale(F).ordinal(T).get());
        assertEquals("fünfzehnte", pf.pronounceNumber(15).shortScale(T).ordinal(T).get());
        assertEquals("fünfzehnte", pf.pronounceNumber(15).shortScale(F).ordinal(T).get());
        assertEquals("zwanzigste", pf.pronounceNumber(20).shortScale(T).ordinal(T).get());
        assertEquals("zwanzigste", pf.pronounceNumber(20).shortScale(F).ordinal(T).get());
        assertEquals("siebenundzwanzigste", pf.pronounceNumber(27).shortScale(T).ordinal(T).get());
        assertEquals("siebenundzwanzigste", pf.pronounceNumber(27).shortScale(F).ordinal(T).get());
        assertEquals("dreißigste", pf.pronounceNumber(30).shortScale(T).ordinal(T).get());
        assertEquals("dreißigste", pf.pronounceNumber(30).shortScale(F).ordinal(T).get());
        assertEquals("dreiunddreißigste", pf.pronounceNumber(33).shortScale(T).ordinal(T).get());
        assertEquals("dreiunddreißigste", pf.pronounceNumber(33).shortScale(F).ordinal(T).get());
        assertEquals("hundertste", pf.pronounceNumber(100).shortScale(T).ordinal(T).get());
        assertEquals("hundertste", pf.pronounceNumber(100).shortScale(F).ordinal(T).get());
        assertEquals("tausendste", pf.pronounceNumber(1000).shortScale(T).ordinal(T).get());
        assertEquals("tausendste", pf.pronounceNumber(1000).shortScale(F).ordinal(T).get());
        assertEquals("zehntausendste", pf.pronounceNumber(10000).shortScale(T).ordinal(T).get());
        assertEquals("zehntausendste", pf.pronounceNumber(10000).shortScale(F).ordinal(T).get());
        assertEquals("zweihundertste", pf.pronounceNumber(200).shortScale(T).ordinal(T).get());
        assertEquals("zweihundertste", pf.pronounceNumber(200).shortScale(F).ordinal(T).get());
        assertEquals("achtzehntausendsechshunderteinundneunzigste", pf.pronounceNumber(18691).ordinal(T).shortScale(T).get());
        assertEquals("achtzehntausendsechshunderteinundneunzigste", pf.pronounceNumber(18691).ordinal(T).shortScale(F).get());
        assertEquals("eintausendfünfhundertsiebenundsechzigste", pf.pronounceNumber(1567).ordinal(T).shortScale(T).get());
        assertEquals("eintausendfünfhundertsiebenundsechzigste", pf.pronounceNumber(1567).ordinal(T).shortScale(F).get());

        // große Zahlen
        assertEquals("achtzehn millionste", pf.pronounceNumber(18000000).ordinal(T).get());
        assertEquals("achtzehn millionenhundertste", pf.pronounceNumber(18000100).ordinal(T).get());
        assertEquals("einhundertsiebenundzwanzig milliardste", pf.pronounceNumber(127000000000.0).ordinal(T).shortScale(T).get());
        assertEquals("zweihunderteintausend millionste", pf.pronounceNumber(201000000000.0).ordinal(T).shortScale(F).get());
        assertEquals("neunhundertdreizehn milliardenachtzig millionensechshunderttausendvierundsechzigste", pf.pronounceNumber(913080600064.0).ordinal(T).shortScale(T).get());
        assertEquals("neunhundertdreizehntausendachtzig millionensechshunderttausendvierundsechzigste", pf.pronounceNumber(913080600064.0).ordinal(T).shortScale(F).get());
        assertEquals("eine billionzweimillionste", pf.pronounceNumber(1000002000000.0).ordinal(T).shortScale(T).get());
        assertEquals("eine milliardezweimillionste", pf.pronounceNumber(1000002000000.0).ordinal(T).shortScale(F).get());
        assertEquals("vier billionenmillionste", pf.pronounceNumber(4000001000000.0).ordinal(T).shortScale(T).get());
        assertEquals("vier milliardemillionste", pf.pronounceNumber(4000001000000.0).ordinal(T).shortScale(F).get());

        // Dezimalzahlen und wissenschaftliche Notation: Das Verhalten sollte dasselbe sein wie mit ordinal=F
        assertEquals("zwei Komma sieben acht", pf.pronounceNumber(2.78).ordinal(T).get());
        assertEquals("dritte", pf.pronounceNumber(2.78).places(0).ordinal(T).get());
        assertEquals("neunzehnte", pf.pronounceNumber(19.004).ordinal(T).get());
        assertEquals("acht hundert und dreißig millionen, vier hundert und acht und dreißig tausend, zweiundneunzig komma eins acht drei", pf.pronounceNumber(830438092.1829).places(3).ordinal(T).get());
        assertEquals("zwei komma fünf vier mal zehn hoch sechs", pf.pronounceNumber(2.54e6).ordinal(T).scientific(T).get());
    }

    @Test
    public void edgeCases() {
        assertEquals("null", pf.pronounceNumber(0.0).get());
        assertEquals("null", pf.pronounceNumber(-0.0).get());
        assertEquals("unendlich", pf.pronounceNumber(Double.POSITIVE_INFINITY).get());
        assertEquals("minus unendlich", pf.pronounceNumber(Double.NEGATIVE_INFINITY).scientific(F).get());
        assertEquals("minus unendlich", pf.pronounceNumber(Double.NEGATIVE_INFINITY).scientific(T).get());
        assertEquals("keine Zahl", pf.pronounceNumber(Double.NaN).get());
    }
}
