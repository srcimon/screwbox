package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Playback;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//TODO rename this class and the managed sound class
public class PlaybackTracker {
    //TODO rework playback with position all propertys can be changed dynamically
    public record ActivePlayback(UUID id, Playback playback, SourceDataLine line) {
    }

    private final Map<UUID, ActivePlayback> activeSounds = new ConcurrentHashMap<>();
    private final AudioAdapter audioAdapter;
    private final DataLinePool dataLinePool;

    public PlaybackTracker(AudioAdapter audioAdapter, DataLinePool dataLinePool) {
        this.audioAdapter = audioAdapter;
        this.dataLinePool = dataLinePool;
    }

    public void play(final Playback playback, final Percent volume) {//TODO <-- use sound options here
        int loop = 0;
        UUID id = UUID.randomUUID();//TODO move id into playback / offer updatePosition(Playback)?

        final var format = AudioAdapter.getAudioFormat(playback.sound().content());
        var line = dataLinePool.getLine(format);
        ActivePlayback activePlayback = new ActivePlayback(id, playback, line);
        activeSounds.put(activePlayback.id, activePlayback);
        audioAdapter.setVolume(line, volume);
        audioAdapter.setBalance(line, playback.options().balance());
        audioAdapter.setPan(line, playback.options().pan());


        do {
            try (var stream = AudioAdapter.getAudioInputStream(playback.sound().content())) {
                loop++;
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && activeSounds.containsKey(activePlayback.id)) {
                    line.write(bufferBytes, 0, readBytes);
                }
                line.drain();
                dataLinePool.freeLine(line);
            } catch (IOException e) {
                throw new IllegalStateException("could not close audio stream", e);
            }
        } while (loop < playback.options().times() && activeSounds.containsKey(id));

        activeSounds.remove(id);
    }

    public void changeVolume(ActivePlayback activePlayback, Percent volume) {
        audioAdapter.setVolume(activePlayback.line(), volume);
    }

    public void stop(final ActivePlayback activePlayback) {
        activeSounds.remove(activePlayback.id);
    }

    public List<ActivePlayback> allActive() {
        return new ArrayList<>(activeSounds.values());
    }
}
