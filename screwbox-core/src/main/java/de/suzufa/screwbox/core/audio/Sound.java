package de.suzufa.screwbox.core.audio;

import static java.util.Objects.nonNull;

import java.io.Serializable;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.utils.Resources;

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

    // TODO: javadoc and test
    public static Asset<Sound> assetFromFile(String fileName) {
        return Asset.asset(() -> fromFile(fileName));
    }

    Sound(byte[] content) {
        this.content = content;
    }

    /**
     * The binary content of the sound.
     */
    public byte[] content() {
        return content;
    }

}
