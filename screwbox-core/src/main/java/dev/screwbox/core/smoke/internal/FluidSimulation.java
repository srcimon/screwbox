package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.graphics.Color;

import java.util.Arrays;

public class FluidSimulation {

    private final int cells;

    private double[] densityR, densityR0;
    private double[] densityG, densityG0;
    private double[] densityB, densityB0;

    private final double[] velocityX;
    private final double[] velocityX0;
    private final double[] velocityY;
    private final double[] velocityY0;


    public FluidSimulation(final int cells) {
        this.cells = cells;
        this.densityR = new double[this.cells * this.cells];
        this.densityR0 = new double[this.cells * this.cells];
        this.densityG = new double[this.cells * this.cells];
        this.densityG0 = new double[this.cells * this.cells];
        this.densityB = new double[this.cells * this.cells];
        this.densityB0 = new double[this.cells * this.cells];
        this.velocityX = new double[this.cells * this.cells];
        this.velocityX0 = new double[this.cells * this.cells];
        this.velocityY = new double[this.cells * this.cells];
        this.velocityY0 = new double[this.cells * this.cells];
    }


    public void addDensity(final int x, int y, final double amount, Color color) {
        densityR[IX(x, y)] += amount * color.r() / 255.0;
        densityG[IX(x, y)] += amount * color.g() / 255.0;
        densityB[IX(x, y)] += amount * color.b() / 255.0;
    }

    public void addVelocity(final int x, int y, final double amountX, final double amountY) {
        velocityX[IX(x, y)] += amountX;
        velocityY[IX(x, y)] += amountY;
    }


    public DensityInfo densityInfo() {
        return new DensityInfo(cells, Arrays.copyOf(densityR, densityR.length), Arrays.copyOf(densityG, densityG.length),Arrays.copyOf(densityB, densityB.length));
    }

    private int IX(int x, int y) {
        return Math.clamp(x, 0, cells - 1) +
               Math.clamp(y, 0, cells - 1) * cells;
    }

    private int IXFastButUnsave(int x, int y) {
        return x + y * cells;
    }

    public void step(double delta, double visc, double diff, int iter) {

        // diffuse velocities
        diffuse(this.velocityX0, this.velocityX, visc, delta, iter);
        diffuse(this.velocityY0, this.velocityY, visc, delta, iter);

        // clean up so that same amount of fluid is everywhere
        project(this.velocityX0, this.velocityY0, this.velocityX, this.velocityY, iter);

        // advect velocities
        advect(this.velocityX, this.velocityX0, this.velocityX0, this.velocityY0, delta);
        advect(this.velocityY, this.velocityY0, this.velocityX0, this.velocityY0, delta);

        // clean that up
        project(this.velocityX, this.velocityY, this.velocityX0, this.velocityY0, iter);

        // 1. Diffuse all three color channels
        diffuse(this.densityR0, this.densityR, visc, delta, iter);
        diffuse(this.densityG0, this.densityG, visc, delta, iter);
        diffuse(this.densityB0, this.densityB, visc, delta, iter);


        // 2. Advect all three color channels using the solved velocities
        advect(this.densityR, this.densityR0, this.velocityX, this.velocityY, delta);
        advect(this.densityG, this.densityG0, this.velocityX, this.velocityY, delta);
        advect(this.densityB, this.densityB0, this.velocityX, this.velocityY, delta);
    }

    public void fade(double fade) {
        for (int i = 0; i < densityR.length; i++) {
            densityR[i] = Math.max(0, densityR[i] - fade);
            densityG[i] = Math.max(0, densityG[i] - fade);
            densityB[i] = Math.max(0, densityB[i] - fade);
        }
    }

    void diffuse(double[] x, double[] x0, double diff, double dt, int iter) {
        double a = dt * diff * (cells - 2) * (cells - 2);
        double c = 1.0 + 4.0 * a;
        lin_solve(x, x0, a, c, iter);
    }

    void lin_solve(double[] x, double[] x0, double a, double c, int iter) {
        double cRecip = 1.0 / c;

        for (int k = 0; k < iter; k++) {
            // Eine einzige, sequentielle Schleife über alle Zeilen (Keine Streams!)
            for (int j = 1; j < cells - 1; j++) {

                // Pointer-Initialisierung für den Zeilenstart (i = 1)
                int idx_current = 1 + j * cells;
                int idx_left = idx_current - 1;
                int idx_right = idx_current + 1;
                int idx_top = idx_current - cells;
                int idx_bottom = idx_current + cells;

                // Die schnellstmögliche innere Schleife für die CPU (i++)
                // Perfekt linear im Speicher, ideal für das automatische Hardware-Prefetching
                for (int i = 1; i < cells - 1; i++) {
                    x[idx_current] = (x0[idx_current] + a * (
                        x[idx_right] +
                        x[idx_left] +
                        x[idx_bottom] +
                        x[idx_top]
                    )) * cRecip;

                    // Alle Pointer rücken synchron um genau 1 Zelle weiter
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
        double h = 1.0 / (cells - 2);

        for (int j = 1; j < cells - 1; j++) {
            for (int i = 1; i < cells - 1; i++) {
                int ix = IXFastButUnsave(i, j);
                div[ix] = -0.5 * h * (
                    velocX[IX(i + 1, j)]
                    - velocX[IX(i - 1, j)]
                    + velocY[IX(i, j + 1)]
                    - velocY[IX(i, j - 1)]
                );
                p[ix] = 0;
            }
        }
        lin_solve(p, div, 1, 4, iter);

        for (int j = 1; j < cells - 1; j++) {
            for (int i = 1; i < cells - 1; i++) {
                int ix = IXFastButUnsave(i, j);
                velocX[ix] -= 0.5 * (p[IX(i + 1, j)] - p[IX(i - 1, j)]) / h;
                velocY[ix] -= 0.5 * (p[IX(i, j + 1)] - p[IX(i, j - 1)]) / h;
            }
        }
    }

    void advect(double[] d, double[] d0, double[] velocX, double[] velocY, double dt) {
        double i0, i1, j0, j1;

        double dtx = dt * (cells - 2);
        double dty = dt * (cells - 2);

        double s0, s1, t0, t1;
        double tmp1, tmp2, x, y;

        double Nfloat = cells;
        double ifloat, jfloat;
        int i, j;

        for (j = 1, jfloat = 1; j < cells - 1; j++, jfloat++) {
            for (i = 1, ifloat = 1; i < cells - 1; i++, ifloat++) {
                int ix = IXFastButUnsave(i, j);
                tmp1 = dtx * velocX[ix];
                tmp2 = dty * velocY[ix];
                x = ifloat - tmp1;
                y = jfloat - tmp2;

                if (x < 0.5f) x = 0.5;
                if (x > Nfloat + 0.5) x = Nfloat + 0.5;
                i0 = Math.floor(x);
                i1 = i0 + 1.0;
                if (y < 0.5) y = 0.5;
                if (y > Nfloat + 0.5) y = Nfloat + 0.5;
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

                d[ix] = s0 * (t0 * d0[IX(i0i, j0i)] + t1 * d0[IX(i0i, j1i)]) +
                        s1 * (t0 * d0[IX(i1i, j0i)] + t1 * d0[IX(i1i, j1i)]);
            }
        }
    }


    public int size() {
        return cells;
    }

    public void loadFrom(FluidSimulation oldSimulation, int deltaX, int deltaY) {
        // Schleife läuft über das NEUE Gitter (this.cells)
        for (int x = 2; x < this.cells - 2; x++) {
            for (int y = 2; y < this.cells - 2; y++) {

                // Wo lag diese Zelle in der alten Simulation?
                // Wenn die neue Kamera weiter rechts steht (deltaX > 0),
                // müssen wir im alten Gitter weiter rechts lesen (+ deltaX).
                int xOld = x + deltaX;
                int yOld = y + deltaY;

                // ABSOLUTE BRANDMAUER: Prüfe gegen die Dimensionen der ALTEN Simulation!
                if (xOld >= 2 && xOld < oldSimulation.cells - 2 &&
                    yOld >= 2 && yOld < oldSimulation.cells - 2) {

                    // Nutze für jedes Objekt die jeweils eigene Index-Arithmetik!
                    int ix = x + y * this.cells; // Inlined für das neue Grid
                    int ixOld = xOld + yOld * oldSimulation.cells; // Nutzt oldSimulation.cells!

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
                }
            }
        }
    }
}
