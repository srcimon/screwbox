package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static io.github.srcimon.screwbox.core.audio.Sound.dummyEffect;

public class PlaySoundOnClickSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isPressedLeft()) {
            engine.audio().playEffect(dummyEffect(), engine.mouse().position());
        }
    }
}
