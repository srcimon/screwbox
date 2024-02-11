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
     * The source format of the sound data.
     */
    public enum SourceFormat {
        MINI_STEREO,
        MIDI_MONO,
        WAV_STEREO,
        WAV_MONO
    }

    private final byte[] content;
    private final SourceFormat sourceFormat;
    private final Duration duration;

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
        throw new IllegalArgumentException("audio only supports WAV- and MIDI-Files at the moment.");
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
            this.duration = Duration.ofMillis((int) length);
            final var isMono = audioInputStream.getFormat().getFrameSize() <= 2;
            sourceFormat = detectSourceFormat(content, isMono);
            this.content = isMono ? convertToStereo(audioInputStream) : content;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not create sound", e);
        }
    }

    private SourceFormat detectSourceFormat(byte[] content, boolean isMono) {
        final boolean startsWithMidiHeader = content.length >= 4
                && content[0] == 0x4D && content[1] == 0x54 && content[2] == 0x68 && content[3] == 0x64;

        if (startsWithMidiHeader) {
            return isMono ? SourceFormat.MINI_STEREO : SourceFormat.MIDI_MONO;
        }
        return isMono ? SourceFormat.WAV_MONO : SourceFormat.WAV_STEREO;
    }

    /**
     * The binary content of the sound.
     */
    public byte[] content() {
        return content;
    }

    /**
     * Returns the {@link SourceFormat} of the {@link Sound}.
     */
    public SourceFormat sourceFormat() {
        return sourceFormat;
    }

    /**
     * Returns the {@link Duration} of the {@link Sound}.
     */
    public Duration duration() {
        return duration;
    }

}
