package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import static java.util.Objects.isNull;

//TODO add System for auto enabling SoundComponent when out of reach

/**
 * Adds a continuously playing {@link Sound} to an {@link Entity} linked to it's position.
 */
public class SoundSystem implements EntitySystem {

    private static final Archetype SOUNDS = Archetype.of(SoundComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(SOUNDS)) {
            final var soundComponent = entity.get(SoundComponent.class);
            SoundOptions soundOptions = SoundOptions.playContinuously().position(entity.position());
            if (isNull(soundComponent.playback) || !engine.audio().isActive(soundComponent.playback)) {
                soundComponent.playback = engine.audio().playSound(soundComponent.sound, soundOptions);
            } else {
                engine.audio().updatePlaybackOptions(soundComponent.playback, soundOptions);
            }
        }
    }
}
