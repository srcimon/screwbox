package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;

public class AudioConfiguration {

    private record Volume(Percent value, boolean isMuted) {

        Volume() {
            this(Percent.max(), false);
        }

        Volume updatedValue(final Percent value) {
            return new Volume(value, isMuted);
        }

        Percent playbackVolume() {
            return isMuted ? Percent.zero() : value;
        }

        Volume muted(boolean isMuted) {
            return new Volume(value, isMuted);
        }
    }

    private Volume effectVolume = new Volume();
    private Volume musicVolume = new Volume();

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link #playEffect(Sound)} and {@link #playEffectLooped(Sound)}.
     */
    public AudioConfiguration setEffectVolume(final Percent volume) {
        effectVolume = effectVolume.updatedValue(volume);
        //TODO updateVolumeOfActiveClips(volume, false);
        return this;
    }

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link #playMusic(Sound)}.
     */
    public AudioConfiguration setMusicVolume(final Percent volume) {
        musicVolume = musicVolume.updatedValue(volume);
        //TODO updateVolumeOfActiveClips(volume, true);
        return this;
    }

    /**
     * Returns the current volume for effects.
     *
     * @see #musicVolume()
     */
    public Percent effectVolume() {
        return effectVolume.value();
    }

    /**
     * Returns the current volume for music.
     *
     * @see #effectVolume()
     */
    public Percent musicVolume() {
        return musicVolume.value();
    }

    /**
     * Sets music to muted or unmuted.
     *
     * @see #mute()
     * @see #muteMusic()
     * @see #unmuteMusic()
     */
    public AudioConfiguration setMusicMuted(final boolean isMuted) {
        musicVolume = musicVolume.muted(isMuted);
        return this;
    }

    /**
     * Returns {@code true} if music is muted.
     */
    public boolean isMusicMuted() {
        return musicVolume.isMuted();
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
        effectVolume = effectVolume.muted(isMuted);
        return this;
    }

    /**
     * Returns {@code true} if effects are muted.
     */
    public boolean areEffectsMuted() {
        return effectVolume.isMuted();
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
