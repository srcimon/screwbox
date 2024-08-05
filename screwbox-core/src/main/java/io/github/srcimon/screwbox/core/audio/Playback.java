package io.github.srcimon.screwbox.core.audio;

import java.io.Serializable;
import java.util.UUID;

//TODO javadoc
public interface Playback extends Serializable {

    Sound sound();

    SoundOptions options();

    UUID id();

}
