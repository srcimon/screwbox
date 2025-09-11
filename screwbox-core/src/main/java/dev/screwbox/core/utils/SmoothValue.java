package dev.screwbox.core.utils;

/**
 * Used to provide a fast average mean value for metrics.
 *
 * @since 3.9.0
 */
public class SmoothValue {

    private final double[] samples;
    private int index = 0;
    private double value = 0;
    private int count = 0;

    /**
     * Creates a new instance.
     *
     * @param size sample size that will be used to calculate averages.
     */
    public SmoothValue(final int size) {
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
        count = Math.min(count + 1, samples.length);
    }

    /**
     * Returns the average over the provided samples.
     */
    public double average() {
        final int sampleCount = count == 0 ? 1 : count;
        return this.value / sampleCount;
    }

    /**
     * Adds a new sample value and returns the average over the provided samples.
     */
    public double average(final double value) {
        record(value);
        return average();
    }
}
