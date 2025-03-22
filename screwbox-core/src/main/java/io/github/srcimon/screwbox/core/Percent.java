package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;

/**
 * A percent value between 0.0 and 1.0.
 */
public class Percent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 1;
    private static final Percent MIN_PERCENT = of(MIN_VALUE);
    private static final Percent MAX_PERCENT = of(MAX_VALUE);
    private static final Percent QUARTER_PERCENT = of(0.25);
    private static final Percent HALF_PERCENT = of(0.5);
    private static final Percent THREE_QUARTER_PERCENT = of(0.75);

    private final double value;

    private Percent(final double value) {
        this.value = Math.clamp(value, MIN_VALUE, MAX_VALUE);
    }

    /**
     * Returns a new instance with the given values. Values below 0 returns {@link Percent#zero()}. Values above 1 returns {@link Percent#max()}.
     */
    public static Percent of(final double value) {
        return new Percent(value);
    }

    /**
     * Returns 25 percent.
     */
    public static Percent quarter() {
        return QUARTER_PERCENT;
    }

    /**
     * Returns 50 percent.
     */
    public static Percent half() {
        return HALF_PERCENT;
    }

    /**
     * Returns 75 percent.
     */
    public static Percent threeQuarter() {
        return THREE_QUARTER_PERCENT;
    }

    /**
     * Returns 0 percent.
     */
    public static Percent zero() {
        return MIN_PERCENT;
    }

    /**
     * Returns 100 percent.
     */
    public static Percent max() {
        return MAX_PERCENT;
    }

    /**
     * Returns the represented value between 0.0 and 1.0.
     */
    public double value() {
        return value;
    }

    /**
     * Returns {@code true} if value is 0.
     */
    public boolean isZero() {
        return MIN_VALUE == value;
    }

    /**
     * Returns {@code true} if value is 1.0.
     */
    public boolean isMax() {
        return MAX_VALUE == value;
    }

    @Override
    public String toString() {
        return "Percentage [value=" + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return prime * result + Double.hashCode(value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Percent other = (Percent) obj;
        return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
    }

    /**
     * Returns the inverted value of the percentage.
     */
    public Percent invert() {
        return Percent.of(MAX_VALUE - value);
    }

    /**
     * Returns a new instance with the sum of both values. Returns
     * {@link Percent#max()} when the sum is above 100 percent.
     */
    public Percent add(final double value) {
        return new Percent(this.value + value);
    }

    /**
     * Returns a new instance with the sum of both values. Does not cap on max or min value but
     * circles in valid range.
     */
    public Percent addWithOverflow(final double value) {
        final double addedValue = this.value + value;
        final double clamp = addedValue >= 0 ? 0 : 1;
        return new Percent(addedValue % 1.0 + clamp);
    }

    /**
     * Returns a new instance with the current value minus the given value. Returns
     * {@link Percent#zero()} when the sum is below 0 percent.
     */
    public Percent substract(final double value) {
        return new Percent(this.value - value);
    }

    /**
     * Returns a new instance with the current value multiplied with the given
     * value.
     */
    public Percent multiply(final double value) {
        return new Percent(this.value * value);
    }

    //TODO document
    public int rangeValue(final int from, final int to) {
        return (int)(from + value() * (to - from));
    }

    //TODO document
    public double rangeValue(final double from, final double to) {
        return (int)(from + value() * (to - from));
    }
}