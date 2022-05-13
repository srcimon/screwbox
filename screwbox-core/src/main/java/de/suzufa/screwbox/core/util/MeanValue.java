package de.suzufa.screwbox.core.util;

public class MeanValue {

    private double sum;
    private double count;

    public void addValue(final double value) {
        sum += value;
        count++;
    }

    public double calculate() {
        return sum / count;
    }

    public void reset() {
        sum = 0;
        count = 0;
    }
}
