package dev.screwbox.core.graphics.smoke.internal;

public record FluidSimulationState(int cells, double[] densityR, double[] densityG, double[] densityB, double[] densityA) {

    public double densityRed(int x, int y) {
        return densityR[x + y * cells];
    }

    public double densityGreen(int x, int y) {
        return densityG[x + y * cells];
    }

    public double densityBlue(int x, int y) {
        return densityB[x + y * cells];
    }

    public double densityAlpha(int x, int y) {
        return densityA[x + y * cells];
    }
}
