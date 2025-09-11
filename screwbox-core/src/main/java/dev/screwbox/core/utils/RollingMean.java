package dev.screwbox.core.utils;

/**
 * Used to provide a fast average mean value for metrics.
 *
 * @since 3.9.0
 */
public class RollingMean {

    private final double[] samples;
    private int index = 0;
    private double value;
    private int filled;

    /**
     * Creates a new instance.
     *
     * @param size sample size that will be used to calculate averages.
     */
    public RollingMean(final int size) {
        Validate.positive(size, "size must be positive");
        samples = new double[size];
    }

    /**
     * Adds a new sample value.
     */
    public void record(final double value) {
        this.value += value - samples[index];
        samples[index] = value;
        index++;
        if (index == samples.length) {
            index = 0;
        }
        filled = Math.min(filled + 1, samples.length);
    }

    /**
     * Returns the average over the provided samples.
     */
    public double average() {
        return this.value / filled;
    }

}
