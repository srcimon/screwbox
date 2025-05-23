package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Duration;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.ParticlesBundle;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;

/**
 * Adds audio and particle effects to fluids.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 3.3.0
 */
public class FluidEffectsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speedThreshold = 20;
    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(10));
    public double minAudioSpeed = 0.6;
    public double maxAudioSpeed = 1.2;
    public Sound primarySound = SoundBundle.FLUID.get();
    public Sound secondarySound = SoundBundle.FLUID_ALT.get();

    /**
     * Options used when creating particles. Won't spawn any particles when {@code null}.
     */
    public ParticleOptions particleOptions = ParticlesBundle.WATER_SPLASH.get();

    /**
     * Current {@link Playback} associated with this fluid. Will be automatically updated by the {@link FluidEffectsSystem}.
     */
    public Playback playback;
}
