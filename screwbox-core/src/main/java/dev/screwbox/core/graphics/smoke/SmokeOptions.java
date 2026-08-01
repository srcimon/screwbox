package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.smoke.styles.OriginalColorSmokeStyle;

public record SmokeOptions(double viscosity, double diffusion, int iterations, Percent opacity, Percent fade,
                           Vector baseVelocity, Percent baseVelocityAdaption, SmokeStyle style) {


    public static SmokeOptions noFade() {
        return new SmokeOptions(Percent.zero());
    }

    public static SmokeOptions slowFade() {
        return new SmokeOptions(Percent.of(0.04));
    }

    public SmokeOptions {
        //TODO add validations
    }

    private SmokeOptions(Percent fade) {
        this(0.0000000004, 0.000001, 2, Percent.max(), fade, null, Percent.of(0.001), new OriginalColorSmokeStyle());
    }

    public SmokeOptions diffusion(final double diffusion) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions viscosity(final double viscosity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions fade(final Percent fade) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions opacity(final Percent opacity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions iterations(final int iterations) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions baseVelocity(final Vector baseVelocity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions baseVelocityAdaption(Percent baseVelocityAdaption) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }

    public SmokeOptions style(SmokeStyle style) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, baseVelocity, baseVelocityAdaption, style);
    }
}
