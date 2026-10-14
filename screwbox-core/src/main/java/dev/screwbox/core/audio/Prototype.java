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

        double noiseFilterLP = 0.0;
        // ---------------------------------------------------------------------
        // 2. RETRO AUDIO-DATA SAMPLES GENERIEREN
        // ---------------------------------------------------------------------
        for (int i = 0; i < text.length(); i++) {
            char c = text.toUpperCase().charAt(i);
            boolean isSpace = (c == ' ');

            double randomPitchOffset = (random.nextDouble() * 0.7) - 0.35;
            double targetFrequency = baseFreq * (1.0 + randomPitchOffset);

            boolean isPause = random.nextInt(8) == 0 || isSpace;
            double volumeModifier = isPause ? 0.0 : (0.7 + random.nextDouble() * 0.3);

            for (int s = 0; s < samplesPerBlip; s++) {
                double time = (double) s / SAMPLE_RATE;

                // Sanfteres Phasen-Chaos (halbiert für weniger digitale Artefakte)
                double phaseChaos = (random.nextDouble() * 0.08 - 0.04);
                double angle = 2.0 * Math.PI * targetFrequency * time + phaseChaos;

                double rawValue = 0.0;

                if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
                    // --- KLASSE 1: VOKALE (Weich & Tonal) ---
                    double vowelShift = 1.0;
                    switch (c) {
                        case 'A': vowelShift = 1.2; break;
                        case 'E': vowelShift = 1.8; break;
                        case 'I': vowelShift = 2.5; break; // Nicht mehr ganz so extrem scharf
                        case 'O': vowelShift = 0.8; break;
                        case 'U': vowelShift = 0.6; break;
                    }
                    double vAngle = angle * vowelShift;
                    rawValue = generateBaseWave(vAngle, waveType, random);

                } else if ("SZFCXCHKSST".contains(String.valueOf(c))) {
                    // --- KLASSE 2: FRIKATIVE (Jetzt mit weichem Low-Pass-Filter!) ---
                    double rawNoise = (random.nextDouble() * 2.0 - 1.0) * 0.35; // Lautstärke halbiert

                    // Tiefpass-Filter: Lässt das Rauschen "hauchend" statt stechend klingen
                    noiseFilterLP = (noiseFilterLP * 0.85) + (rawNoise * 0.15);
                    rawValue = noiseFilterLP;

                } else if ("PTKBDG".contains(String.valueOf(c))) {
                    // --- KLASSE 3: PLOSIVE (Sanfterer Klick-Ansatz) ---
                    if (s < samplesPerBlip * 0.10) {
                        // Ein sinusbasierter "Pop" statt reinem Rausch-Kratzen
                        rawValue = Math.sin(2.0 * Math.PI * 120.0 * time) * 0.6;
                    } else {
                        rawValue = 0.0;
                    }

                } else if (!isSpace) {
                    // --- KLASSE 4: NASALE (M, N, L...) ---
                    // Sehr weicher, dumpfer Sound
                    double nasalAngle = angle * 0.5;
                    rawValue = generateBaseWave(nasalAngle, waveType, random) * 0.6;
                }

                // Knackschutz Hüllkurve
                double envelope = 1.0;
                int decayThreshold = (int) (samplesPerBlip * 0.7);
                if (s < 100) envelope = s / 100.0;
                else if (s > decayThreshold) envelope = 1.0 - ((double) (s - decayThreshold) / (samplesPerBlip - decayThreshold));

                double sampleValue = rawValue * envelope * volumeModifier;

                if (sampleValue > 1.0) sampleValue = 1.0;
                if (sampleValue < -1.0) sampleValue = -1.0;

                short pcm16BitValue = (short) (sampleValue * 32767.0);
                buffer.putShort(pcm16BitValue);
            }
        }

        return outputBuffer;
    }

    private static double generateBaseWave(double angle, WaveType waveType, Random random) {
        switch (waveType) {
            case SINE:     return Math.sin(angle);
            case SQUARE:
                double dutyCycle = 0.35 + (Math.sin(angle * 0.3) * 0.15); // Dynamische Pulsbreite
                return (angle % (2 * Math.PI) < (2 * Math.PI * dutyCycle)) ? 1.0 : -1.0;
            case TRIANGLE: return (Math.abs((angle % (2 * Math.PI)) - Math.PI) / Math.PI) * 2.0 - 1.0;
            default:       return 0.0;
        }
    }
}
