package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SoundManagement {

    private final Map<UUID, ManagedSound> activeSounds = new ConcurrentHashMap<>();

    public static class ManagedSound {
        private UUID id = UUID.randomUUID();
        private final Playback playback;
        private boolean isShutdown = false;
        private SourceDataLine line;

        public Sound sound() {
            return playback.sound();
        }

        public SourceDataLine line() {
            return line;
        }

        public ManagedSound(final Playback playback) {
            this.playback = playback;
        }

        public void stop() {
            isShutdown = true;
        }

        public Playback playback() {
            return playback;
        }

        public boolean isShutdown() {
            return isShutdown;
        }

        public void setLine(SourceDataLine line) {
            this.line = line;
        }
    }

    public ManagedSound add(Playback playback) {
        ManagedSound managedSound = new ManagedSound(playback);
        activeSounds.put(managedSound.id, managedSound);
        return managedSound;
    }

    public void remove(ManagedSound managedSound) {
        activeSounds.remove(managedSound.id);
    }

    public List<ManagedSound> activeSounds() {
        return new ArrayList<>(activeSounds.values());
    }

    public List<ManagedSound> fetchActiveSounds(final Sound sound) {
        final List<ManagedSound> active = new ArrayList<>();
        for (final var activeSound : activeSounds.values()) {
            if (activeSound.playback().sound().equals(sound)) {
                active.add(activeSound);
            }
        }
        return active;
    }
}
