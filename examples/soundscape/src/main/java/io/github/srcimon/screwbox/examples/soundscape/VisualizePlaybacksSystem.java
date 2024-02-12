package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;

public class VisualizePlaybacksSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (var playback : engine.audio().activePlaybacks()) {
            Percent percentDone = Percent.of(1.0 * Duration.since(playback.start()).milliseconds()/ playback.sound().duration().milliseconds());
            engine.graphics().world().drawCircle(
                    playback.position().get(),
                    engine.audio().configuration().soundDistance() * percentDone.value(),
                    Color.BLUE.opacity(percentDone.invert()),
                    8);
        }
    }
}
