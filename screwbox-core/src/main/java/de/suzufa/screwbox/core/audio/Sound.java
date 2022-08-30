package de.suzufa.screwbox.core.audio;

import java.io.Serializable;

import de.suzufa.screwbox.core.utils.ResourceLoader;

public final class Sound implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] content;

    public static Sound fromFile(final String fileName) {
        return new Sound(ResourceLoader.loadResource(fileName));
    }

    Sound(byte[] content) {
        this.content = content;
    }

    public byte[] content() {
        return content;
    }

}
