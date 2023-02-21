package io.github.simonbas.screwbox.core.audio;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.utils.Resources;

import java.io.Serializable;

import static java.util.Objects.nonNull;

/**
 * A {@link Sound} that can be played via {@link Engine#audio()}.
 */
public final class Sound implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] content;

    /**
     * Creates a new {@link Sound} from file. Only supports WAV-Files at the moment.
     */
    public static Sound fromFile(final String fileName) {
        if (nonNull(fileName) && !fileName.endsWith(".wav")) {
            throw new IllegalArgumentException("Audio only supports WAV-Files at the moment.");
        }
        return new Sound(Resources.loadBinary(fileName));
    }

    /**
     * Creates a new {@link Asset} for a {@link Sound}. Only supports WAV-Files at
     * the moment.
     */
    public static Asset<Sound> assetFromFile(String fileName) {
        return Asset.asset(() -> fromFile(fileName));
    }

    private Sound(byte[] content) {
        this.content = content;
    }

    /**
     * The binary content of the sound.
     */
    public byte[] content() {
        return content;
    }

}
