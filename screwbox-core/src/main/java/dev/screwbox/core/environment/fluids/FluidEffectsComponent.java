package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Duration;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.ParticlesBundle;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public double minAudioSpeed = 0.8;
    public double maxAudioSpeed = 1.2;
    public final List<Sound> sounds;
    public double soundSuppressionRange = 80;

    public FluidEffectsComponent() {
        this(SoundBundle.FLUID.get(), SoundBundle.FLUID_ALT.get());
    }

    public FluidEffectsComponent(Sound... sounds) {
        this.sounds = new ArrayList<>(Arrays.asList(sounds));
    }



    /**
     * Options used when creating particles. Won't spawn any particles when {@code null}.
     */
    public ParticleOptions particleOptions = ParticlesBundle.WATER_SPLASH.get();

}
