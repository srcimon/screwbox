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
import java.util.ArrayList;
import java.util.List;

//TODO document

/**
 * Adds audio and particle effects to fluids.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 3.3.0
 */
public class FluidEffectsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
    public Playback playback;
    public double speedThreshold = 15;
    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(10));
    public double minAudioSpeed = 0.6;
    public double maxAudioSpeed = 1.2;
    public List<Sound> sounds = new ArrayList<>(List.of(SoundBundle.FLUID_ALT.get(), SoundBundle.FLUID.get()));
    public ParticleOptions particleOptions = ParticlesBundle.WATER_SPLASH.get();
}
