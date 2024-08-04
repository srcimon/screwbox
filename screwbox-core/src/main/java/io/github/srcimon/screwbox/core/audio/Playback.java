package io.github.srcimon.screwbox.core.audio;

import java.util.UUID;

//TODO javadoc
public interface Playback {

    UUID id();

    Sound sound();

    SoundOptions options();

}
