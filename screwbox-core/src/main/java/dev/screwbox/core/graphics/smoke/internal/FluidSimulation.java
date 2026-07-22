package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Size;

public class FluidSimulation {

    private final int width;
    private final int height;
    private final int N;

    private final double[] density;
    private final double[] density0;

    private final double[] velocityX;
    private final double[] velocityX0;
    private final double[] velocityY;
    private final double[] velocityY0;


    public FluidSimulation(final Size size) {
        this.width = size.width();
        this.height = size.height();
        this.N = size.width();//TODO get rid of N
        this.density = new double[width * height];
        this.density0 = new double[width * height];
        this.velocityX = new double[width * height];
        this.velocityX0 = new double[width * height];
        this.velocityY = new double[width * height];
        this.velocityY0 = new double[width * height];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void addDensity(final int x, int y, final double amount) {
        density[IX(x, y)] += amount;
    }

    public void addVelocity(final int x, int y, final double amountX, final double amountY) {
        velocityX[IX(x, y)] += amountX;
        velocityY[IX(x, y)] += amountY;
    }

    public double density(final int x, final int y) {
        return density[IX(x, y)];
    }

    private int IX(int x, int y) {
        return Math.clamp(x, 0, N - 1) +
               Math.clamp(y, 0, N - 1) * N;
    }

    public void step(double delta, double visc, double diff, int iter) {

        // diffuse velocities
        diffuse(1, this.velocityX0, this.velocityX, visc, delta, iter);
        diffuse(2, this.velocityY0, this.velocityY, visc, delta, iter);

        // clean up so that same amount of fluid is everywhere
        project(this.velocityX0, this.velocityY0, this.velocityX, this.velocityY, iter);

        // advect velocities
        advect(1, this.velocityX, this.velocityX0, this.velocityX0, this.velocityY0, delta);
        advect(2, this.velocityY, this.velocityY0, this.velocityX0, this.velocityY0, delta);

        // clean that up
        project(this.velocityX, this.velocityY, this.velocityX0, this.velocityY0, iter);

        diffuse(0, this.density0, this.density, diff, delta, iter);
        advect(0, this.density, this.density0, this.velocityX, this.velocityY, delta);
    }

    public void fade(double fade) {
        for (int i = 0; i < density.length; i++) {
            density[i] = Math.max(0, density[i] - fade);
        }
    }

    void diffuse(int b, double[] x, double[] x0, double diff, double dt, int iter) {
        double a = dt * diff * (N - 2) * (N - 2);
        double c = 1.0 + 4.0 * a;
        lin_solve(b, x, x0, a, c, iter);
    }

    void lin_solve(int b, double[] x, double[] x0, double a, double c, int iter) {
        double cRecip = 1.0 / c;

        for (int k = 0; k < iter; k++) {
            for (int j = 1; j < N - 1; j++) {
                int idx_current = 1 + j * N;
                int idx_left = idx_current - 1;
                int idx_right = idx_current + 1;
                int idx_top = idx_current - N;
                int idx_bottom = idx_current + N;

                for (int i = 1; i < N - 1; i++) {
                    x[idx_current] = (x0[idx_current] + a * (
                        x[idx_right] +
                        x[idx_left] +
                        x[idx_bottom] +
                        x[idx_top]
                    )) * cRecip;

                    idx_current++;
                    idx_left++;
                    idx_right++;
                    idx_top++;
                    idx_bottom++;
                }
            }
            set_bnd(b, x);
        }
    }

    void project(double[] velocX, double[] velocY, double[] p, double[] div, int iter) {
        for (int j = 1; j < N - 1; j++) {
            for (int i = 1; i < N - 1; i++) {
                div[IX(i, j)] = -0.5 * (
                    velocX[IX(i + 1, j)]
                    - velocX[IX(i - 1, j)]
                    + velocY[IX(i, j + 1)]
                    - velocY[IX(i, j - 1)]
                ) / N;
                p[IX(i, j)] = 0;
            }
        }
        set_bnd(0, div);
        set_bnd(0, p);

        lin_solve(0, p, div, 1, 4, iter);

        for (int j = 1; j < N - 1; j++) {
            for (int i = 1; i < N - 1; i++) {
                velocX[IX(i, j)] -= 0.5 * (p[IX(i + 1, j)] - p[IX(i - 1, j)]);
                velocY[IX(i, j)] -= 0.5 * (p[IX(i, j + 1)] - p[IX(i, j - 1)]);
            }
        }
        set_bnd(1, velocX);
        set_bnd(2, velocY);
    }

    void advect(int b, double[] d, double[] d0, double[] velocX, double[] velocY, double dt) {
        double i0, i1, j0, j1;

        double dtx = dt * (N - 2);
        double dty = dt * (N - 2);

        double s0, s1, t0, t1;
        double tmp1, tmp2, x, y;

        double Nfloat = N;
        double ifloat, jfloat;
        int i, j;

        for (j = 1, jfloat = 1; j < N - 1; j++, jfloat++) {
            for (i = 1, ifloat = 1; i < N - 1; i++, ifloat++) {
                tmp1 = dtx * velocX[IX(i, j)];
                tmp2 = dty * velocY[IX(i, j)];
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

                d[IX(i, j)] = s0 * (t0 * d0[IX(i0i, j0i)] + t1 * d0[IX(i0i, j1i)]) +
                              s1 * (t0 * d0[IX(i1i, j0i)] + t1 * d0[IX(i1i, j1i)]);
            }
        }
        set_bnd(b, d);
    }

    void set_bnd(int b, double[] x) {
        for (int i = 1; i < N - 1; i++) {
            x[IX(i, 0)] = b == 2 ? -x[IX(i, 1)] : x[IX(i, 1)];
            x[IX(i, N - 1)] = b == 2 ? -x[IX(i, N - 2)] : x[IX(i, N - 2)];
        }
        for (int j = 1; j < N - 1; j++) {
            x[IX(0, j)] = b == 1 ? -x[IX(1, j)] : x[IX(1, j)];
            x[IX(N - 1, j)] = b == 1 ? -x[IX(N - 2, j)] : x[IX(N - 2, j)];
        }

        x[IX(0, 0)] = 0.5 * (x[IX(1, 0)] + x[IX(0, 1)]);
        x[IX(0, N - 1)] = 0.5 * (x[IX(1, N - 1)] + x[IX(0, N - 2)]);
        x[IX(N - 1, 0)] = 0.5 * (x[IX(N - 2, 0)] + x[IX(N - 1, 1)]);
        x[IX(N - 1, N - 1)] = 0.5 * (x[IX(N - 2, N - 1)] + x[IX(N - 1, N - 2)]);
    }
}
