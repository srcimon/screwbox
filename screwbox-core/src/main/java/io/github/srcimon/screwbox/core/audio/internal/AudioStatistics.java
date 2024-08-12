package io.github.srcimon.screwbox.core.audio.internal;
//TODO generalize?
public class AudioStatistics {

    private int soundCount;
    private int playbacksCount;

    public void increaseSounds() {
        synchronized (this) {
            soundCount++;
        }
    }

    public void increasePlaybacks() {
        synchronized (this) {
            playbacksCount++;
        }
    }

    public int soundCount() {
        return soundCount;
    }

    public int playbackCount() {
        return playbacksCount;
    }
}
