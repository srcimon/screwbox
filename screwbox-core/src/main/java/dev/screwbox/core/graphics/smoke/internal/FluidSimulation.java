package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;

import java.util.Arrays;

public class FluidSimulation {

    private final int resolution;
    private final int resolutionMinusTwo; // performance
    private final int resolutionMinusOne; // performance
    private final double physicalCellSize; // performance

    private final double[] densityR;
    private final double[] densityR0;

    private final double[] densityG;
    private final double[] densityG0;

    private final double[] densityB;
    private final double[] densityB0;

    private final double[] densityA;
    private final double[] densityA0;

    private final double[] velocityX;
    private final double[] velocityX0;

    private final double[] velocityY;
    private final double[] velocityY0;

    private final boolean[] obstacles;

    public FluidSimulation(final int resolution) {
        this.resolution = resolution;
        this.resolutionMinusTwo = resolution - 2;
        this.resolutionMinusOne = resolution - 1;
        this.physicalCellSize = 1.0 / resolutionMinusTwo;
        final int cellCount = resolution * resolution;
        this.densityR = new double[cellCount];
        this.densityR0 = new double[cellCount];
        this.densityG = new double[cellCount];
        this.densityG0 = new double[cellCount];
        this.densityB = new double[cellCount];
        this.densityB0 = new double[cellCount];
        this.densityA = new double[cellCount];
        this.densityA0 = new double[cellCount];
        this.velocityX = new double[cellCount];
        this.velocityX0 = new double[cellCount];
        this.velocityY = new double[cellCount];
        this.velocityY0 = new double[cellCount];
        this.obstacles = new boolean[cellCount];
    }

    public int resolution() {
        return resolution;
    }

    public boolean hasDensity() {
        final int cellCount = resolution * resolution;
        for (int i = 0; i < cellCount; i++) {
            if (densityR[i] > 0 || densityG[i] > 0 || densityB[i] > 0 || densityA[i] > 0) {
                return true;
            }
        }
        return false;
    }

    public void addDensity(final Offset cell, final double amount, final Color color) {
        if (isInGrid(cell.x(), cell.y()) && !isObstacle(cell)) {
            final int index = index(cell.x(), cell.y());
            densityR[index] = densityR[index] + (color.r() * amount);
            densityG[index] = densityG[index] + (color.g() * amount);
            densityB[index] = densityB[index] + (color.b() * amount);
            densityA[index] = densityA[index] + (color.alpha() * amount);
        }
    }

    public void addVelocity(final int x, final int y, final Vector velocity) {
        if (isInGrid(x, y) && !isObstacle(x, y)) {
            final int index = index(x, y);
            velocityX[index] = velocityX[index] + velocity.x();
            velocityY[index] = velocityY[index] + velocity.y();
        }
    }


    public void setVelocity(int x, int y, Vector velocity) {
        if (isInGrid(x, y) && !isObstacle(x, y)) {
            final int index = index(x, y);
            velocityX[index] = velocity.x();
            velocityY[index] = velocity.y();
        }
    }

    private boolean isObstacle(final Offset cell) {
        return obstacles[index(cell.x(), cell.y())];
    }

    private boolean isObstacle(final int x, int y) {
        return obstacles[index(x, y)];
    }

    private boolean isInGrid(final int x, final int y) {
        return x > 0 &&
               y > 0 &&
               x < resolution() &&
               y < resolution();
    }

    public DensityInfo densityData() {
        return new DensityInfo(resolution, densityR, densityG, densityB, densityA);
    }

    private int index(final int x, final int y) {
        return x + y * resolution;
    }

    public void step(final double delta, final double viscosity, final double diffusion, final int iterations) {
        // diffuse the volecities x and y
        diffuseVelocity(delta, viscosity, iterations);

        // clean up so that same amount of fluid is everywhere
        project(velocityX0, velocityY0, velocityX, velocityY, iterations);

        // advect velocities
        advect(velocityX, velocityX0, velocityX0, velocityY0, delta);
        advect(velocityY, velocityY0, velocityX0, velocityY0, delta);

        // clean that up
        project(velocityX, velocityY, velocityX0, velocityY0, iterations);

        // DIFFUSION FIX 2: Alle drei Farbkanäle zusammen diffundieren (3-in-1 Pass)
        diffuseRGB(diffusion, delta, iterations);

        // 2. Advect all three color channels using the solved velocities
        advect(densityR, densityR0, velocityX, velocityY, delta);
        advect(densityG, densityG0, velocityX, velocityY, delta);
        advect(densityB, densityB0, velocityX, velocityY, delta);
        advect(densityA, densityA0, velocityX, velocityY, delta);
    }

    void diffuseVelocity(final double delta, final double diffuse, final int iterations) {
        final double a = calculateA(delta, diffuse);
        double cRecip = 1.0 / (1.0 + 4.0 * a);

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int y = 1; y < resolutionMinusOne; y++) {
                final int row = y * resolution;


                for (int x = 1; x < resolutionMinusOne; x++) {
                    diffuseVelocityCell(x, row, a, cRecip);
                }
            }
        }
    }

    private void diffuseVelocityCell(final int x, final int row, final double a, final double cRecip) {
        final int index = x + row;
        if (obstacles[index]) {
            velocityX0[index] = 0;
            velocityY0[index] = 0;
            return;
        }

        final int top = x + row - resolution;
        final int bottom = x + row + resolution;
        final int left = index - 1;
        final int right = index + 1;

        final double nLeft = obstacles[left] ? -velocityX0[index] : velocityX0[left];
        final double nRight = obstacles[right] ? -velocityX0[index] : velocityX0[right];
        final double nTop = obstacles[top] ? velocityX0[index] : velocityX0[top];
        final double nBot = obstacles[bottom] ? velocityX0[index] : velocityX0[bottom];

        final double nLeftY = obstacles[left] ? velocityY0[index] : velocityY0[left];
        final double nRightY = obstacles[right] ? velocityY0[index] : velocityY0[right];
        final double nTopY = obstacles[top] ? -velocityY0[index] : velocityY0[top];
        final double nBotY = obstacles[bottom] ? -velocityY0[index] : velocityY0[bottom];

        velocityX0[index] = (velocityX[index] + a * (nRight + nLeft + nBot + nTop)) * cRecip;
        velocityY0[index] = (velocityY[index] + a * (nRightY + nLeftY + nBotY + nTopY)) * cRecip;
    }

    private double calculateA(final double delta, final double diffuse) {
        return delta * diffuse * resolutionMinusTwo * resolutionMinusTwo;
    }

    private void diffuseRGB(final double diffuse, final double delta, final int iterations) {
        double a = calculateA(delta, diffuse);
        double cRecip = 1.0 / (1.0 + 4.0 * a);

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int j = 1; j < resolutionMinusOne; j++) {
                final int row = j * resolution;
                final int topRow = row - resolution;
                final int botRow = row + resolution;

                for (int i = 1; i < resolutionMinusOne; i++) {
                    int curr = i + row;

                    if (obstacles[curr]) {
                        densityR0[curr] = 0;
                        densityG0[curr] = 0;
                        densityB0[curr] = 0;
                        densityA0[curr] = 0;
                        continue;
                    }

                    final int left = obstacles[curr - 1] ? curr : curr - 1;
                    final int right = obstacles[curr + 1] ? curr : curr + 1;
                    final int top = obstacles[topRow + i] ? curr : topRow + i;
                    final int bot = obstacles[botRow + i] ? curr : botRow + i;

                    densityR0[curr] = (densityR[curr] + a * (densityR0[right] + densityR0[left] + densityR0[bot] + densityR0[top])) * cRecip;
                    densityG0[curr] = (densityG[curr] + a * (densityG0[right] + densityG0[left] + densityG0[bot] + densityG0[top])) * cRecip;
                    densityB0[curr] = (densityB[curr] + a * (densityB0[right] + densityB0[left] + densityB0[bot] + densityB0[top])) * cRecip;
                    densityA0[curr] = (densityA[curr] + a * (densityA0[right] + densityA0[left] + densityA0[bot] + densityA0[top])) * cRecip;
                }
            }
        }
    }

    public void fade(final double fade) {
        for (int i = 0; i < densityR.length; i++) {
            densityR[i] = Math.max(0, densityR[i] - fade);
            densityG[i] = Math.max(0, densityG[i] - fade);
            densityB[i] = Math.max(0, densityB[i] - fade);
            densityA[i] = Math.max(0, densityA[i] - fade);
        }
    }

    private void linearSolve(final double[] x, final double[] x0, final int iterations) {
        for (int k = 0; k < iterations; k++) {
            linearSolveInteration(x, x0);
        }
    }

    private void linearSolveInteration(final double[] x, final double[] x0) {
        for (int j = 1; j < resolutionMinusOne; j++) {
            int indexCurrent = 1 + j * resolution;
            int indexLeft = indexCurrent - 1;
            int indexRight = indexCurrent + 1;
            int indexTop = indexCurrent - resolution;
            int indexBottom = indexCurrent + resolution;

            for (int i = 1; i < resolutionMinusOne; i++) {
                if (obstacles[indexCurrent]) {
                    x[indexCurrent] = 0;
                } else {
                    final double nRight = obstacles[indexRight] ? x[indexCurrent] : x[indexRight];
                    final double nLeft = obstacles[indexLeft] ? x[indexCurrent] : x[indexLeft];
                    final double nBottom = obstacles[indexBottom] ? x[indexCurrent] : x[indexBottom];
                    final double nTop = obstacles[indexTop] ? x[indexCurrent] : x[indexTop];
                    x[indexCurrent] = (x0[indexCurrent] + nRight + nLeft + nBottom + nTop) * 0.25;
                }

                indexCurrent++;
                indexLeft++;
                indexRight++;
                indexTop++;
                indexBottom++;
            }
        }
    }

    void project(double[] velocX, double[] velocY, double[] p, double[] div, int iter) {
        projectVelocities(velocX, velocY, p, div);
        linearSolve(p, div, iter);
        projectVelocities(velocX, velocY, p);
    }

    private void projectVelocities(final double[] velocX, final double[] velocY, final double[] p, final double[] div) {
        for (int j = 1; j < resolutionMinusOne; j++) {
            for (int i = 1; i < resolutionMinusOne; i++) {
                final int index = index(i, j);

                if (obstacles[index]) {
                    div[index] = 0;
                    p[index] = 0;
                    continue;
                }

                final int left = index(i - 1, j);
                final int right = index(i + 1, j);
                final int top = index(i, j - 1);
                final int bot = index(i, j + 1);

                div[index] = -0.5 * physicalCellSize * (velocX[right] - velocX[left] + velocY[bot] - velocY[top]);
                p[index] = 0;
            }
        }
    }

    private void projectVelocities(final double[] velocX, final double[] velocY, final double[] p) {
        for (int y = 1; y < resolutionMinusOne; y++) {
            for (int x = 1; x < resolutionMinusOne; x++) {
                final int index = index(x, y);

                if (obstacles[index]) {
                    velocX[index] = 0;
                    velocY[index] = 0;
                    continue;
                }

                final int left = index(x - 1, y);
                final int right = index(x + 1, y);
                final int top = index(x, y - 1);
                final int bot = index(x, y + 1);

                // mirror pressure
                final double pressureLeft = obstacles[left] ? p[index] : p[left];
                final double pressureRight = obstacles[right] ? p[index] : p[right];
                final double pressureTop = obstacles[top] ? p[index] : p[top];
                final double pressureBottom = obstacles[bot] ? p[index] : p[bot];

                velocX[index] -= 0.5 * (pressureRight - pressureLeft) / physicalCellSize;
                velocY[index] -= 0.5 * (pressureBottom - pressureTop) / physicalCellSize;
            }
        }
    }

    private void advect(double[] d, double[] d0, double[] velocX, double[] velocY, double dt) {
        final double tdRes = dt * resolutionMinusTwo;
        final double maxVal = resolution - 1.5;

        for (int j = 1; j < resolutionMinusOne; j++) {
            final double jfloat = j;

            for (int i = 1; i < resolutionMinusOne; i++) {
                final int ix = index(i, j);

                if (obstacles[ix]) {
                    d[ix] = 0;
                    continue;
                }

                double x = i - tdRes * velocX[ix];
                if (x < 0.5) x = 0.5;
                else if (x > maxVal) x = maxVal;

                double y = jfloat - tdRes * velocY[ix];
                if (y < 0.5) y = 0.5;
                else if (y > maxVal) y = maxVal;

                final int i0i = (int) x;
                final int j0i = (int) y;
                final int i1i = i0i + 1;
                final int j1i = j0i + 1;

                final double s1 = x - i0i;
                final double s0 = 1.0 - s1;
                final double t1 = y - j0i;
                final double t0 = 1.0 - t1;

                final int stride = resolution;
                final int base0 = j0i * stride;
                final int base1 = j1i * stride;

                final int idx00 = base0 + i0i;
                final int idx10 = base0 + i1i;
                final int idx01 = base1 + i0i;
                final int idx11 = base1 + i1i;

                final double m00 = obstacles[idx00] ? 0.0 : 1.0;
                final double m01 = obstacles[idx01] ? 0.0 : 1.0;
                final double m10 = obstacles[idx10] ? 0.0 : 1.0;
                final double m11 = obstacles[idx11] ? 0.0 : 1.0;

                d[ix] = s0 * (t0 * d0[idx00] * m00 + t1 * d0[idx01] * m01) +
                        s1 * (t0 * d0[idx10] * m10 + t1 * d0[idx11] * m11);
            }
        }
    }

    public void loadFrom(FluidSimulation oldSimulation, int deltaX, int deltaY) {
        for (int x = 1; x < resolutionMinusOne; x++) {
            for (int y = 1; y < resolutionMinusOne; y++) {

                int xOld = x + deltaX;
                int yOld = y + deltaY;

                if (xOld >= 1 && xOld < oldSimulation.resolutionMinusOne &&
                    yOld >= 1 && yOld < oldSimulation.resolutionMinusOne) {

                    int ix = x + y * resolution;
                    int ixOld = xOld + yOld * oldSimulation.resolution;

                    densityR[ix] = oldSimulation.densityR[ixOld];
                    densityR0[ix] = oldSimulation.densityR0[ixOld];
                    densityG[ix] = oldSimulation.densityG[ixOld];
                    densityG0[ix] = oldSimulation.densityG0[ixOld];

                    densityB[ix] = oldSimulation.densityB[ixOld];
                    densityB0[ix] = oldSimulation.densityB0[ixOld];
                    densityA[ix] = oldSimulation.densityA[ixOld];
                    densityA0[ix] = oldSimulation.densityA0[ixOld];

                    velocityX[ix] = oldSimulation.velocityX[ixOld];
                    velocityX0[ix] = oldSimulation.velocityX0[ixOld];
                    velocityY[ix] = oldSimulation.velocityY[ixOld];
                    velocityY0[ix] = oldSimulation.velocityY0[ixOld];

                    obstacles[ix] = oldSimulation.obstacles[ixOld];
                }
            }
        }
    }

    public void setObstacle(final int x, final int y) {
        if (isInGrid(x, y)) {
            obstacles[index(x, y)] = true;
        }
    }

    public void clearObstacles() {
        Arrays.fill(obstacles, false);
    }
}
