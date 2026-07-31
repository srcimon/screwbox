package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Percent;

public record SmokeOptions(double viscosity, double diffusion, int iterations, Percent fade) {

    //TODO add validations
    public static SmokeOptions vaporPreset() {//TODO find better names and presets
        return new SmokeOptions(0.0000000004, 0.000001, 2, Percent.of(0.04));
    }

    public SmokeOptions diffusion(final double diffusion) {
        return new SmokeOptions(viscosity, diffusion, iterations, fade);
    }

    public SmokeOptions viscosity(final double viscosity) {
        return new SmokeOptions(viscosity, diffusion, iterations, fade);
    }

    public SmokeOptions fade(final Percent fade) {
        return new SmokeOptions(viscosity, diffusion, iterations, fade);
    }
}
