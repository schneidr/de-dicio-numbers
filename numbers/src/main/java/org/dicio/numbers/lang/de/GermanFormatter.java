package org.dicio.numbers.lang.de;

import org.dicio.numbers.formatter.Formatter;
import org.dicio.numbers.unit.MixedFraction;
import org.dicio.numbers.util.Utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GermanFormatter extends Formatter {

    final Map<Long, String> NUMBER_NAMES = new HashMap<Long, String>() {{
        put(0L, "null");
        put(1L, "eins");
        put(2L, "zwei");
        put(3L, "drei");
        put(4L, "vier");
        put(5L, "fünf");
        put(6L, "sechs");
        put(7L, "sieben");
        put(8L, "acht");
        put(9L, "neun");
        put(10L, "zehn");
        put(11L, "elf");
        put(12L, "zwölf");
        put(13L, "dreizehn");
        put(14L, "vierzehn");
        put(15L, "fünfzehn");
        put(16L, "sechzehn");
        put(17L, "siebzehn");
        put(18L, "achtzehn");
        put(19L, "neunzehn");
        put(20L, "zwanzig");
        put(30L, "dreißig");
        put(40L, "vierzig");
        put(50L, "fünfzig");
        put(60L, "sechzig");
        put(70L, "siebzig");
        put(80L, "achtzig");
        put(90L, "neunzig");
        put(100L, "hundert");
        put(1000L, "tausend");
    }};
    
    final Map<Long, String> NUMBER_NAMES_SHORT_SCALE = new HashMap<Long, String>(NUMBER_NAMES) {{
        put(1000000L, "Million");
        put(1000000000L, "Milliarde");
        put(1000000000000L, "Billion");
        put(1000000000000000L, "Billiarde");
        put(1000000000000000000L, "Trillion");
    }};
    
    final Map<Long, String> NUMBER_NAMES_LONG_SCALE = new HashMap<Long, String>(NUMBER_NAMES) {{
        put(1000000000000L, "Billion");
        put(1000000000000000000L, "Trillion");
    }};
    
    
    final Map<Long, String> ORDINAL_NAMES = new HashMap<Long, String>() {{
        put(1L, "erste");
        put(2L, "zweite");
        put(3L, "dritte");
        put(4L, "vierte");
        put(5L, "fünfte");
        put(6L, "sechste");
        put(7L, "siebte");
        put(8L, "achte");
        put(9L, "neunte");
        put(10L, "zehnte");
        put(11L, "elfte");
        put(12L, "zwölfte");
        put(13L, "dreizehnte");
        put(14L, "vierzehnte");
        put(15L, "fünfzehnte");
        put(16L, "sechzehnte");
        put(17L, "siebzehnte");
        put(18L, "achtzehnte");
        put(19L, "neunzehnte");
        put(20L, "zwanzigste");
        put(30L, "dreißigste");
        put(40L, "vierzigste");
        put(50L, "fünfzigste");
        put(60L, "sechzigste");
        put(70L, "siebzigste");
        put(80L, "achtzigste");
        put(90L, "neunzigste");
        put(100L, "hundertste");
        put(1000L, "tausendste");
        put(1000000L, "Millionste");
    }};
    
    final Map<Long, String> ORDINAL_NAMES_SHORT_SCALE = new HashMap<Long, String>(ORDINAL_NAMES) {{
        put(1000000000L, "Milliardste");
        put(1000000000000L, "Billionste");
        put(1000000000000000L, "Billiardste");
        put(1000000000000000000L, "Trillionste");
    }};
    
    final Map<Long, String> ORDINAL_NAMES_LONG_SCALE = new HashMap<Long, String>(ORDINAL_NAMES) {{
        put(1000000000000L, "Billionste");
        put(1000000000000000000L, "Trillionste");
    }};

    public GermanFormatter() {
        super("config/de-de");
    }

    @Override
    public String niceNumber(final MixedFraction mixedFraction, final boolean speech) {
        if (speech) {
            final String sign = mixedFraction.negative ? "minus " : "";
            if (mixedFraction.numerator == 0) {
                return sign + pronounceNumber(mixedFraction.whole, 0, true, false, false);
            }

            String denominatorString;
            if (mixedFraction.denominator == 2) {
                denominatorString = "halb";
            } else if (mixedFraction.denominator == 4) {
                denominatorString = "viertel";
            } else {
                // use ordinal: only half and quarter are exceptions
                denominatorString
                        = pronounceNumber(mixedFraction.denominator, 0, true, false, true);
            }

            final String numeratorString;
            if (mixedFraction.numerator == 1) {
                numeratorString = "a";
            } else {
                numeratorString = pronounceNumber(mixedFraction.numerator, 0, true, false, false);
                denominatorString += "s";
            }

            if (mixedFraction.whole == 0) {
                return sign + numeratorString + " " + denominatorString;
            } else {
                return sign + pronounceNumber(mixedFraction.whole, 0, true, false, false)
                        + " und " + numeratorString + " " + denominatorString;
            }

        } else {
            return niceNumberNotSpeech(mixedFraction);
        }
    }

    @Override
    public String pronounceNumber(double number,
                                  final int places,
                                  final boolean shortScale,
                                  final boolean scientific,
                                  final boolean ordinal) {

        if (number == Double.POSITIVE_INFINITY) {
            return "unendlich";
        } else if (number == Double.NEGATIVE_INFINITY) {
            return "minus unendlich";
        } else if (Double.isNaN(number)) {
            return "keine Zahl";
        }

        // also using scientific mode if the number is too big to be spoken fully. Checking against
        // the biggest double smaller than 10^21 = 1000 * 10^18, which is the biggest pronounceable
        // number, since e.g. 999.99 * 10^18 can be pronounced correctly.
        if (scientific || Math.abs(number) > 999999999999999934463d) {
            final String scientificFormatted = String.format(Locale.ENGLISH, "%E", number);
            final String[] parts = scientificFormatted.split("E", 2);
            final double power = Integer.parseInt(parts[1]);

            if (power != 0) {
                // This handles negatives of powers separately from the normal
                // handling since each call disables the scientific flag
                final double n = Double.parseDouble(parts[0]);
                return String.format("%s%s mal zehn hoch %s%s",
                        n < 0 ? "minus " : "",
                        pronounceNumber(Math.abs(n), places, shortScale, false, false),
                        power < 0 ? "minus " : "",
                        pronounceNumber(Math.abs(power), places, shortScale, false, false));
            }
        }

        final StringBuilder result = new StringBuilder();
        if (number < 0) {
            number = -number;
            // from here on number is always positive
            if (places != 0 || number >= 0.5) {
                // do not add minus if number will be rounded to 0
                result.append(scientific ? "negative " : "minus ");
            }
        }

        final int realPlaces = Utils.decimalPlacesNoFinalZeros(number, places);
        final boolean numberIsWhole = realPlaces == 0;
        // if no decimal places to be printed, numberLong should be the rounded number
        final long numberLong = (long) number + (number % 1 >= 0.5 && numberIsWhole ? 1 : 0);

        if (!ordinal && numberIsWhole && numberLong > 1000 && numberLong < 2000) {
            // deal with 4 digits that can be said like a date, i.e. 1972 => nineteen seventy two

            result.append(NUMBER_NAMES.get(numberLong / 100));
            result.append(" ");
            if (numberLong % 100 == 0) {
                // 1900 => nineteen hundred
                result.append(NUMBER_NAMES.get(100L));
            } else if (numberLong % 100 < 10 && numberLong % 100 != 0) {
                // 1906 => nineteen oh six
                result.append("oh ");
                result.append(NUMBER_NAMES.get(numberLong % 10));
            } else if (numberLong % 10 == 0 || numberLong % 100 < 20) {
                // 1960 => nineteen sixty; 1911 => nineteen eleven
                result.append(NUMBER_NAMES.get(numberLong % 100));
            } else {
                // 1961 => nineteen sixty one
                result.append(NUMBER_NAMES.get(numberLong % 100 - numberLong % 10));
                result.append(" ");
                result.append(NUMBER_NAMES.get(numberLong % 10));
            }

            return result.toString();
        }

        if (!ordinal && NUMBER_NAMES.containsKey(numberLong)) {
            if (number > 90) {
                result.append("ein");
            }
            result.append(NUMBER_NAMES.get(numberLong));

        } else if (shortScale) {
            boolean ordi = ordinal && numberIsWhole; // not ordinal if not whole
            final List<Long> groups = Utils.splitByModulus(numberLong, 1000);
            final List<String> groupNames = new ArrayList<>();
            for (int i = 0; i < groups.size(); ++i) {
                final long z = groups.get(i);
                if (z == 0) {
                    continue; // skip 000 groups
                }
                String groupName = subThousand(z, i == 0 && ordi);

                if (i != 0) {
                    final long magnitude = Utils.longPow(1000, i);
                    if (ordi) {
                        // ordi can be true only for the first group (i.e. at the end of the number)
                        if (z == 1) {
                            // remove "one" from first group (e.g. "one billion, millionth")
                            groupName = ORDINAL_NAMES_SHORT_SCALE.get(magnitude);
                        } else {
                            groupName += " " + ORDINAL_NAMES_SHORT_SCALE.get(magnitude);
                        }
                    } else {
                        String name = NUMBER_NAMES_SHORT_SCALE.get(magnitude);
                        if (z > 1) {
                            name += (name.endsWith("e") ? "n" : "en");
                        }
                        groupName += " " + name;
                    }
                }

                groupNames.add(groupName);
                ordi = false;
            }

            appendSplitGroups(result, groupNames);

        } else {
            boolean ordi = ordinal && numberIsWhole; // not ordinal if not whole
            final List<Long> groups = Utils.splitByModulus(numberLong, 1000000);
            final List<String> groupNames = new ArrayList<>();
            for (int i = 0; i < groups.size(); ++i) {
                final long z = groups.get(i);
                if (z == 0) {
                    continue; // skip 000000 groups
                }

                String groupName;
                if (z < 1000) {
                    groupName = subThousand(z, i == 0 && ordi);
                } else {
                    groupName = subThousand(z / 1000, false) + "tausend";
                    if (z % 1000 != 0) {
                        groupName += (i == 0 ? ", " : " ") + subThousand(z % 1000, i == 0 && ordi);
                    } else if (i == 0 && ordi) {
                        if (z / 1000 == 1) {
                            groupName = "tausendste";
                        } else {
                            groupName += "ste";
                        }
                    }
                }

                if (i != 0) {
                    final long magnitude = Utils.longPow(1000000, i);
                    if (ordi) {
                        // ordi can be true only for the first group (i.e. at the end of the number)
                        if (z == 1) {
                            // remove "one" from first group (e.g. "one billion, millionth")
                            groupName = ORDINAL_NAMES_LONG_SCALE.get(magnitude);
                        } else {
                            groupName += " " + ORDINAL_NAMES_LONG_SCALE.get(magnitude);
                        }
                    } else {
                        groupName += " " + NUMBER_NAMES_LONG_SCALE.get(magnitude);
                    }
                }

                groupNames.add(groupName);
                ordi = false;
            }

            appendSplitGroups(result, groupNames);
        }

        if (realPlaces > 0) {
            if (number < 1.0 && (result.length() == 0 || "minus ".contentEquals(result))) {
                result.append("zero"); // nothing was written before
            }
            result.append(" Komma");

            final String fractionalPart = String.format("%." + realPlaces + "f", number % 1);
            for (int i = 2; i < fractionalPart.length(); ++i) {
                result.append(" ");
                result.append(NUMBER_NAMES.get((long) (fractionalPart.charAt(i) - '0')));
            }
        }

        return result.toString();
    }

    @Override
    public String niceTime(final LocalTime time,
                           final boolean speech,
                           final boolean use24Hour,
                           final boolean showAmPm) {
        if (speech) {
            if (use24Hour) {
                final StringBuilder result = new StringBuilder();
                if (time.getHour() < 10) {
                    result.append("zero ");
                }
                result.append(pronounceNumberDuration(time.getHour()));

                result.append(" ");
                if (time.getMinute() == 0) {
                    result.append("hundred");
                } else {
                    if (time.getMinute() < 10) {
                        result.append("zero ");
                    }
                    result.append(pronounceNumberDuration(time.getMinute()));
                }

                return result.toString();
            } else {
                if (time.getHour() == 0 && time.getMinute() == 0) {
                    return "midnight";
                } else if (time.getHour() == 12 && time.getMinute() == 0) {
                    return "noon";
                }

                final int normalizedHour = (time.getHour() + 11) % 12 + 1; // 1 to 12
                final StringBuilder result = new StringBuilder();
                if (time.getMinute() == 15) {
                    result.append("quarter past ");
                    result.append(pronounceNumberDuration(normalizedHour));
                } else if (time.getMinute() == 30) {
                    result.append("half past ");
                    result.append(pronounceNumberDuration(normalizedHour));
                } else if (time.getMinute() == 45) {
                    result.append("quarter to ");
                    result.append(pronounceNumberDuration(normalizedHour % 12 + 1));
                } else {
                    result.append(pronounceNumberDuration(normalizedHour));

                    if (time.getMinute() == 0) {
                        if (!showAmPm) {
                            return result + " o'clock";
                        }
                    } else {
                        if (time.getMinute() < 10) {
                            result.append(" oh");
                        }
                        result.append(" ");
                        result.append(pronounceNumberDuration(time.getMinute()));
                    }
                }

                if (showAmPm) {
                    result.append(time.getHour() >= 12 ? " p.m." : " a.m.");
                }
                return result.toString();
            }

        } else {
            if (use24Hour) {
                return time.format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH));
            } else {
                final String result = time.format(DateTimeFormatter.ofPattern(
                        showAmPm ? "K:mm a" : "K:mm", Locale.ENGLISH));
                if (result.startsWith("0:")) {
                    return "12:" + result.substring(2);
                } else {
                    return result;
                }
            }
        }
    }


    /**
     * @param n must be 0 <= n <= 999
     * @param ordinal whether to return an ordinal number (usually with -th)
     * @return the string representation of a number smaller than 1000
     */
    private String subThousand(final long n, final boolean ordinal) {
        // this function calls itself inside if branches to make sure `ordinal` is respected
        if (ordinal && ORDINAL_NAMES.containsKey(n)) {
            return ORDINAL_NAMES.get(n);
        } else if (n < 100) {
            if (!ordinal && NUMBER_NAMES.containsKey(n)) {
                return NUMBER_NAMES.get(n);
            }
            // n is surely => 20 from here on, since all n < 20 are in (ORDINAL|NUMBER)_NAMES

            return (n % 10 > 0 ? subThousand(n % 10, false) : "") + "und" + subThousand(n - n % 10, ordinal);
        } else {
            return NUMBER_NAMES.get(n / 100) + "hundert"
                    + (n % 100 > 0 ? subThousand(n % 100, ordinal)
                    : (ordinal ? "th" : ""));
        }
    }

    /**
     * @param result the string builder to append the comma-separated group names to
     * @param groupNames the group names
     */
    private void appendSplitGroups(final StringBuilder result, final List<String> groupNames) {
        if (!groupNames.isEmpty()) {
            result.append(groupNames.get(groupNames.size() - 1));
        }

        for (int i = groupNames.size() - 2; i >= 0; --i) {
            result.append(", ");
            result.append(groupNames.get(i));
        }
    }
}
