package io.github.srcimon.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class WarumUpDataLinePoolTask implements Runnable {

    private final DataLinePool dataLinePool;

    public WarumUpDataLinePoolTask(final DataLinePool dataLinePool) {
        this.dataLinePool = dataLinePool;
    }

    @Override
    public void run() {
        // create active lines for common audio formats -> reduces lag on first sound that's played
        dataLinePool.startNewLine(new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false));
        dataLinePool.startNewLine(new AudioFormat(PCM_SIGNED, 48000, 16, 2, 4, 44100, false));
    }
}
