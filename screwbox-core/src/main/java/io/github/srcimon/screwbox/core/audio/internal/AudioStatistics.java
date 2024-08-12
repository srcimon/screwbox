package io.github.srcimon.screwbox.core.audio.internal;
//TODO generalize?
//TODO add engine.metrics() => returned when engine stops
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
