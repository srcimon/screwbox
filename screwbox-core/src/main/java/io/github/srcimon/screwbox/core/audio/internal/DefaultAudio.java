package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, Updatable {

    private final ExecutorService executor;
    private final AudioConfiguration configuration;
    private final MicrophoneMonitor microphoneMonitor;
    private final AudioLinePool audioLinePool;
    private final PlaybackSupport playbackSupport;
    private final Map<Playback, ActivePlayback> activePlaybacks = new ConcurrentHashMap<>();

    public DefaultAudio(final ExecutorService executor, final AudioConfiguration configuration,
                        final PlaybackSupport playbackSupport,
                        final MicrophoneMonitor microphoneMonitor,
                        final AudioLinePool audioLinePool) {
        this.executor = executor;
        this.playbackSupport = playbackSupport;
        this.microphoneMonitor = microphoneMonitor;
        this.audioLinePool = audioLinePool;
        this.configuration = configuration;
    }

    @Override
    public Audio stopAllSounds() {
        activePlaybacks.clear();
        for (final var activePlayback : allActivePlaybacks()) {
            activePlayback.line().flush();
        }
        return this;
    }

    @Override
    public Audio stopPlayback(Playback playback) {
        var activePlayback = activePlaybacks.get(playback);
        if (nonNull(activePlayback)) {
            activePlayback.line().flush();
            activePlaybacks.remove(playback);
        }
        return this;
    }

    @Override
    public int availableAudioLines() {
        return audioLinePool.size();
    }

    @Override
    public Percent microphoneLevel() {
        return microphoneMonitor.level();
    }

    @Override
    public boolean isMicrophoneActive() {
        return microphoneMonitor.isActive();
    }

    @Override
    public List<Playback> activePlaybacks() {
        return new ArrayList<>(activePlaybacks.values());
    }

    @Override
    public Playback playSound(final Sound sound, final SoundOptions options) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");

        ActivePlayback activePlayback = new ActivePlayback(sound, options);
        activePlaybacks.put(activePlayback, activePlayback);
        executor.execute(() -> play(activePlayback));
        return activePlayback;
    }

    @Override
    public boolean isActive(final Playback playback) {
        requireNonNull(playback, "playback must not be null");
        return activePlaybacks.containsKey(playback);
    }

    @Override
    public boolean updatePlaybackOptions(final Playback playback, final SoundOptions options) {
        requireNonNull(playback, "playback must not be null");
        requireNonNull(options, "options must not be null");

        var activePlayback = activePlaybacks.get(playback);
        if (isNull(activePlayback)) {
            return false;
        }
        activePlayback.setOptions(options);
        return true;
    }

    private void play(final ActivePlayback activePlayback) {
        int loop = 1;
        final var format = AudioAdapter.getAudioFormat(activePlayback.sound().content());
        activePlayback.setLine(audioLinePool.aquireLine(format));
        playbackSupport.applyOptionsOnPlayback(activePlayback);

        do {
            try (var stream = AudioAdapter.getAudioInputStream(activePlayback.sound().content())) {
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && activePlaybacks.containsKey(activePlayback)) {
                    activePlayback.line().write(bufferBytes, 0, readBytes);
                }
                activePlayback.line().drain();
            } catch (IOException e) {
                throw new IllegalStateException("could not close audio stream", e);
            }
        } while (loop++ < activePlayback.options().times() && activePlaybacks.containsKey(activePlayback));
        audioLinePool.releaseLine(activePlayback.line());
        activePlaybacks.remove(activePlayback);
    }

    @Override
    public Audio stopSound(final Sound sound) {
        requireNonNull(sound, "sound must not be null");
        for (final var activePlayback : fetchPlaybacks(sound)) {
            activePlaybacks.remove(activePlayback);
        }
        return this;
    }

    @Override
    public int activeCount(final Sound sound) {
        return fetchPlaybacks(sound).size();
    }

    @Override
    public boolean isActive(final Sound sound) {
        return activeCount(sound) > 0;
    }

    @Override
    public int activeCount() {
        return activePlaybacks.size();
    }

    @Override
    public AudioConfiguration configuration() {
        return configuration;
    }

    @Override
    public void update() {
        for (var activePlayback : allActivePlaybacks()) {
            playbackSupport.applyOptionsOnPlayback(activePlayback);
        }
    }

    private List<ActivePlayback> allActivePlaybacks() {
        return new ArrayList<>(activePlaybacks.values());
    }

    private List<ActivePlayback> fetchPlaybacks(final Sound sound) {
        final var active = new ArrayList<ActivePlayback>();
        for (final var activePlayback : allActivePlaybacks()) {
            if (activePlayback.sound().equals(sound)) {
                active.add(activePlayback);
            }
        }
        return active;
    }
}