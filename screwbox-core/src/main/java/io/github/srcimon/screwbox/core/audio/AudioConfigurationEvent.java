package io.github.srcimon.screwbox.core.audio;

import java.util.EventObject;

/**
 * Occurs on any Change of the {@link AudioConfiguration} properties.
 */
public class AudioConfigurationEvent extends EventObject {

    private final ConfigurationProperty changedProperty;

    //TODO add javadoc
    public enum ConfigurationProperty {
        MUSIC_VOLUME,
        EFFECTS_VOLUME,
        SOUND_RANGE,
        MAX_LINES,
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
