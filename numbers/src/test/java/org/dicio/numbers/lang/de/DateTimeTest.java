package org.dicio.numbers.lang.de;

import org.dicio.numbers.formatter.Formatter;
import org.dicio.numbers.test.DateTimeTestBase;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class DateTimeTest extends DateTimeTestBase {

    @Override
    public String configFolder() {
        return "config/de-de";
    }

    @Override
    public Formatter buildNumberFormatter() {
        return new GermanFormatter();
    }

    @Test
    public void testNiceDate() {
        // just check that the NumberParserFormatter functions do their job
        assertEquals("Mittwoch, der Achtundzwanzigste April zweitausend ein und zwanzig",
                pf.niceDate(LocalDate.of(2021, 4, 28)).get());
        assertEquals("Sonntag, der Dreizehnte August",
                pf.niceDate(LocalDate.of(-84, 8, 13)).now(LocalDate.of(-84, 8, 23)).get());
    }

    @Test
    public void testNiceYear() {
        // just check that the NumberParserFormatter functions do their job
        assertEquals("neunzehn hundert vier und achtzig", pf.niceYear(LocalDate.of(1984, 4, 28)).get());
        assertEquals("acht hundert zehn v. Chr.", pf.niceYear(LocalDate.of(-810, 8, 13)).get());
    }

    @Test
    public void testNiceDateTime() {
        // just check that the NumberParserFormatter functions do their job
        assertEquals("wednesday, september twelfth, seventeen sixty four at noon", pf.niceDateTime(LocalDateTime.of(1764, 9, 12, 12, 0)).get());
        assertEquals("thursday, november third, three hundred twenty eight b.c. at five oh seven", pf.niceDateTime(LocalDateTime.of(-328, 11, 3, 5, 7)).get());
    }
}
