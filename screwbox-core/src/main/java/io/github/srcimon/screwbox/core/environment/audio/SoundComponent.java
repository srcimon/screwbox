package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;
import java.util.UUID;
import java.util.function.Supplier;

public class SoundComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Sound sound;
    public UUID playbackId;

    public SoundComponent(Supplier<Sound> sound) {
        this(sound.get());
    }

    public SoundComponent(Sound sound) {
        this.sound = sound;
    }
}
