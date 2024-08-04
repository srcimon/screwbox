package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import static java.util.Objects.isNull;

//TODO add feature
public class SoundSystem implements EntitySystem {

    private static final Archetype SOUNDS = Archetype.of(SoundComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var soundEntity : engine.environment().fetchAll(SOUNDS)) {
            final var soundComponent = soundEntity.get(SoundComponent.class);
            SoundOptions soundOptions = SoundOptions.playContinuously().position(soundEntity.position());
            if (isNull(soundComponent.playback) || !engine.audio().isActive(soundComponent.playback)) {
                soundComponent.playback = engine.audio().playSound(soundComponent.sound, soundOptions);
            } else {
                engine.audio().updatePlaybackOptions(soundComponent.playback, soundOptions);
            }
        }
    }
}
