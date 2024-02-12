package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;

public class VisualizePlaybacksSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.audio().activePlaybacks().stream().filter(playback -> playback.position().isPresent()).forEach(playback -> {
            final var percentDone = 1.0 * Duration.since(playback.start()).milliseconds() / playback.sound().duration().milliseconds();
            final var position = playback.position().get();
            final var diameter = engine.audio().configuration().soundDistance() * percentDone;
            final var color = Color.BLUE.opacity(1 - percentDone);
            engine.graphics().world().drawCircle(position, diameter, color, 8);
        });
    }
}
