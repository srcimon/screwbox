package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import java.io.Serial;
import java.util.UUID;

class ActivePlayback implements Playback {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @Override
    public Sound sound() {
        return sound;
    }

    @Override
    public SoundOptions options() {
        return options;
    }

    public SourceDataLine line() {
        return line;
    }

}
