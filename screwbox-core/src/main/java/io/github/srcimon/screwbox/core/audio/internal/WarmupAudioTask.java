package io.github.srcimon.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class WarmupAudioTask implements Runnable {

    private final AudioLinePool audioLinePool;

    public WarmupAudioTask(final AudioLinePool audioLinePool) {
        this.audioLinePool = audioLinePool;
    }

    @Override
    public void run() {
        // create active lines for common audio formats -> reduces lag on first sound that's played
        audioLinePool.prepareLine(new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false));
        audioLinePool.prepareLine(new AudioFormat(PCM_SIGNED, 48000, 16, 2, 4, 44100, false));
    }
}
