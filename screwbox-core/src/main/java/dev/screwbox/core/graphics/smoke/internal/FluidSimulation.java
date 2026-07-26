package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Color;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class FluidSimulation {

    private final int resolution;

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

    double maxDensity = 4;
    double maxVelocity = 20;

    public void addDensity(final int x, int y, final double amount, Color color) {
        int ix = indexSafe(x, y);
        densityR[ix] += amount * color.r() / 255.0;
        densityG[ix] += amount * color.g() / 255.0;
        densityB[ix] += amount * color.b() / 255.0;
        densityR[ix] = Math.min(maxDensity, densityR[ix]);
        densityG[ix] = Math.min(maxDensity, densityG[ix]);
        densityB[ix] = Math.min(maxDensity, densityB[ix]);
    }

    public void addVelocity(final int x, int y, final double amountX, final double amountY) {
        int ix = indexSafe(x, y);
        velocityX[ix] += amountX;
        velocityY[ix] += amountY;
        velocityX[ix] = Math.min(maxVelocity, velocityX[ix]);
        velocityY[ix] = Math.min(maxVelocity, velocityY[ix]);
    }


    public FluidSimulationState state() {
        return new FluidSimulationState(resolution, Arrays.copyOf(densityR, densityR.length), Arrays.copyOf(densityG, densityG.length), Arrays.copyOf(densityB, densityB.length));
    }

    private int indexSafe(int x, int y) {
        return Math.clamp(x, 0, resolution - 1) +
               Math.clamp(y, 0, resolution - 1) * resolution;
    }

    private int index(int x, int y) {
        return x + y * resolution;
    }

    public void step(double delta, double visc, double diff, int iter) {
        // DIFFUSION FIX 1: Geschwindigkeiten zusammen diffundieren (2-in-1 Pass)
        diffuse2D(this.velocityX0, this.velocityY0, this.velocityX, this.velocityY, visc, delta, iter);

        // clean up so that same amount of fluid is everywhere
        project(this.velocityX0, this.velocityY0, this.velocityX, this.velocityY, iter);

        // advect velocities
        advect(this.velocityX, this.velocityX0, this.velocityX0, this.velocityY0, delta);
        advect(this.velocityY, this.velocityY0, this.velocityX0, this.velocityY0, delta);

        // clean that up
        project(this.velocityX, this.velocityY, this.velocityX0, this.velocityY0, iter);

        // DIFFUSION FIX 2: Alle drei Farbkanäle zusammen diffundieren (3-in-1 Pass)
        diffuseRGB(this.densityR0, this.densityG0, this.densityB0, this.densityR, this.densityG, this.densityB, diff, delta, iter);

        // 2. Advect all three color channels using the solved velocities
        advect(this.densityR, this.densityR0, this.velocityX, this.velocityY, delta);
        advect(this.densityG, this.densityG0, this.velocityX, this.velocityY, delta);
        advect(this.densityB, this.densityB0, this.velocityX, this.velocityY, delta);
    }

    void diffuse2D(double[] x, double[] y, double[] x0, double[] y0, double diff, double dt, int iter) {
        int size = this.resolution;
        double iterationBonus = 1.0 + (20.0 - iter) * 0.05;
        double a = dt * (diff * iterationBonus) * (size - 2) * (size - 2);
        double cRecip = 1.0 / (1.0 + 4.0 * a);

        for (int k = 0; k < iter; k++) {
            for (int j = 1; j < size - 1; j++) {
                int row = j * size;
                int topRow = row - size;
                int botRow = row + size;

                int i = 1;
                // Loop Unrolling (Faktor 2)
                for (; i < size - 2; i += 2) {
                    int curr0 = i + row;
                    int curr1 = curr0 + 1;

                    // --- INDEX i ---
                    if (obstacles[curr0]) {
                        x[curr0] = 0;
                        y[curr0] = 0;
                    } else {
                        int top0 = i + topRow;
                        int bot0 = i + botRow;
                        int left0 = curr0 - 1;
                        int right0 = curr0 + 1;

                        // Wenn Nachbar ein Hindernis ist, nimm den aktuellen Zellwert (Spiegelung)
                        double nLeft = obstacles[left0] ? -x[curr0] : x[left0]; // -x für Reflektion der Geschwindigkeit
                        double nRight = obstacles[right0] ? -x[curr0] : x[right0];
                        double nTop = obstacles[top0] ? x[curr0] : x[top0];
                        double nBot = obstacles[bot0] ? x[curr0] : x[bot0];

                        double nLeftY = obstacles[left0] ? y[curr0] : y[left0];
                        double nRightY = obstacles[right0] ? y[curr0] : y[right0];
                        double nTopY = obstacles[top0] ? -y[curr0] : y[top0]; // -y für Reflektion an waagerechten Wänden
                        double nBotY = obstacles[bot0] ? -y[curr0] : y[bot0];

                        x[curr0] = (x0[curr0] + a * (nRight + nLeft + nBot + nTop)) * cRecip;
                        y[curr0] = (y0[curr0] + a * (nRightY + nLeftY + nBotY + nTopY)) * cRecip;
                    }

                    // --- INDEX i + 1 ---
                    if (obstacles[curr1]) {
                        x[curr1] = 0;
                        y[curr1] = 0;
                    } else {
                        int top1 = (i + 1) + topRow;
                        int bot1 = (i + 1) + botRow;
                        int left1 = curr1 - 1;
                        int right1 = curr1 + 1;

                        double nLeft = obstacles[left1] ? -x[curr1] : x[left1];
                        double nRight = obstacles[right1] ? -x[curr1] : x[right1];
                        double nTop = obstacles[top1] ? x[curr1] : x[top1];
                        double nBot = obstacles[bot1] ? x[curr1] : x[bot1];

                        double nLeftY = obstacles[left1] ? y[curr1] : y[left1];
                        double nRightY = obstacles[right1] ? y[curr1] : y[right1];
                        double nTopY = obstacles[top1] ? -y[curr1] : y[top1];
                        double nBotY = obstacles[bot1] ? -y[curr1] : y[bot1];

                        x[curr1] = (x0[curr1] + a * (nRight + nLeft + nBot + nTop)) * cRecip;
                        y[curr1] = (y0[curr1] + a * (nRightY + nLeftY + nBotY + nTopY)) * cRecip;
                    }
                }

                // Rest-Zelle falls ungerade
                for (; i < size - 1; i++) {
                    int curr = i + row;
                    if (obstacles[curr]) {
                        x[curr] = 0;
                        y[curr] = 0;
                    } else {
                        int top = i + topRow;
                        int bot = i + botRow;
                        int left = curr - 1;
                        int right = curr + 1;

                        double nLeft = obstacles[left] ? -x[curr] : x[left];
                        double nRight = obstacles[right] ? -x[curr] : x[right];
                        double nTop = obstacles[top] ? x[curr] : x[top];
                        double nBot = obstacles[bot] ? x[curr] : x[bot];

                        double nLeftY = obstacles[left] ? y[curr] : y[left];
                        double nRightY = obstacles[right] ? y[curr] : y[right];
                        double nTopY = obstacles[top] ? -y[curr] : y[top];
                        double nBotY = obstacles[bot] ? -y[curr] : y[bot];

                        x[curr] = (x0[curr] + a * (nRight + nLeft + nBot + nTop)) * cRecip;
                        y[curr] = (y0[curr] + a * (nRightY + nLeftY + nBotY + nTopY)) * cRecip;
                    }
                }
            }
        }
    }

    // Kombinierter Solver für alle 3 Farbkanäle (Massiver Cache-Gewinn!)
    void diffuseRGB(double[] r, double[] g, double[] b, double[] r0, double[] g0, double[] b0, double diff, double dt, int iter) {
        int size = this.resolution;
        double iterationBonus = 1.0 + (20.0 - iter) * 0.05; // Faktor empirisch anpassen (z.B. bei iter=5 -> ~1.75)
        double a = dt * (diff * iterationBonus) * (size - 2) * (size - 2);
        double cRecip = 1.0 / (1.0 + 4.0 * a);

        for (int k = 0; k < iter; k++) {
            for (int j = 1; j < size - 1; j++) {
                int row = j * size;
                int topRow = row - size;
                int botRow = row + size;

                int i = 1;
                // Verarbeitet R, G und B für 2 Zellen pro Schleifendurchlauf
                for (; i < size - 2; i += 2) {
                    int curr0 = i + row;
                    int curr1 = curr0 + 1;
                    int top0 = i + topRow;
                    int top1 = top0 + 1;
                    int bot0 = i + botRow;
                    int bot1 = bot0 + 1;

                    // Index i
                    r[curr0] = (r0[curr0] + a * (r[curr0 + 1] + r[curr0 - 1] + r[bot0] + r[top0])) * cRecip;
                    g[curr0] = (g0[curr0] + a * (g[curr0 + 1] + g[curr0 - 1] + g[bot0] + g[top0])) * cRecip;
                    b[curr0] = (b0[curr0] + a * (b[curr0 + 1] + b[curr0 - 1] + b[bot0] + b[top0])) * cRecip;

                    // Index i + 1
                    r[curr1] = (r0[curr1] + a * (r[curr1 + 1] + r[curr1 - 1] + r[bot1] + r[top1])) * cRecip;
                    g[curr1] = (g0[curr1] + a * (g[curr1 + 1] + g[curr1 - 1] + g[bot1] + g[top1])) * cRecip;
                    b[curr1] = (b0[curr1] + a * (b[curr1 + 1] + b[curr1 - 1] + b[bot1] + b[top1])) * cRecip;
                }
                for (; i < size - 1; i++) {
                    int curr = i + row;
                    r[curr] = (r0[curr] + a * (r[curr + 1] + r[curr - 1] + r[i + botRow] + r[i + topRow])) * cRecip;
                    g[curr] = (g0[curr] + a * (g[curr + 1] + g[curr - 1] + g[i + botRow] + g[i + topRow])) * cRecip; // Fix Tippfehler im Quellcode
                    b[curr] = (b0[curr] + a * (b[curr + 1] + b[curr - 1] + b[i + botRow] + b[i + topRow])) * cRecip;
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


    void lin_solve(double[] x, double[] x0, int iter) {
        double cRecip = 1.0 / (double) 4;

        for (int k = 0; k < iter; k++) {
            for (int j = 1; j < resolution - 1; j++) {

                // Pointer-Initialisierung für den Zeilenstart (i = 1)
                int idx_current = 1 + j * resolution;
                int idx_left = idx_current - 1;
                int idx_right = idx_current + 1;
                int idx_top = idx_current - resolution;
                int idx_bottom = idx_current + resolution;

                for (int i = 1; i < resolution - 1; i++) {

                    if (obstacles[idx_current]) {
                        x[idx_current] = 0; // Druck/Wert im Hindernis ist Null
                    } else {
                        // Wenn Nachbar ein Hindernis ist, nimm den Wert der aktuellen Zelle (Reflektion)
                        double nRight = obstacles[idx_right] ? x[idx_current] : x[idx_right];
                        double nLeft = obstacles[idx_left] ? x[idx_current] : x[idx_left];
                        double nBottom = obstacles[idx_bottom] ? x[idx_current] : x[idx_bottom];
                        double nTop = obstacles[idx_top] ? x[idx_current] : x[idx_top];

                        x[idx_current] = (x0[idx_current] + (nRight + nLeft + nBottom + nTop)) * cRecip;
                    }

                    // Alle Pointer rücken synchron um genau 1 Zelle weiter (Hardware-Prefetching bleibt aktiv)
                    idx_current++;
                    idx_left++;
                    idx_right++;
                    idx_top++;
                    idx_bottom++;
                }
            }
        }
    }

    void project(double[] velocX, double[] velocY, double[] p, double[] div, int iter) {
        double h = 1.0 / (resolution - 2);

        // 1. Schritt: Divergenz berechnen unter Berücksichtigung der Hindernisse
        for (int j = 1; j < resolution - 1; j++) {
            for (int i = 1; i < resolution - 1; i++) {
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
        lin_solve(p, div, iter);

        // 2. Schritt: Geschwindigkeiten korrigieren (Druckgradient abziehen)
        for (int j = 1; j < resolution - 1; j++) {
            for (int i = 1; i < resolution - 1; i++) {
                int ix = index(i, j);

                if (obstacles[ix]) {
                    velocX[ix] = 0;
                    velocY[ix] = 0;
                    continue;
                }

                int left = indexSafe(i - 1, j);
                int right = indexSafe(i + 1, j);
                int top = indexSafe(i, j - 1);
                int bot = indexSafe(i, j + 1);

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
        double i0, i1, j0, j1;

        double dtx = dt * (resolution - 2);
        double dty = dt * (resolution - 2);

        double s0, s1, t0, t1;
        double tmp1, tmp2, x, y;

        double Nfloat = resolution;
        double ifloat, jfloat;
        int i, j;

        for (j = 1, jfloat = 1; j < resolution - 1; j++, jfloat++) {
            for (i = 1, ifloat = 1; i < resolution - 1; i++, ifloat++) {
                int ix = index(i, j);

                // Wenn die aktuelle Zelle ein Hindernis ist, strömt hier nichts hin
                if (obstacles[ix]) {
                    d[ix] = 0;
                    continue;
                }

                tmp1 = dtx * velocX[ix];
                tmp2 = dty * velocY[ix];
                x = ifloat - tmp1;
                y = jfloat - tmp2;

                // Grenzen des Simulationsbereichs einhalten
                if (x < 0.5) x = 0.5;
                if (x > Nfloat - 1.5) x = Nfloat - 1.5; // Leicht korrigiert für sichere Grid-Indizes
                i0 = Math.floor(x);
                i1 = i0 + 1.0;

                if (y < 0.5) y = 0.5;
                if (y > Nfloat - 1.5) y = Nfloat - 1.5;
                j0 = Math.floor(y);
                j1 = j0 + 1.0;

                s1 = x - i0;
                s0 = 1.0 - s1;
                t1 = y - j0;
                t0 = 1.0 - t1;

                int i0i = (int) (i0);
                int i1i = (int) (i1);
                int j0i = (int) (j0);
                int j1i = (int) (j1);

                // Hindernis-Check für die 4 Interpolations-Nachbarn:
                // Wenn ein Quellpixel im Hindernis liegt, nutzen wir stattdessen das aktuelle Feld (ix)
                int idx00 = obstacles[indexSafe(i0i, j0i)] ? ix : indexSafe(i0i, j0i);
                int idx01 = obstacles[indexSafe(i0i, j1i)] ? ix : indexSafe(i0i, j1i);
                int idx10 = obstacles[indexSafe(i1i, j0i)] ? ix : indexSafe(i1i, j0i);
                int idx11 = obstacles[indexSafe(i1i, j1i)] ? ix : indexSafe(i1i, j1i);

                // Bilineare Interpolation mit den korrigierten Indizes
                d[ix] = s0 * (t0 * d0[idx00] + t1 * d0[idx01]) +
                        s1 * (t0 * d0[idx10] + t1 * d0[idx11]);
            }
        }
    }


    public int resolution() {
        return resolution;
    }

    public void loadFrom(FluidSimulation oldSimulation, int deltaX, int deltaY) {
        for (int x = 1; x < this.resolution - 1; x++) {
            for (int y = 1; y < this.resolution - 1; y++) {

                int xOld = x + deltaX;
                int yOld = y + deltaY;

                if (xOld >= 1 && xOld < oldSimulation.resolution - 1 &&
                    yOld >= 1 && yOld < oldSimulation.resolution - 1) {

                    // Nutze für jedes Objekt die jeweils eigene Index-Arithmetik!
                    int ix = x + y * this.resolution; // Inlined für das neue Grid
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

    public void setObstacle(int x, int y, boolean b) {
        obstacles[indexSafe(x, y)] = b;
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
