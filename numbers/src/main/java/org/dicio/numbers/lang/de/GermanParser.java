package org.dicio.numbers.lang.de;

import org.dicio.numbers.parser.Parser;
import org.dicio.numbers.parser.lexer.TokenStream;
import org.dicio.numbers.unit.Duration;
import org.dicio.numbers.unit.Number;
import org.dicio.numbers.util.DurationExtractorUtils;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class GermanParser extends Parser {

    public GermanParser() {
        super("config/de-de");
    }


    @Override
    public Supplier<Number> extractNumber(final TokenStream tokenStream,
                                          final boolean shortScale,
                                          final boolean preferOrdinal) {
        final GermanNumberExtractor numberExtractor
                = new GermanNumberExtractor(tokenStream, shortScale);
        if (preferOrdinal) {
            return numberExtractor::numberPreferOrdinal;
        } else {
            return numberExtractor::numberPreferFraction;
        }
    }

    @Override
    public Supplier<Duration> extractDuration(final TokenStream tokenStream,
                                              final boolean shortScale) {
        final GermanNumberExtractor numberExtractor
                = new GermanNumberExtractor(tokenStream, shortScale);
        return new DurationExtractorUtils(tokenStream, numberExtractor::numberNoOrdinal)::duration;
    }

    @Override
    public Supplier<LocalDateTime> extractDateTime(final TokenStream tokenStream,
                                                   final boolean shortScale,
                                                   final boolean preferMonthBeforeDay,
                                                   final LocalDateTime now) {
        return new GermanDateTimeExtractor(tokenStream, shortScale, preferMonthBeforeDay, now)
                ::dateTime;
    }
}
