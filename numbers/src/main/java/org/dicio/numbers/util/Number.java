package org.dicio.numbers.util;

import java.util.Objects;

public class Number {

    private final boolean isDecimal;
    private final long integerValue;
    private final double decimalValue;

    public Number(final long integerValue) {
        this.isDecimal = false;
        this.integerValue = integerValue;
        this.decimalValue = Double.NaN;
    }

    public Number(final double decimalValue) {
        this.isDecimal = true;
        this.integerValue = 0;
        this.decimalValue = decimalValue;
    }


    public boolean isDecimal() {
        return isDecimal;
    }

    public boolean isInteger() {
        return !isDecimal;
    }

    public long integerValue() {
        return integerValue;
    }

    public double decimalValue() {
        return decimalValue;
    }


    public Number multiply(final long integer) {
        if (isDecimal) {
            return new Number(decimalValue * integer);
        } else {
            try {
                return new Number(Math.multiplyExact(integerValue, integer));
            } catch (final ArithmeticException e) {
                return new Number((double) integerValue * integer);
            }
        }
    }

    public Number multiply(final double decimal) {
        return new Number((isDecimal ? decimalValue : integerValue) * decimal);
    }

    public Number multiply(final Number number) {
        return number.isDecimal ? multiply(number.decimalValue) : multiply(number.integerValue);
    }

    public Number plus(final long integer) {
        if (isDecimal) {
            return new Number(decimalValue + integer);
        } else {
            try {
                return new Number(Math.addExact(integerValue, integer));
            } catch (final ArithmeticException e) {
                return new Number((double) integerValue + integer);
            }
        }
    }

    public Number plus(final double decimal) {
        return new Number((isDecimal ? decimalValue : integerValue) + decimal);
    }

    public Number plus(final Number number) {
        return number.isDecimal ? plus(number.decimalValue) : plus(number.integerValue);
    }

    public Number divide(final Number number) {
        if (isDecimal || number.isDecimal || (integerValue % number.integerValue != 0)) {
            return new Number((isDecimal ? decimalValue : integerValue)
                    / (number.isDecimal ? number.decimalValue : number.integerValue));
        } else {
            return new Number(integerValue / number.integerValue);
        }
    }

    public boolean lessThan(final long integer) {
        return isDecimal ? (decimalValue < integer) : (integerValue < integer);
    }

    public boolean moreThan(final long integer) {
        return isDecimal ? (decimalValue > integer) : (integerValue > integer);
    }


    public boolean equals(final long integer) {
        return !isDecimal && integerValue == integer;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            final Number number = (Number) o;
            return isDecimal == number.isDecimal && (isDecimal
                    ? decimalValue == number.decimalValue : integerValue == number.integerValue);
        }
    }

    @Override
    public int hashCode() {
        return isDecimal ? Objects.hash(true, decimalValue) : Objects.hash(false, integerValue);
    }

    @Override
    public String toString() {
        return isDecimal ? String.valueOf(decimalValue) : String.valueOf(integerValue);
    }
}
