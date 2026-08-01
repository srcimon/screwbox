package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Percent;

public record SmokeOptions(double viscosity, double diffusion, int iterations, Percent opacity, Percent fade) {

    //TODO add validations
    public static SmokeOptions vaporPreset() {//TODO find better names and presets
        return new SmokeOptions(0.0000000004, 0.000001, 2, Percent.max(), Percent.of(0.04));
    }

    public SmokeOptions diffusion(final double diffusion) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade);
    }

    public SmokeOptions viscosity(final double viscosity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade);
    }

    public SmokeOptions fade(final Percent fade) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade);
    }

    public SmokeOptions opacity(final Percent opacity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade);
    }

    //TODO baseVelocity
}
