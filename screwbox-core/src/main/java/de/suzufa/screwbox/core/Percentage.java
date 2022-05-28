package de.suzufa.screwbox.core;

import java.io.Serializable;

import de.suzufa.screwbox.core.utils.MathUtil;

public class Percentage implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 1;
    private static final double HALF_VALUE = (MAX_VALUE - MIN_VALUE) / 2.0;
    private static final Percentage MIN_PERCENTAGE = of(MIN_VALUE);
    private static final Percentage MAX_PERCENTAGE = of(MAX_VALUE);
    private static final Percentage HALF_PERCENTAGE = of(HALF_VALUE);

    private final double value;

    private Percentage(final double value) {
        this.value = MathUtil.clamp(MIN_VALUE, value, MAX_VALUE);
    }

    public static Percentage of(final double value) {
        return new Percentage(value);
    }

    public static Percentage half() {
        return HALF_PERCENTAGE;
    }

    public static Percentage min() {
        return MIN_PERCENTAGE;
    }

    public static Percentage max() {
        return MAX_PERCENTAGE;
    }

    public double value() {
        return value;
    }

    public float valueFloat() {
        return (float) value;
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
        final Percentage other = (Percentage) obj;
        return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
    }

    public Percentage invert() {
        return Percentage.of(MAX_VALUE - value);
    }

    /**
     * Returns a new instance with the sum of both values. Returns
     * {@link Percentage#max()} when the sum is above 100 percent.
     */
    public Percentage add(final double value) {
        return new Percentage(this.value + value);
    }

    /**
     * Returns a new instance with the current value minus the given value. Returns
     * {@link Percentage#min()} when the sum is below 0 percent.
     */
    public Percentage substract(final double value) {
        return new Percentage(this.value - value);
    }

}
