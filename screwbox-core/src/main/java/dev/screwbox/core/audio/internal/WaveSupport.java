package dev.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.US_ASCII;

public final class WaveSupport {

    public static final int WAV_HEADER_SIZE = 44;

    private WaveSupport() {

    }

    public static void writeHeaderToBuffer(final int audioDataLength, final AudioFormat format, final ByteBuffer buffer) {
        final int sampleRate = (int) format.getSampleRate();
        final int bitsPerSample = format.getSampleSizeInBits();
        final int bytesPerSample = bitsPerSample / 8;

        buffer.put("RIFF".getBytes(US_ASCII));
        buffer.putInt(WAV_HEADER_SIZE - 8 + audioDataLength);
        buffer.put("WAVE".getBytes(US_ASCII));
        buffer.put("fmt ".getBytes(US_ASCII));
        buffer.putInt(16);
        buffer.putShort((short) 1);
        buffer.putShort((short) format.getChannels());
        buffer.putInt(sampleRate);
        buffer.putInt(sampleRate * format.getChannels() * bytesPerSample);
        buffer.putShort((short) (format.getChannels() * bytesPerSample));
        buffer.putShort((short) bitsPerSample);
        buffer.put("data".getBytes(US_ASCII));
        buffer.putInt(audioDataLength);
    }
}
