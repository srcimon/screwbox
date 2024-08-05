package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, Updatable {

    private static class ActivePlayback implements Playback {

        @Serial
        private static final long serialVersionUID = 1L;

        private final UUID id;
        private final Sound sound;
        private SoundOptions options;
        private SourceDataLine line;
        private SoundOptions currentOptions;

        public ActivePlayback(final Sound sound, final SoundOptions options, final SoundOptions currentOptions) {
            this.id = UUID.randomUUID();
            this.sound = sound;
            this.options = options;
            this.currentOptions = currentOptions;
        }

        @Override
        public Sound sound() {
            return sound;
        }

        @Override
        public SoundOptions options() {
            return options;
        }
    }

    private final ExecutorService executor;
    private final AudioConfiguration configuration;
    private final MicrophoneMonitor microphoneMonitor;
    private final AudioAdapter audioAdapter;
    private final AudioLinePool audioLinePool;
    private final SoundOptionsSupport soundOptionsSupport;
    private final Map<Playback, ActivePlayback> activePlaybacks = new ConcurrentHashMap<>();

    public DefaultAudio(final ExecutorService executor, final AudioConfiguration configuration,
                        final SoundOptionsSupport soundOptionsSupport,
                        final MicrophoneMonitor microphoneMonitor, final AudioAdapter audioAdapter, final AudioLinePool audioLinePool) {
        this.executor = executor;
        this.soundOptionsSupport = soundOptionsSupport;
        this.microphoneMonitor = microphoneMonitor;
        this.audioLinePool = audioLinePool;
        this.configuration = configuration;
        this.audioAdapter = audioAdapter;
    }

    @Override
    public Audio stopAllSounds() {
        activePlaybacks.clear();
        for (final var activePlayback : allActivePlaybacks()) {
            activePlayback.line.flush();
        }
        return this;
    }

    @Override
    public Audio stopPlayback(Playback playback) {
        var activePlayback = activePlaybacks.get(playback);
        if (nonNull(activePlayback)) {
            activePlayback.line.flush();
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

        final SoundOptions currentOptions = soundOptionsSupport.determinActualOptions(options);
        ActivePlayback activePlayback = new ActivePlayback(sound, options, currentOptions);
        activePlaybacks.put(activePlayback, activePlayback);
        executor.execute(() -> play(sound, activePlayback));
        return activePlayback;
    }

    @Override
    public boolean isActive(final Playback playback) {
        requireNonNull(playback, "playback must not be null");
        return activePlaybacks.containsKey(playback);
    }

    @Override
    public boolean updatePlaybackOptions(final Playback playback,final SoundOptions options) {
        requireNonNull(playback, "playback must not be null");
        requireNonNull(options, "options must not be null");

        var activePlayback = activePlaybacks.get(playback);
        if (isNull(activePlayback)) {
            return false;
        }
        activePlayback.options = options;
        return true;
    }

    private void play(final Sound sound, final ActivePlayback activePlayback) {
        int loop = 1;
        final var format = AudioAdapter.getAudioFormat(sound.content());
        activePlayback.line = audioLinePool.aquireLine(format);
        applyOptionsOnLine(activePlayback.line, activePlayback.currentOptions);

        do {
            try (var stream = AudioAdapter.getAudioInputStream(sound.content())) {
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && activePlaybacks.containsKey(activePlayback)) {
                    activePlayback.line.write(bufferBytes, 0, readBytes);
                }
                activePlayback.line.drain();
            } catch (IOException e) {
                throw new IllegalStateException("could not close audio stream", e);
            }
        } while (loop++ < activePlayback.currentOptions.times() && activePlaybacks.containsKey(activePlayback));
        audioLinePool.releaseLine(activePlayback.line);
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
            final SoundOptions currentOptions = soundOptionsSupport.determinActualOptions(activePlayback.options);
            if (!activePlayback.currentOptions.equals(currentOptions)) {
                applyOptionsOnLine(activePlayback.line, currentOptions);
                activePlayback.currentOptions = currentOptions;
            }
        }
    }

    private void applyOptionsOnLine(final SourceDataLine line, final SoundOptions options) {
        if (nonNull(line)) {
            audioAdapter.setVolume(line, options.volume());
            audioAdapter.setPan(line, options.pan());
        }
    }

    private List<ActivePlayback> allActivePlaybacks() {
        return new ArrayList<>(activePlaybacks.values());
    }

    private List<ActivePlayback> fetchPlaybacks(final Sound sound) {
        final var active = new ArrayList<ActivePlayback>();
        for (final var activePlayback : allActivePlaybacks()) {
            if (activePlayback.sound.equals(sound)) {
                active.add(activePlayback);
            }
        }
        return active;
    }
}