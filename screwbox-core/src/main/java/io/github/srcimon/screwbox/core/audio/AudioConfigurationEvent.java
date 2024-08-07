package io.github.srcimon.screwbox.core.audio;

import java.util.EventObject;

/**
 * Occurs on any Change of the {@link AudioConfiguration} properties.
 */
public class AudioConfigurationEvent extends EventObject {

    private final ConfigurationProperty changedProperty;

    /**
     * The property of the {@link AudioConfiguration} changed.
     */
    public enum ConfigurationProperty {

        /**
         * {@link AudioConfiguration#musicVolume()} has been changed.
         */
        MUSIC_VOLUME,

        /**
         * {@link AudioConfiguration#effectVolume()} has been changed.
         */
        EFFECTS_VOLUME,

        /**
         * {@link AudioConfiguration#soundRange()} has been changed.
         */
        SOUND_RANGE,

        /**
         * {@link AudioConfiguration#maxLines()} has been changed.
         */
        MAX_LINES,

        /**
         * {@link AudioConfiguration#microphoneIdleTimeout()} has been changed.
         */
        MICROPHONE_TIMEOUT
    }
    public AudioConfigurationEvent(final Object source, final ConfigurationProperty changedProperty) {
        super(source);
        this.changedProperty = changedProperty;
    }

    public ConfigurationProperty changedProperty() {
        return changedProperty;
    }
}
