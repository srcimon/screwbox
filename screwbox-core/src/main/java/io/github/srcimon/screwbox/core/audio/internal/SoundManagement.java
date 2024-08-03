package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SoundManagement {

    private final Map<UUID, ManagedSound> activeSounds = new ConcurrentHashMap<>();
    private final AudioAdapter audioAdapter;
    private final DataLinePool dataLinePool;

    public SoundManagement(AudioAdapter audioAdapter, DataLinePool dataLinePool) {
        this.audioAdapter = audioAdapter;
        this.dataLinePool = dataLinePool;
    }

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

    public void streamPlayback(final Playback playback, final Percent volume) {
        int loop = 0;
        ManagedSound managedSound = add(playback);
        while (loop < playback.options().times() && !managedSound.isShutdown()) {
            loop++;
            try (var stream = AudioAdapter.getAudioInputStream(playback.sound().content())) {
                var line = dataLinePool.getLine(stream.getFormat());
                managedSound.setLine(line);
                audioAdapter.setVolume(line, volume);
                audioAdapter.setBalance(line, playback.options().balance());
                audioAdapter.setPan(line, playback.options().pan());
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && !managedSound.isShutdown()) {
                    line.write(bufferBytes, 0, readBytes);
                }
                dataLinePool.freeLine(line);
                remove(managedSound);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
