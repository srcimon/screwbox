package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.utils.MathUtil;

import java.io.Serial;
import java.io.Serializable;

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
        this.value = MathUtil.clamp(MIN_VALUE, value, MAX_VALUE);
    }

    public static Percent of(final double value) {
        return new Percent(value);
    }

    /**
     * Returns 25 percent.
     */
    public static Percent quater() {
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
    public static Percent min() {
        return MIN_PERCENT;
    }

    /**
     * Returns 100 percent.
     */
    public static Percent max() {
        return MAX_PERCENT;
    }

    public double value() {
        return value;
    }

    public boolean isMinValue() {
        return MIN_VALUE == value;
    }

    public boolean isMaxValue() {
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
        long temp;
        temp = Double.doubleToLongBits(value);
        return prime * result + (int) (temp ^ (temp >>> 32));
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
     * Returns a new instance with the current value minus the given value. Returns
     * {@link Percent#min()} when the sum is below 0 percent.
     */
    public Percent substract(final double value) {
        return new Percent(this.value - value);
    }

    /**
     * Returns a new instance with the current value multiplied with the given
     * value.
     */
    public Percent multiply(double value) {
        return new Percent(this.value * value);
    }

}
