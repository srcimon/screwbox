package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, Updatable {

    private final ExecutorService executor;
    private final Camera camera;
    private final AudioConfiguration configuration;
    private final MicrophoneMonitor microphoneMonitor;
    private final AudioAdapter audioAdapter;

    private final Map<UUID, ActivePlayback> activePlaybacks = new ConcurrentHashMap<>();
    private final DataLinePool dataLinePool;

    @Override
    public void update() {
        for (var activePlayback : allActivePlaybacks()) {
            SoundOptions currentOptions = determinActualOptions(activePlayback.options);
            if (!activePlayback.currentOptions.equals(currentOptions)) {
                applyOptionsOnLine(activePlayback.line, currentOptions);
                activePlayback.currentOptions = currentOptions;
            }
        }
    }

    public class ActivePlayback {
        private final UUID id;
        private final Sound sound;
        private SoundOptions options;
        private SourceDataLine line;
        private SoundOptions currentOptions;

        public ActivePlayback(final Sound sound, final SoundOptions options) {
            this.id = UUID.randomUUID();
            this.sound = sound;
            this.options = options;
            this.currentOptions = options;
        }
    }

    public DefaultAudio(final ExecutorService executor, final AudioConfiguration configuration,
                        final MicrophoneMonitor microphoneMonitor, final Camera camera, final AudioAdapter audioAdapter, final DataLinePool dataLinePool) {
        this.executor = executor;
        this.camera = camera;
        this.microphoneMonitor = microphoneMonitor;
        this.dataLinePool = dataLinePool;
        this.configuration = configuration;
        this.audioAdapter = audioAdapter;
    }

    @Override
    public Audio stopAllSounds() {
        activePlaybacks.clear();
        return this;
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
        List<Playback> playbacks = new ArrayList<>();
        for (var activePlayback : allActivePlaybacks()) {
            playbacks.add(new Playback(activePlayback.id, activePlayback.sound, activePlayback.options));//TODO activePlayback.toPlayback();
        }
        return playbacks;
    }

    private SoundOptions calculateCurrent(SoundOptions options) {
        if (isNull(options.position())) {
            return options;
        }
        final var distance = camera.position().distanceTo(options.position());
        final var direction = modifier(options.position().x() - camera.position().x());
        final var quotient = distance / configuration.soundRange();
        return options
                .pan(direction * quotient)
                .volume(Percent.of(1 - quotient));
    }

    @Override
    public UUID playSound(final Sound sound, final SoundOptions options) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");

        ActivePlayback activePlayback = new ActivePlayback(sound, options);
        activePlayback.currentOptions = determinActualOptions(options);
        activePlaybacks.put(activePlayback.id, activePlayback);
        executor.execute(() -> play(sound, activePlayback));
        return activePlayback.id;
    }

    @Override
    public boolean isActive(final UUID playbackId) {
        return activePlaybacks.containsKey(playbackId);
    }

    @Override
    public boolean updateOptions(UUID playbackId, SoundOptions options) {
        var playback = activePlaybacks.get(playbackId);
        if (Objects.isNull(playback)) {
            return false;
        }
        playback.options = options;
        return true;
    }

    private void play(final Sound sound, final ActivePlayback activePlayback) {
        int loop = 0;
        final var format = AudioAdapter.getAudioFormat(sound.content());
        activePlayback.line = dataLinePool.getLine(format);
        applyOptionsOnLine(activePlayback.line, activePlayback.currentOptions);

        do {
            try (var stream = AudioAdapter.getAudioInputStream(sound.content())) {
                loop++;
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && activePlaybacks.containsKey(activePlayback.id)) {
                    activePlayback.line.write(bufferBytes, 0, readBytes);
                }
                activePlayback.line.drain();
            } catch (IOException e) {
                throw new IllegalStateException("could not close audio stream", e);
            }
        } while (loop < activePlayback.currentOptions.times() && activePlaybacks.containsKey(activePlayback.id));
        dataLinePool.freeLine(activePlayback.line);
        activePlaybacks.remove(activePlayback.id);
    }

    private void applyOptionsOnLine(final SourceDataLine line, final SoundOptions options) {
        if (nonNull(line)) {
            audioAdapter.setVolume(line, options.volume());
            audioAdapter.setBalance(line, options.balance());
            audioAdapter.setPan(line, options.pan());
        }
    }

    private SoundOptions determinActualOptions(SoundOptions options) {
        SoundOptions in = calculateCurrent(options);
        return in.volume(calculateVolume(in));
    }

    @Override
    public Audio stopSound(final Sound sound) {
        requireNonNull(sound, "sound must not be null");
        for (final var activePlayback : fetchPlaybacks(sound)) {
            activePlaybacks.remove(activePlayback.id);
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

    private Percent calculateVolume(final SoundOptions options) {
        if (options.isMusic()) {
            final var musicVolume = configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
            return musicVolume.multiply(options.volume().value());
        }
        final var effectVolume = configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
        return effectVolume.multiply(options.volume().value());
    }
}