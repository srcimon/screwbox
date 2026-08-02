package dev.screwbox.core.graphics.smoke.internal;

import java.util.Arrays;

public class DensityInfo {

    private final int cells;
    private final double[] red;
    private final double[] green;
    private final double[] blue;
    private final double[] alpha;

    public DensityInfo(final int cells, final double[] red, final double[] green, final double[] blue, final double[] alpha) {
        this.cells = cells;
        this.red = Arrays.copyOf(red, red.length);
        this.green = Arrays.copyOf(green, green.length);
        this.blue = Arrays.copyOf(blue, blue.length);
        this.alpha = Arrays.copyOf(alpha, alpha.length);
    }

    public int cells() {
        return cells;
    }

    public int calculateIndex(final int x, final int y) {
        return x + y * cells;
    }

    public double red(final int index) {
        return red[index];
    }

    public double green(final int index) {
        return green[index];
    }

    public double blue(final int index) {
        return blue[index];
    }

    public double alpha(final int index) {
        return alpha[index];
    }

    public double red(final int x, final int y) {
        return red[calculateIndex(x, y)];
    }

    public double green(final int x, final int y) {
        return green[calculateIndex(x, y)];
    }

    public double blue(final int x, final int y) {
        return blue[calculateIndex(x, y)];
    }

    public double alpha(final int x, final int y) {
        return alpha[calculateIndex(x, y)];
    }

}
