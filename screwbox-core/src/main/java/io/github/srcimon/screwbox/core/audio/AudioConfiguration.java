package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Read and change the current {@link AudioConfiguration}.
 */
public class AudioConfiguration {

    private Percent effectVolume = Percent.max();
    private Percent musicVolume = Percent.max();
    private boolean isMusicMuted = false;
    private boolean areEffectsMuted = false;
    private double soundRange = 1024;
    private Duration microphoneTimeout = Duration.ofSeconds(5);
    private int maxLines = 64;

    private final List<AudioConfigurationListener> listeners = new ArrayList<>();

    //TODO  test
    /**
     * Sets the maximum number of audio lines used by the engine. This specifies the max count of parallel sounds.
     */
    public AudioConfiguration setMaxLines(final int maxLines) {
        Validate.positive(maxLines, "max lines must be positive");
        this.maxLines = maxLines;
        notifyListeners(ConfigurationProperty.MAX_LINES);
        return this;
    }

    //TODO test
    /**
     * Returns the maximum number of audio lines used by the engine. This specifies the max count of parallel sounds.
     */
    public int maxLines() {
        return maxLines;
    }

    /**
     * Sets the sound range that is used to determin {@link SoundOptions#pan()} and {@link SoundOptions#volume()}
     * when {@link Sound} is played based on {@link SoundOptions#position()}.
     */
    public AudioConfiguration setSoundRange(final double soundRange) {
        Validate.positive(soundRange, "sound range must be positive");
        this.soundRange = soundRange;
        notifyListeners(ConfigurationProperty.SOUND_RANGE);
        return this;
    }

    /**
     * Sets timeout for the microphone to turn off after no further reading via {@link Audio#microphoneLevel()}.
     */
    public AudioConfiguration setMicrophoneIdleTimeout(final Duration timeout) {
        microphoneTimeout = requireNonNull(timeout, "timeout must not be null");
        notifyListeners(ConfigurationProperty.MICROPHONE_TIMEOUT);
        return this;
    }

    /**
     * Gets the timout after that the microphone turns off after no further reading via {@link Audio#microphoneLevel()}.
     */
    public Duration microphoneIdleTimeout() {
        return microphoneTimeout;
    }

    /**
     * Gets the sound range that is used to determin {@link SoundOptions#pan()} and {@link SoundOptions#volume()}
     * when {@link Sound} is played based on {@link SoundOptions#position()}.
     */
    public double soundRange() {
        return soundRange;
    }

    public AudioConfiguration addListener(final AudioConfigurationListener listener) {
        listeners.add(requireNonNull(listener, "listener must not be null"));
        return this;
    }

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link Audio#playSound(Sound)}.
     */
    public AudioConfiguration setEffectVolume(final Percent volume) {
        effectVolume = volume;
        notifyListeners(ConfigurationProperty.EFFECTS_VOLUME);
        return this;
    }

    /**
     * Sets the volume of all {@link Sound}s that are played having {@link SoundOptions#isMusic()}
     */
    public AudioConfiguration setMusicVolume(final Percent volume) {
        musicVolume = volume;
        notifyListeners(ConfigurationProperty.MUSIC_VOLUME);
        return this;
    }

    /**
     * Returns the current volume for effects.
     *
     * @see #musicVolume()
     */
    public Percent effectVolume() {
        return effectVolume;
    }

    /**
     * Returns the current volume for music.
     *
     * @see #effectVolume()
     */
    public Percent musicVolume() {
        return musicVolume;
    }

    /**
     * Sets music to muted or unmuted.
     *
     * @see #mute()
     * @see #muteMusic()
     * @see #unmuteMusic()
     */
    public AudioConfiguration setMusicMuted(final boolean isMuted) {
        isMusicMuted = isMuted;
        notifyListeners(ConfigurationProperty.MUSIC_VOLUME);
        return this;
    }

    /**
     * Returns {@code true} if music is muted.
     */
    public boolean isMusicMuted() {
        return isMusicMuted;
    }

    /**
     * Mutes music playback.
     *
     * @see #mute()
     * @see #setMusicMuted(boolean)
     * @see #unmuteMusic()
     */
    public AudioConfiguration muteMusic() {
        return setMusicMuted(true);
    }

    /**
     * Unmutes music playback.
     *
     * @see #mute()
     * @see #setMusicMuted(boolean)
     * @see #muteMusic()
     */
    public AudioConfiguration unmuteMusic() {
        return setMusicMuted(false);
    }

    /**
     * Sets effects to muted or unmuted.
     *
     * @see #mute()
     * @see #muteEffects()
     * @see #unmuteEffects()
     */
    public AudioConfiguration setEffectsMuted(final boolean isMuted) {
        areEffectsMuted = isMuted;
        notifyListeners(ConfigurationProperty.EFFECTS_VOLUME);
        return this;
    }

    /**
     * Returns {@code true} if effects are muted.
     */
    public boolean areEffectsMuted() {
        return areEffectsMuted;
    }

    /**
     * Mutes effects playback.
     *
     * @see #mute()
     * @see #setEffectsMuted(boolean)
     * @see #unmuteEffects()
     */
    public AudioConfiguration muteEffects() {
        return setEffectsMuted(true);
    }

    /**
     * Unmutes effects playback.
     *
     * @see #mute()
     * @see #muteEffects()
     * @see #setMusicMuted(boolean)
     */
    public AudioConfiguration unmuteEffects() {
        return setEffectsMuted(false);
    }

    /**
     * Sets all playback muted or unmuted.
     *
     * @see #isMuted()
     * @see #setEffectsMuted(boolean)
     * @see #setMusicMuted(boolean)
     */
    public AudioConfiguration setMuted(final boolean isMuted) {
        setMusicMuted(isMuted);
        setEffectsMuted(isMuted);
        return this;
    }

    /**
     * Returns {@code true} if all playback is muted.
     *
     * @see #setMuted(boolean)
     */
    public boolean isMuted() {
        return isMusicMuted() && areEffectsMuted();
    }

    /**
     * Mutes all playback.
     *
     * @see #setMuted(boolean)
     * @see #isMuted()
     */
    public AudioConfiguration mute() {
        return setMuted(true);
    }

    /**
     * Unmutes all playback.
     *
     * @see #mute()
     * @see #isMuted()
     */
    public AudioConfiguration unmute() {
        return setMuted(false);
    }

    private void notifyListeners(final ConfigurationProperty configurationProperty) {
        final var audioConfigurationEvent = new AudioConfigurationEvent(this, configurationProperty);
        for (final var listener : listeners) {
            listener.configurationChanged(audioConfigurationEvent);
        }
    }

}
