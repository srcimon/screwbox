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

//TODO rename this class and the managed sound class
public class PlaybackTracker {

    public record ManagedSound(UUID id, Playback playback, SourceDataLine line) {
    }

    private final Map<UUID, ManagedSound> activeSounds = new ConcurrentHashMap<>();
    private final AudioAdapter audioAdapter;
    private final DataLinePool dataLinePool;

    public PlaybackTracker(AudioAdapter audioAdapter, DataLinePool dataLinePool) {
        this.audioAdapter = audioAdapter;
        this.dataLinePool = dataLinePool;
    }

    public void changeVolume(ManagedSound managedSound, Percent volume) {
        audioAdapter.setVolume(managedSound.line(), volume);
    }

    public void play(final Playback playback, final Percent volume) {
        int loop = 0;
        UUID id = UUID.randomUUID();
        do {
            loop++;
            try (var stream = AudioAdapter.getAudioInputStream(playback.sound().content())) {
                var line = dataLinePool.getLine(stream.getFormat());
                ManagedSound managedSound = new ManagedSound(id, playback, line);
                activeSounds.put(managedSound.id, managedSound);
                audioAdapter.setVolume(line, volume);
                audioAdapter.setBalance(line, playback.options().balance());
                audioAdapter.setPan(line, playback.options().pan());
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && activeSounds.containsKey(managedSound.id)) {
                    line.write(bufferBytes, 0, readBytes);
                }
                dataLinePool.freeLine(line);
                activeSounds.remove(managedSound.id);
            } catch (IOException e) {
                throw new IllegalStateException("could not close audio stream", e);
            }
        } while (loop < playback.options().times() && activeSounds.containsKey(id));
    }

    public void stop(final ManagedSound managedSound) {
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
