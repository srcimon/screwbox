package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.ShockwaveOptions;

//TODO inline
class Shockwave {
    private final Time startTime;
    private Vector position;
    public double radius, waveWidth, intensity;
    private final ShockwaveOptions options;
    private Percent progress = Percent.zero();

    public Shockwave(final Time startTime, final Vector position, final ShockwaveOptions options) {
        this.position = position;
        this.radius = 0;
        this.options = options;
        this.intensity = options.intensity();
        this.startTime = startTime;
    }

    public ShockwaveOptions options() {
        return options;
    }

    public Vector position() {
        return position;
    }

    public void update(final Time now) {
        progress = options.duration().progress(startTime, now);
        radius = progress.value() * options.radius();
        waveWidth = 30 + (radius * 0.2);
        intensity = options.intensity() * Math.cos(progress.value() * Math.PI / 2);
    }

    public boolean isFinished() {
        return progress.isMax();
    }
}
