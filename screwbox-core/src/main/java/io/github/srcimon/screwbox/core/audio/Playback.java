package io.github.srcimon.screwbox.core.audio;

import java.util.UUID;

public interface Playback {

    UUID id();

    SoundOptions options();
}
