package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.utils.Resources;

import java.io.Serial;
import java.io.Serializable;

import static java.util.Objects.nonNull;

/**
 * A {@link Sound} that can be played via {@link Engine#audio()}.
 */
public final class Sound implements Serializable {

    @Serial
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
    public static Asset<Sound> assetFromFile(final String fileName) {
        return Asset.asset(() -> fromFile(fileName));
    }

    private Sound(final byte[] content) {
        this.content = content;
    }

    /**
     * The binary content of the sound.
     */
    public byte[] content() {
        return content;
    }

}
