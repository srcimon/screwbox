package dev.screwbox.core.utils;

public class RollingMean {

    private final double[] samples;
    private int index = 0;
    private double value;
    private int filled;

    public RollingMean(final int size) {
        Validate.positive(size, "size must be positive");
        samples = new double[size];
    }

    public int intValue() {
        return (int) (this.value / filled);
    }

    public void record(final double value) {
        this.value += value - samples[index];
        samples[index] = value;
        index++;
        if (index == samples.length) {
            index = 0;
        }
        filled = Math.min(filled + 1, samples.length);
    }

    public double average() {
        return this.value / filled;
    }

}
