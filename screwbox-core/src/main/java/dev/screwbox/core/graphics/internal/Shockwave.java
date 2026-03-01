package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.ShockwaveOptions;

//TODO inline
class Shockwave {
    private final Time startTime;
    public Vector position;
    public double radius, waveWidth, intensity;
    public ShockwaveOptions options;
    public Percent progress = Percent.zero();

    public Shockwave(final Time startTime, final Vector position, final ShockwaveOptions options) {
        this.position = position;
        this.radius = 0;
        this.options = options;
        this.intensity = options.intensity();
        this.startTime = startTime;
    }

    public void update(final Time now) {
        progress = options.duration().progress(startTime, now);
        radius = progress.value() * options.maxRadius();
        waveWidth = 30 + (radius * 0.2);
        intensity = options.intensity() * Math.cos(progress.value() * Math.PI / 2);
    }

}
