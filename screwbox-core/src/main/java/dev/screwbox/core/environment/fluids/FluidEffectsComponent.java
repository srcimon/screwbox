package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Duration;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.ParticlesBundle;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Adds audio and particle effects to fluids.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 3.3.0
 */
public class FluidEffectsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Minimum speed of the physics {@link Entity} to create an effect.
     */
    public double speedThreshold = 20;

    /**
     * Scheduler for effect creation.
     */
    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(10));

    /**
     * Minimum speed of the played sounds.
     */
    public double minAudioSpeed = 0.8;

    /**
     * Maximum speed of the played sounds.
     */
    public double maxAudioSpeed = 1.2;

    /**
     * List of played sounds. Will pick random {@link Sound} out of this list. Won't play sounds at all when empty.
     */
    public final List<Sound> sounds;

    /**
     * Minimum distance to other active playback to avoid sound spamming.
     */
    public double soundSuppressionRange = 80;

    /**
     * Options used when creating particles. Won't spawn any particles when {@code null}.
     */
    public ParticleOptions particleOptions = ParticlesBundle.WATER_SPLASH.get();

    /**
     * Creates a new instance using default sounds.
     */
    public FluidEffectsComponent() {
        this(List.of(SoundBundle.FLUID.get(), SoundBundle.FLUID_ALT.get()));
    }

    /**
     * Creates a new instance using specified sounds.
     */
    public FluidEffectsComponent(final List<Sound> sounds) {
        this.sounds = new ArrayList<>(sounds);
    }

}
