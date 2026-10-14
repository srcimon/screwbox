package dev.screwbox.core.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Prototype {

    public enum WaveType { SINE, SQUARE, TRIANGLE }

    // Festgelegtes Format für maximale macOS-Kompatibilität und Lautstärke
    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 1; // Mono

    private final static Random random = new Random();

    /**
     * Generiert ein fertiges WAV-formatiertes byte[] Array (inklusive Format-Header).
     * Dieses Array kann auf macOS direkt verarbeitet oder abgespeichert werden.
     */
    public static byte[] generateMumbleWavByteArray(int textLength, double baseFreq, WaveType waveType) {
        double blipDurationSeconds = 0.08;
        int samplesPerBlip = (int) (SAMPLE_RATE * blipDurationSeconds);
        int totalSamples = samplesPerBlip * textLength;
        int dataSize = totalSamples * (BITS_PER_SAMPLE / 8);

        // Gesamte Array-Größe: 44 Bytes WAV-Header + die rohen Audio-Daten
        int totalFileSize = 44 + dataSize;
        byte[] outputBuffer = new byte[totalFileSize];

        // WAV-Header erfordern strikt LITTLE_ENDIAN
        ByteBuffer buffer = ByteBuffer.wrap(outputBuffer).order(ByteOrder.LITTLE_ENDIAN);

        // ---------------------------------------------------------------------
        // 1. SCHREIBE DEN RIFF-WAV HEADER (Die Format-Informationen)
        // ---------------------------------------------------------------------
        buffer.put("RIFF".getBytes());                      // ChunkID
        buffer.putInt(totalFileSize - 8);                   // ChunkSize
        buffer.put("WAVE".getBytes());                      // Format

        buffer.put("fmt ".getBytes());                      // Subchunk1ID
        buffer.putInt(16);                                  // Subchunk1Size (16 für PCM)
        buffer.putShort((short) 1);                         // AudioFormat (1 = unkomprimiertes PCM)
        buffer.putShort((short) CHANNELS);                  // NumChannels
        buffer.putInt(SAMPLE_RATE);                         // SampleRate
        buffer.putInt(SAMPLE_RATE * CHANNELS * (BITS_PER_SAMPLE / 8)); // ByteRate
        buffer.putShort((short) (CHANNELS * (BITS_PER_SAMPLE / 8)));   // BlockAlign
        buffer.putShort((short) BITS_PER_SAMPLE);           // BitsPerSample

        buffer.put("data".getBytes());                      // Subchunk2ID
        buffer.putInt(dataSize);                            // Subchunk2Size

        // ---------------------------------------------------------------------
        // 2. RETRO AUDIO-DATA SAMPLES GENERIEREN
        // ---------------------------------------------------------------------
        for (int i = 0; i < textLength; i++) {
            // Lebendiger Pitch-Wechsel pro Buchstabe (+/- 35%)
            double randomPitchOffset = (random.nextDouble() * 0.7) - 0.35;
            double targetFrequency = baseFreq * (1.0 + randomPitchOffset);

            boolean isPause = random.nextInt(6) == 0;
            double volumeModifier = isPause ? 0.0 : (0.7 + random.nextDouble() * 0.3); // Hohe Grundlautstärke

            for (int s = 0; s < samplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;
                double angle = 2.0 * Math.PI * targetFrequency * time;
                double rawValue = 0.0;

                switch (waveType) {
                    case SINE:     rawValue = Math.sin(angle); break;
                    case SQUARE:   rawValue = Math.sin(angle) >= 0 ? 1.0 : -1.0; break;
                    case TRIANGLE: rawValue = (Math.abs((angle % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0; break;
                }

                // Knackschutz Hüllkurve
                double envelope = 1.0;
                int decayThreshold = (int) (samplesPerBlip * 0.7);
                if (s < 100) envelope = s / 100.0;
                else if (s > decayThreshold) envelope = 1.0 - ((double) (s - decayThreshold) / (samplesPerBlip - decayThreshold));

                double sampleValue = rawValue * envelope * volumeModifier;
                short pcm16BitValue = (short) (sampleValue * 32767.0);

                // Wird direkt nach dem Header fortlaufend ins Array geschrieben
                buffer.putShort(pcm16BitValue);
            }
        }

        return outputBuffer;
    }

    public static byte[] generateMumbleWavByteArray(String text, double baseFreq, WaveType waveType) {
        double blipDurationSeconds = 0.08;
        int samplesPerBlip = (int) (SAMPLE_RATE * blipDurationSeconds);
        int totalSamples = samplesPerBlip * text.length();//TODO FIX ME
        int dataSize = totalSamples * (BITS_PER_SAMPLE / 8);

        // Gesamte Array-Größe: 44 Bytes WAV-Header + die rohen Audio-Daten
        int totalFileSize = 44 + dataSize;
        byte[] outputBuffer = new byte[totalFileSize];

        // WAV-Header erfordern strikt LITTLE_ENDIAN
        ByteBuffer buffer = ByteBuffer.wrap(outputBuffer).order(ByteOrder.LITTLE_ENDIAN);

        // ---------------------------------------------------------------------
        // 1. SCHREIBE DEN RIFF-WAV HEADER (Die Format-Informationen)
        // ---------------------------------------------------------------------
        buffer.put("RIFF".getBytes());                      // ChunkID
        buffer.putInt(totalFileSize - 8);                   // ChunkSize
        buffer.put("WAVE".getBytes());                      // Format

        buffer.put("fmt ".getBytes());                      // Subchunk1ID
        buffer.putInt(16);                                  // Subchunk1Size (16 für PCM)
        buffer.putShort((short) 1);                         // AudioFormat (1 = unkomprimiertes PCM)
        buffer.putShort((short) CHANNELS);                  // NumChannels
        buffer.putInt(SAMPLE_RATE);                         // SampleRate
        buffer.putInt(SAMPLE_RATE * CHANNELS * (BITS_PER_SAMPLE / 8)); // ByteRate
        buffer.putShort((short) (CHANNELS * (BITS_PER_SAMPLE / 8)));   // BlockAlign
        buffer.putShort((short) BITS_PER_SAMPLE);           // BitsPerSample

        buffer.put("data".getBytes());                      // Subchunk2ID
        buffer.putInt(dataSize);                            // Subchunk2Size

        // ---------------------------------------------------------------------
        // 2. RETRO AUDIO-DATA SAMPLES GENERIEREN
        // ---------------------------------------------------------------------
        for (int i = 0; i < text.length(); i++) {
            char c = text.toUpperCase().charAt(i);

            // 1. ÜBERSPRINGEN: Wenn es kein Vokal und kein Leerzeichen ist, direkt weitergehen
            boolean isVowel = (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
            boolean isSpace = (c == ' ');

            if (!isVowel && !isSpace) {
                continue; // Springt sofort zum nächsten Buchstaben, erzeugt KEINE Pause/Zeitverlust
            }

            // 2. Dein originaler, wilder Pitch-Wechsel (+/- 35%) für das lebendige Rattern
            double randomPitchOffset = (random.nextDouble() * 0.7) - 0.35;
            double targetFrequency = baseFreq * (1.0 + randomPitchOffset);

            // Pausen-Wahrscheinlichkeit nur für echte Leerzeichen oder dezenten Zufall
            boolean isPause = random.nextInt(8) == 0 || isSpace;
            double volumeModifier = isPause ? 0.0 : (0.7 + random.nextDouble() * 0.3);

            for (int s = 0; s < samplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;
                double angle = 2.0 * Math.PI * targetFrequency * time;
                double rawValue = 0.0;

                // 3. INDIVIDUELLE VOKAL-CHARAKTERISTIK (Nur noch Vokale kommen hier an)
                double v1 = 0.0, v2 = 0.0;

                switch (c) {
                    case 'A': // Breit, offen
                        v1 = Math.sin(angle * 1.5);
                        v2 = Math.sin(angle * 2.5);
                        rawValue = (Math.sin(angle) * 0.4) + (v1 * 0.4) + (v2 * 0.2);
                        break;
                    case 'E': // Hell, spitz
                        v1 = Math.sin(angle * 2.0);
                        v2 = Math.sin(angle * 4.0);
                        rawValue = (Math.sin(angle) * 0.3) + (v1 * 0.5) + (v2 * 0.2);
                        break;
                    case 'I': // Hoch, nasal
                        v1 = Math.sin(angle * 3.5);
                        v2 = Math.sin(angle * 5.0);
                        rawValue = (Math.sin(angle) * 0.2) + (v1 * 0.5) + (v2 * 0.3);
                        break;
                    case 'O': // Tief, hohl
                        v1 = Math.sin(angle * 1.2);
                        v2 = Math.sin(angle * 1.8);
                        rawValue = (Math.sin(angle) * 0.6) + (v1 * 0.3) + (v2 * 0.1);
                        break;
                    case 'U': // Dumpf, dunkel
                        v1 = Math.sin(angle * 1.1);
                        rawValue = (Math.sin(angle) * 0.7) + (v1 * 0.3);
                        break;
                }

                // Chiptune-Mutter (SQUARE / TRIANGLE Formung)
                if (waveType == WaveType.SQUARE) {
                    rawValue = rawValue >= 0 ? 1.0 : -1.0;
                } else if (waveType == WaveType.TRIANGLE) {
                    double normalizedAngle = (rawValue + 1.0) * Math.PI;
                    rawValue = (Math.abs((normalizedAngle % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
                }

                // Knackschutz Hüllkurve (Exakt dein Original)
                double envelope = 1.0;
                int decayThreshold = (int) (samplesPerBlip * 0.7);
                if (s < 100) envelope = s / 100.0;
                else if (s > decayThreshold) envelope = 1.0 - ((double) (s - decayThreshold) / (samplesPerBlip - decayThreshold));

                double sampleValue = rawValue * envelope * volumeModifier;

                // Hard Clipping-Schutz
                if (sampleValue > 1.0) sampleValue = 1.0;
                if (sampleValue < -1.0) sampleValue = -1.0;

                short pcm16BitValue = (short) (sampleValue * 32767.0);
                buffer.putShort(pcm16BitValue);
            }
        }

        return outputBuffer;
    }
}
