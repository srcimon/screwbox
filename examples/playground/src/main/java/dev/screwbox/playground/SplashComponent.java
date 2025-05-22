package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.assets.Asset;
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
    public Scheduler soundScheduler = Scheduler.withInterval(Duration.ofMillis(800));
    public Asset<Sound> sound = SoundBundle.FLUID.asset();//TODO not serializable use sound
}
