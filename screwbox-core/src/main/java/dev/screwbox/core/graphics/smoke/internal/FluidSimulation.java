package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class FluidSimulation {

    private final int resolution;
    private final int resolutionMinusTwo; // performance
    private final int resolutionMinusOne; // performance

    private final double[] densityR;
    private final double[] densityR0;

    private final double[] densityG;
    private final double[] densityG0;

    private final double[] densityB;
    private final double[] densityB0;

    private final double[] velocityX;
    private final double[] velocityX0;

    private final double[] velocityY;
    private final double[] velocityY0;

    private final boolean[] obstacles;

    public FluidSimulation(final int resolution) {
        this.resolution = resolution;
        this.resolutionMinusTwo = resolution - 2;
        this.resolutionMinusOne = resolution - 1;
        final int cellCount = resolution * resolution;
        this.densityR = new double[cellCount];
        this.densityR0 = new double[cellCount];
        this.densityG = new double[cellCount];
        this.densityG0 = new double[cellCount];
        this.densityB = new double[cellCount];
        this.densityB0 = new double[cellCount];
        this.velocityX = new double[cellCount];
        this.velocityX0 = new double[cellCount];
        this.velocityY = new double[cellCount];
        this.velocityY0 = new double[cellCount];
        this.obstacles = new boolean[cellCount];
    }

    public int resolution() {
        return resolution;
    }

    public void addDensity(final Offset cell, final double amount, final double limit, Color color) {
        if (isFreeCell(cell)) {
            final int index = index(cell.x(), cell.y());
            densityR[index] = Math.min(densityR[index] + (color.r() * amount), limit);
            densityG[index] = Math.min(densityG[index] + (color.g() * amount), limit);
            densityB[index] = Math.min(densityB[index] + (color.b() * amount), limit);
        }
    }

    public void addVelocity(final Offset cell, final Vector velocity, final double limit) {
        if (isFreeCell(cell)) {
            final int index = index(cell.x(), cell.y());
            velocityX[index] = Math.min(limit, velocityX[index] + velocity.x());
            velocityY[index] = Math.min(limit, velocityY[index] + velocity.y());
        }
    }

    private boolean isFreeCell(final Offset cell) {
        return cell.x() > 0 &&
               cell.y() > 0 &&
               cell.x() < resolution() &&
               cell.y() < resolution() &&
               !obstacles[index(cell.x(), cell.y())];
    }

    //TODO me dont like this
    public FluidSimulationState state() {
        return new FluidSimulationState(resolution, Arrays.copyOf(densityR, densityR.length), Arrays.copyOf(densityG, densityG.length), Arrays.copyOf(densityB, densityB.length));
    }

    //TODO reduce usage as much as possible
    private int indexSafe(int x, int y) {
        return Math.clamp(x, 0, resolutionMinusOne) +
               Math.clamp(y, 0, resolutionMinusOne) * resolution;
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
    }

    private void diffuseCell(int i, int row, int topRow, int botRow, double a, double cRecip) {
        int curr = i + row;

        if (obstacles[curr]) {
            velocityX0[curr] = 0;
            velocityY0[curr] = 0;
            return;
        }

        final int top = i + topRow;
        final int bottom = i + botRow;
        final int left = curr - 1;
        final int right = curr + 1;

        double nLeft = obstacles[left] ? -velocityX0[curr] : velocityX0[left];
        double nRight = obstacles[right] ? -velocityX0[curr] : velocityX0[right];
        double nTop = obstacles[top] ? velocityX0[curr] : velocityX0[top];
        double nBot = obstacles[bottom] ? velocityX0[curr] : velocityX0[bottom];

        double nLeftY = obstacles[left] ? velocityY0[curr] : velocityY0[left];
        double nRightY = obstacles[right] ? velocityY0[curr] : velocityY0[right];
        double nTopY = obstacles[top] ? -velocityY0[curr] : velocityY0[top];
        double nBotY = obstacles[bottom] ? -velocityY0[curr] : velocityY0[bottom];

        velocityX0[curr] = (velocityX[curr] + a * (nRight + nLeft + nBot + nTop)) * cRecip;
        velocityY0[curr] = (velocityY[curr] + a * (nRightY + nLeftY + nBotY + nTopY)) * cRecip;
    }

    void diffuseVelocity(final double delta, final double diffuse, final int iterations) {
        final double a = calculateA(delta, diffuse);
        double cRecip = 1.0 / (1.0 + 4.0 * a);

        for (int k = 0; k < iterations; k++) {
            for (int j = 1; j < resolutionMinusOne; j++) {
                int row = j * resolution;
                int topRow = row - resolution;
                int botRow = row + resolution;

                // Hauptschleife: Behält Ihr manuelles Unrolling (Faktor 2) exakt bei
                for (int i = 1; i < resolutionMinusTwo; i += 2) {
                    diffuseCell(i, row, topRow, botRow, a, cRecip);
                    diffuseCell(i + 1, row, topRow, botRow, a, cRecip);
                }

                // Rest-Schleife: Verarbeitet die verbleibende Zelle, falls resolution ungerade ist
                for (int i = 1; i < resolutionMinusOne; i++) {
                    diffuseCell(i, row, topRow, botRow, a, cRecip);
                }
            }
        }
    }

    private double calculateA(final double delta, final double diffuse) {
        return delta * diffuse * resolutionMinusTwo * resolutionMinusTwo;
    }

    void diffuseRGB(double diff, double dt, int iter) {
        double a = calculateA(dt, diff);
        double cRecip = 1.0 / (1.0 + 4.0 * a);

        for (int k = 0; k < iter; k++) {
            for (int j = 1; j < resolutionMinusOne; j++) {
                int row = j * resolution;
                int topRow = row - resolution;
                int botRow = row + resolution;

                int i = 1;
                // Verarbeitet R, G und B für 2 Zellen pro Schleifendurchlauf
                for (; i < resolutionMinusTwo; i += 2) {
                    int curr0 = i + row;
                    int curr1 = curr0 + 1;
                    int top0 = i + topRow;
                    int top1 = top0 + 1;
                    int bot0 = i + botRow;
                    int bot1 = bot0 + 1;

                    // Index i
                    densityR0[curr0] = (densityR[curr0] + a * (densityR0[curr0 + 1] + densityR0[curr0 - 1] + densityR0[bot0] + densityR0[top0])) * cRecip;
                    densityG0[curr0] = (densityG[curr0] + a * (densityG0[curr0 + 1] + densityG0[curr0 - 1] + densityG0[bot0] + densityG0[top0])) * cRecip;
                    densityB0[curr0] = (densityB[curr0] + a * (densityB0[curr0 + 1] + densityB0[curr0 - 1] + densityB0[bot0] + densityB0[top0])) * cRecip;

                    // Index i + 1
                    densityR0[curr1] = (densityR[curr1] + a * (densityR0[curr1 + 1] + densityR0[curr1 - 1] + densityR0[bot1] + densityR0[top1])) * cRecip;
                    densityG0[curr1] = (densityG[curr1] + a * (densityG0[curr1 + 1] + densityG0[curr1 - 1] + densityG0[bot1] + densityG0[top1])) * cRecip;
                    densityB0[curr1] = (densityB[curr1] + a * (densityB0[curr1 + 1] + densityB0[curr1 - 1] + densityB0[bot1] + densityB0[top1])) * cRecip;
                }
                for (; i < resolutionMinusOne; i++) {
                    int curr = i + row;
                    densityR0[curr] = (densityR[curr] + a * (densityR0[curr + 1] + densityR0[curr - 1] + densityR0[i + botRow] + densityR0[i + topRow])) * cRecip;
                    densityG0[curr] = (densityG[curr] + a * (densityG0[curr + 1] + densityG0[curr - 1] + densityG0[i + botRow] + densityG0[i + topRow])) * cRecip;
                    densityB0[curr] = (densityB[curr] + a * (densityB0[curr + 1] + densityB0[curr - 1] + densityB0[i + botRow] + densityB0[i + topRow])) * cRecip;
                }
            }
        }
    }

    public void fade(double fade) {
        for (int i = 0; i < densityR.length; i++) {
            densityR[i] = Math.max(0, densityR[i] - fade);
            densityG[i] = Math.max(0, densityG[i] - fade);
            densityB[i] = Math.max(0, densityB[i] - fade);
        }
    }


    void linearSolve(final double[] x, final double[] x0, final int iterations) {
        for (int k = 0; k < iterations; k++) {
            linSolveInteration(x, x0);
        }
    }

    private void linSolveInteration(final double[] x, final double[] x0) {
        for (int j = 1; j < resolutionMinusOne; j++) {

            // Pointer-Initialisierung für den Zeilenstart (i = 1)
            int indexCurrent = 1 + j * resolution;
            int indexLeft = indexCurrent - 1;
            int indexRight = indexCurrent + 1;
            int indexTop = indexCurrent - resolution;
            int indexBottom = indexCurrent + resolution;

            for (int i = 1; i < resolutionMinusOne; i++) {

                if (obstacles[indexCurrent]) {
                    x[indexCurrent] = 0;
                } else {
                    // Wenn Nachbar ein Hindernis ist, nimm den Wert der aktuellen Zelle (Reflektion)
                    double nRight = obstacles[indexRight] ? x[indexCurrent] : x[indexRight];
                    double nLeft = obstacles[indexLeft] ? x[indexCurrent] : x[indexLeft];
                    double nBottom = obstacles[indexBottom] ? x[indexCurrent] : x[indexBottom];
                    double nTop = obstacles[indexTop] ? x[indexCurrent] : x[indexTop];

                    x[indexCurrent] = (x0[indexCurrent] + nRight + nLeft + nBottom + nTop) * 0.25;
                }

                // Alle Pointer rücken synchron um genau 1 Zelle weiter (Hardware-Prefetching bleibt aktiv)
                indexCurrent++;
                indexLeft++;
                indexRight++;
                indexTop++;
                indexBottom++;
            }
        }
    }

    void project(double[] velocX, double[] velocY, double[] p, double[] div, int iter) {
        final double h = 1.0 / resolutionMinusTwo;

        // 1. Schritt: Divergenz berechnen unter Berücksichtigung der Hindernisse
        for (int j = 1; j < resolutionMinusOne; j++) {
            for (int i = 1; i < resolutionMinusOne; i++) {
                int ix = index(i, j);

                if (obstacles[ix]) {
                    div[ix] = 0;
                    p[ix] = 0;
                    continue;
                }

                int left = index(i - 1, j);
                int right = index(i + 1, j);
                int top = index(i, j - 1);
                int bot = index(i, j + 1);

                // Wenn der Nachbar ein Hindernis ist, fließt dort nichts durch (Geschwindigkeit = 0)
                double vL = obstacles[left] ? 0 : velocX[left];
                double vR = obstacles[right] ? 0 : velocX[right];
                double vT = obstacles[top] ? 0 : velocY[top];
                double vB = obstacles[bot] ? 0 : velocY[bot];

                div[ix] = -0.5 * h * (vR - vL + vB - vT);
                p[ix] = 0;
            }
        }

        // Berechnet das Druckfeld p basierend auf der Divergenz (lin_solve muss obstacles ebenfalls beachten!)
        linearSolve(p, div, iter);

        // 2. Schritt: Geschwindigkeiten korrigieren (Druckgradient abziehen)
        for (int j = 1; j < resolutionMinusOne; j++) {
            for (int i = 1; i < resolutionMinusOne; i++) {
                int ix = index(i, j);

                if (obstacles[ix]) {
                    velocX[ix] = 0;
                    velocY[ix] = 0;
                    continue;
                }

                int left = index(i - 1, j);
                int right = index(i + 1, j);
                int top = index(i, j - 1);
                int bot = index(i, j + 1);

                // Neumann-Randbedingung für den Druck: p spiegeln, wenn der Nachbar ein Hindernis ist
                double pL = obstacles[left] ? p[ix] : p[left];
                double pR = obstacles[right] ? p[ix] : p[right];
                double pT = obstacles[top] ? p[ix] : p[top];
                double pB = obstacles[bot] ? p[ix] : p[bot];

                velocX[ix] -= 0.5 * (pR - pL) / h;
                velocY[ix] -= 0.5 * (pB - pT) / h;
            }
        }
    }

    void advect(double[] d, double[] d0, double[] velocX, double[] velocY, double dt) {
        double tdRes = dt * resolutionMinusTwo;

        double tmp1, tmp2, x, y;

        double Nfloat = resolution;
        double ifloat, jfloat;
        int i, j;

        for (j = 1, jfloat = 1; j < resolutionMinusOne; j++, jfloat++) {
            for (i = 1, ifloat = 1; i < resolutionMinusOne; i++, ifloat++) {
                int ix = index(i, j);

                // Wenn die aktuelle Zelle ein Hindernis ist, strömt hier nichts hin
                if (obstacles[ix]) {
                    d[ix] = 0;
                    continue;
                }

                tmp1 = tdRes * velocX[ix];
                tmp2 = tdRes * velocY[ix];
                x = ifloat - tmp1;
                y = jfloat - tmp2;

                // Grenzen des Simulationsbereichs einhalten
                if (x < 0.5) x = 0.5;
                if (x > Nfloat - 1.5) x = Nfloat - 1.5; // Leicht korrigiert für sichere Grid-Indizes
                double i0 = Math.floor(x);
                double i1 = i0 + 1.0;

                if (y < 0.5) y = 0.5;
                if (y > Nfloat - 1.5) y = Nfloat - 1.5;
                double j0 = Math.floor(y);
                double j1 = j0 + 1.0;

                double s1 = x - i0;
                double s0 = 1.0 - s1;
                double t1 = y - j0;
                double t0 = 1.0 - t1;

                int i0i = (int) (i0);
                int i1i = (int) (i1);
                int j0i = (int) (j0);
                int j1i = (int) (j1);

                // Hindernis-Check für die 4 Interpolations-Nachbarn:
                // Wenn ein Quellpixel im Hindernis liegt, nutzen wir stattdessen das aktuelle Feld (ix)
                int idx00 = obstacles[index(i0i, j0i)] ? ix : index(i0i, j0i);
                int idx01 = obstacles[index(i0i, j1i)] ? ix : index(i0i, j1i);
                int idx10 = obstacles[index(i1i, j0i)] ? ix : index(i1i, j0i);
                int idx11 = obstacles[index(i1i, j1i)] ? ix : index(i1i, j1i);

                // Bilineare Interpolation mit den korrigierten Indizes
                d[ix] = s0 * (t0 * d0[idx00] + t1 * d0[idx01]) +
                        s1 * (t0 * d0[idx10] + t1 * d0[idx11]);
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

                    // Nutze für jedes Objekt die jeweils eigene Index-Arithmetik!
                    int ix = x + y * resolution; // Inlined für das neue Grid
                    int ixOld = xOld + yOld * oldSimulation.resolution; // Nutzt oldSimulation.cells!

                    densityR[ix] = oldSimulation.densityR[ixOld];
                    densityR0[ix] = oldSimulation.densityR0[ixOld];
                    densityG[ix] = oldSimulation.densityG[ixOld];
                    densityG0[ix] = oldSimulation.densityG0[ixOld];

                    densityB[ix] = oldSimulation.densityB[ixOld];
                    densityB0[ix] = oldSimulation.densityB0[ixOld];

                    velocityX[ix] = oldSimulation.velocityX[ixOld];
                    velocityX0[ix] = oldSimulation.velocityX0[ixOld];
                    velocityY[ix] = oldSimulation.velocityY[ixOld];
                    velocityY0[ix] = oldSimulation.velocityY0[ixOld];

                    obstacles[ix] = oldSimulation.obstacles[ixOld];
                }
            }
        }
    }

    public void setObstacle(int x, int y) {
        obstacles[indexSafe(x, y)] = true;
    }

    public void clearObstacles() {
        Arrays.fill(obstacles, false);
    }

    public void loadDensityFromImageTiled(BufferedImage img) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        for (int y = 0; y < resolution; y++) {
            // Kachelung in Y-Richtung per Modulo
            int imgY = y % imgHeight;

            for (int x = 0; x < resolution; x++) {
                // Kachelung in X-Richtung per Modulo
                int imgX = x % imgWidth;

                // 2D-zu-1D Index für die Simulations-Arrays
                int index = x + y * resolution;

                // Holt den kombinierten ARGB-Wert des Pixels
                int rgb = img.getRGB(imgX, imgY);

                // Bit-Shifting extrahiert die Kanäle (Wertebereich 0 bis 255)
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Normierung auf 0.0 - 1.0 für die Fluid-Simulation
                this.densityR[index] = r / 255.0;
                this.densityG[index] = g / 255.0;
                this.densityB[index] = b / 255.0;

                // Zurücksetzen der vorherigen Zeitschritte
                this.densityR0[index] = 0.0;
                this.densityG0[index] = 0.0;
                this.densityB0[index] = 0.0;
            }
        }
    }

}
