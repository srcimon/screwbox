package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SplashComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(10));
    public Scheduler soundScheduler = Scheduler.withInterval(Duration.ofMillis(400));
    public List<Sound> sounds = new ArrayList<>(List.of(SoundBundle.SPLASH.get()));
}
