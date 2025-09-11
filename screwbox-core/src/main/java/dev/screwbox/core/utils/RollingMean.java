package dev.screwbox.core.utils;

public class RollingMean {

    private final double[] samples;
    private int index = 0;
    private double value;
    private boolean initialized;

    public RollingMean(final int size) {
        Validate.positive(size, "size must be positive");
        samples = new double[size];
    }

    public int intValue() {
        return (int) (this.value / samples.length);
    }

    public void record(final double value) {
        this.value += value - samples[index];
        samples[index] = value;
        index++;
        if (index == samples.length) {
            index = 0;
        }
    }

    public int average(int value) {
        record(value);
        return intValue();
    }
}
