package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.internal.AudioAdapter;
import io.github.srcimon.screwbox.core.utils.Resources;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import static io.github.srcimon.screwbox.core.audio.internal.AudioAdapter.convertToStereo;
import static java.util.Objects.requireNonNull;

/**
 * A {@link Sound} that can be played via {@link Engine#audio()}.
 */
public final class Sound implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The format of the sound data.
     */
    public enum Format {
        MIDI,
        WAV
    }

    private final byte[] content;
    private final Format format;
    private final Duration duration;
    private final boolean isArtificalStereo;

    /**
     * Returns a short dummy sound effect.
     */
    public static Sound dummyEffect() {
        return fromFile("assets/sounds/dummy_effect.wav");
    }

    /**
     * Creates a new {@link Sound} from file. Only supports WAV- and MIDI-Files at the moment.
     */
    public static Sound fromFile(final String fileName) {
        requireNonNull(fileName, "fileName must not be null");
        if (fileName.endsWith(".wav") || fileName.endsWith(".mid")) {
            return fromSoundData(Resources.loadBinary(fileName));
        }
        throw new IllegalArgumentException("Audio only supports WAV- and MIDI-Files at the moment.");
    }

    /**
     * Creates a new {@link Sound} from audio data.
     */
    public static Sound fromSoundData(final byte[] content) {
        return new Sound(content);
    }

    /**
     * Creates a new {@link Asset} for a {@link Sound}. Only supports WAV-Files at
     * the moment.
     */
    public static Asset<Sound> assetFromFile(final String fileName) {
        return Asset.asset(() -> fromFile(fileName));
    }

    private Sound(final byte[] content) {
        requireNonNull(content, "content must not be null");
        try (AudioInputStream audioInputStream = AudioAdapter.getAudioInputStream(content)) {
            final var length = 1000.0 * audioInputStream.getFrameLength() / audioInputStream.getFormat().getFrameRate();
            this.isArtificalStereo = audioInputStream.getFormat().getFrameSize() <= 2;
            this.duration = Duration.ofMillis((int) length);
            this.format = startsWithMidiHeader(content) ? Format.MIDI : Format.WAV;
            this.content = isArtificalStereo ? convertToStereo(audioInputStream) : content;
        } catch (IOException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

    /**
     * The binary content of the sound.
     */
    public byte[] content() {
        return content;
    }

    /**
     * Returns the {@link Format} of the {@link Sound}.
     */
    public Format format() {
        return format;
    }

    /**
     * Returns the {@link Duration} of the {@link Sound}.
     */
    public Duration duration() {
        return duration;
    }

    /**
     * Returns {@code true} if the {@link Sound} was updated from mono to stereo via code.
     * Stereo is required to apply some effects like balance and pan.
     */
    //TODO: link javadoc to balance and pan
    public boolean isArtificalStereo() {
        return isArtificalStereo;
    }

    private boolean startsWithMidiHeader(final byte[] data) {
        return data.length >= 4 && data[0] == 0x4D && data[1] == 0x54 && data[2] == 0x68 && data[3] == 0x64;
    }
}
