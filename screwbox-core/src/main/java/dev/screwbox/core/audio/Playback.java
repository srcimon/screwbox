package dev.screwbox.core.audio;

import java.io.Serializable;
import java.util.UUID;

/**
 * Contains information on a {@link Sound} played via {@link Audio}. Can be used to control the playback after it
 * has been started.
 *
 * @param id      unique identifier of the playback
 * @param sound   {@link Sound} played. Can be null when {@link Sound} is created asynchronously (e.g. {@link Audio#speakMumbled(String, Voice)}).
 * @param options {@link SoundOptions} used to play the {@link Sound}.
 * @see Audio#stopPlayback(Playback)
 * @see Audio#updatePlaybackOptions(Playback, SoundOptions)
 */
public record Playback(UUID id, Sound sound, SoundOptions options) implements Serializable {

}
