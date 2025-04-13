package dev.screwbox.core.audio.internal;

import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundOptions;

import javax.sound.sampled.SourceDataLine;
import java.util.UUID;

class ActivePlayback {

    private final UUID id;
    private final Sound sound;
    private SoundOptions options;
    private SourceDataLine line;

    public ActivePlayback(final Sound sound, final SoundOptions options) {
        this.id = UUID.randomUUID();
        this.sound = sound;
        this.options = options;
    }

    public void setOptions(final SoundOptions options) {
        this.options = options;
    }

    public void setLine(final SourceDataLine line) {
        this.line = line;
    }

    public Sound sound() {
        return sound;
    }

    public SoundOptions options() {
        return options;
    }

    public SourceDataLine line() {
        return line;
    }

    public UUID id() {
        return id;
    }

    public Playback toPlayback() {
        return new Playback(id, sound, options);
    }
}
