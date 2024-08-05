package io.github.srcimon.screwbox.core.audio;

import java.io.Serializable;
import java.util.UUID;

//TODO javadoc
public record Playback(UUID id, Sound sound, SoundOptions options) implements Serializable {


}
