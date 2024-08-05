package io.github.srcimon.screwbox.core.audio;

import java.io.Serializable;
import java.util.UUID;

//TODO javadoc
public interface Playback extends Serializable {

    UUID id();

    Sound sound();

    SoundOptions options();

}
