package dev.screwbox.core.smoke.internal;

public record DensityInfo(int cells, double[] densityR,double[] densityG,double[] densityB) {

    public double dessityRAt(int x, int y) {
        return densityR[x + y * cells];
    }

    public double dessityGAt(int x, int y) {
        return densityG[x + y * cells];
    }

    public double dessityBAt(int x, int y) {
        return densityB[x + y * cells];
    }
}
