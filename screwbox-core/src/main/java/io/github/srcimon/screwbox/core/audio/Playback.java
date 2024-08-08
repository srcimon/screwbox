package io.github.srcimon.screwbox.core.audio;

import java.io.Serializable;
import java.util.UUID;

/**
 * Contains information on a {@link Sound} played via {@link Audio}.
 * Can be used to control the playback after it has been started.
 *
 * @see Audio#stopPlayback(Playback)
 * @see Audio#updatePlaybackOptions(Playback, SoundOptions)
 *
 * @param id      unique identifier of the playback
 * @param sound   {@link Sound} played
 * @param options {@link SoundOptions} used to play the {@link Sound}.
 */
public record Playback(UUID id, Sound sound, SoundOptions options) implements Serializable {

}
