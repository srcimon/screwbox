package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.ShockwaveOptions;

//TODO inline
class Shockwave {
    private final Time startTime;
    private final Vector position;
    private final ShockwaveOptions options;
    private Percent progress = Percent.zero();
    private double radius = 0;
    private double waveWidth = 0;
    private double intensity = 0;

    public Shockwave(final Time startTime, final Vector position, final ShockwaveOptions options) {
        this.position = position;
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

    public double radius() {
        return radius;
    }

    public double intensity() {
        return intensity;
    }

    public double waveWidth() {
        return waveWidth;
    }

    public boolean isFinished() {
        return progress.isMax();
    }
}
