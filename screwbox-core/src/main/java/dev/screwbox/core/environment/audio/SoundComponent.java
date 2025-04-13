package dev.screwbox.core.environment.audio;

import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Camera;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * Adds a constantly playing sound output to an {@link Entity}. The {@link SoundOptions#volume()} and {@link SoundOptions#pan()}
 * will be automatically adjusted by the distance and direction of the {@link Entity} to the {@link Camera}.
 */
public class SoundComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Sound sound;
    public Playback playback;
    
    public SoundComponent(final Supplier<Sound> sound) {
        this(sound.get());
    }

    public SoundComponent(final Sound sound) {
        this.sound = sound;
    }
}
