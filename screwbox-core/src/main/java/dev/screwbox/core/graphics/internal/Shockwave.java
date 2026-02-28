package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.ShockwaveOptions;

//TODO inline
class Shockwave {
    public double x, y, radius, waveWidth, intensity;
    public ShockwaveOptions options;

    public Shockwave(Vector position, ShockwaveOptions options) {
        this.x = position.x();
        this.y = position.y();
        this.radius = 0;
        this.options = options;
        this.intensity = options.initialIntensity();
    }

    public void update(double delta) {
        this.radius += delta * options.speed();
        this.waveWidth = 30 + (radius * 0.2);
        double progress = radius / options.maxRadius();
        double smoothFade = Math.cos(progress * Math.PI / 2); // Cosinus-Fade
        this.intensity = options.initialIntensity() * smoothFade;
    }
}
