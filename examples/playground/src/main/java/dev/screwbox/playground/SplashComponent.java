package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

import java.util.List;

public class SplashComponent implements Component {

    public Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(10));
    public Scheduler soundScheduler = Scheduler.withInterval(Duration.ofMillis(400));
    public List<Sound> sounds = List.of(SoundBundle.SPLASH.get());
}
