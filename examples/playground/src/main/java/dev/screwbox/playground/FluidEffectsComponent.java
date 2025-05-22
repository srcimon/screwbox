package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class FluidEffectsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(20));
    public Scheduler soundScheduler = Scheduler.withInterval(Duration.ofMillis(800));
    public ArrayList<Sound> sounds = new ArrayList<>(List.of(SoundBundle.FLUID_ALT.get(), SoundBundle.FLUID.get()));
    public ParticleOptions particleOptions = ParticleOptions.unknownSource()
            .chaoticMovement(60, Duration.ofSeconds(1))
            .animateOpacity(Percent.of(0.1), Percent.of(0.2))
            .sprite(Sprite.animatedFromFile("splash_anim.png", Size.square(16), Duration.ofMillis(150)))
            .randomRotation(-0.2, 0.2)
            .randomBaseSpeed(10)
            .ease(Ease.SINE_IN_OUT)
            .randomRotation(0.25)
            .randomLifeTimeMilliseconds(400, 800)
            .animateScale(0.5, 0.6);
}
