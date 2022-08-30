package de.suzufa.screwbox.core.audio;

import static java.util.Objects.nonNull;

import java.io.Serializable;

import de.suzufa.screwbox.core.utils.ResourceLoader;

public final class Sound implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] content;

    /**
     * Creats a new {@link Sound} that can be played.
     */
    public static Sound fromFile(final String fileName) {
        if (nonNull(fileName) && !fileName.endsWith(".wav")) {
            throw new IllegalArgumentException("Audio only supports WAV-Files at the moment.");
        }
        return new Sound(ResourceLoader.loadResource(fileName));
    }

    Sound(byte[] content) {
        this.content = content;
    }

    public byte[] content() {
        return content;
    }

}
