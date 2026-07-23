package dev.screwbox.core.smoke.internal;

public record DensityInfo(int cells, double[] density) {

    public double dessityAt(int x, int y) {
        return density[x + y * cells];
    }
}
