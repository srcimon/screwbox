package io.github.srcimon.screwbox.core.audio;

import java.io.Serializable;

//TODO javadoc
public interface Playback extends Serializable {

    Sound sound();

    SoundOptions options();
}
