package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;

public class AudioConfiguration {

    private Percent effectVolume = Percent.max();
    private Percent musicVolume = Percent.max();
    private boolean isMusicMuted = false;
    private boolean areEffectsMuted = false;

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link Audio#playEffect(Sound)} and {@link Audio#playEffectLooped(Sound)}.
     */
    public AudioConfiguration setEffectVolume(final Percent volume) {
        effectVolume = volume;
        //TODO updateVolumeOfActiveClips(volume, false);
        return this;
    }

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link Audio#playMusic(Sound)}.
     */
    public AudioConfiguration setMusicVolume(final Percent volume) {
        musicVolume = volume;
        //TODO updateVolumeOfActiveClips(volume, true);
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
}
