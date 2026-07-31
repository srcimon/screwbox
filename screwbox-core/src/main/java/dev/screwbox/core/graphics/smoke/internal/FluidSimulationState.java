package dev.screwbox.core.graphics.smoke.internal;

public record FluidSimulationState(int cells, double[] densityR, double[] densityG, double[] densityB, double[] densityA) {

    public int calculateIndex(int x , int y) {
        return x + y * cells;
    }
    public double densityRed(int index) {
        return densityR[index];
    }

    public double densityGreen(int index) {
        return densityR[index];
    }

    public double densityBlue(int index) {
        return densityR[index];
    }

    public double densityAlpha(int index) {
        return densityR[index];
    }

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
