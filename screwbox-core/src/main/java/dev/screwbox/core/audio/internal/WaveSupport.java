package dev.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class WaveSupport {

    public static final int WAV_HEADER_SIZE = 44;

    private WaveSupport() {

    }

    public static void writeHeaderToBuffer(final int audioDataLength, final AudioFormat format, final ByteBuffer buffer) {
        int sampleRate = (int) format.getSampleRate();
        int bitsPerSample = format.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;

        buffer.put("RIFF".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(44 - 8 + audioDataLength); // 44 ist die Standard-WAV-Header-Größe
        buffer.put("WAVE".getBytes(StandardCharsets.US_ASCII));
        buffer.put("fmt ".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(16); // Subchunk1Size für PCM
        buffer.putShort((short) 1); // AudioFormat (1 = unkomprimiertes PCM)
        buffer.putShort((short) format.getChannels());
        buffer.putInt(sampleRate);
        buffer.putInt(sampleRate * format.getChannels() * bytesPerSample);
        buffer.putShort((short) (format.getChannels() * bytesPerSample));
        buffer.putShort((short) bitsPerSample);
        buffer.put("data".getBytes(StandardCharsets.US_ASCII));
        buffer.putInt(audioDataLength);
    }
}
