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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;
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
            activePlayback.currentOptions = determinActualOptions(activePlayback.options);
        }
    }

    public class ActivePlayback implements Playback {
        private final UUID id;
        private final Sound sound;
        private final SourceDataLine line;
        private final SoundOptions options;
        private SoundOptions currentOptions;

        public ActivePlayback(Sound sound, SoundOptions options, SourceDataLine line) {
            this.id = UUID.randomUUID();
            this.sound = sound;
            this.options = options;
            this.currentOptions = options;
            this.line = line;
        }

        public UUID id() {
            return id;
        }

        public SoundOptions options() {
            return options;
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
    public Audio playSound(final Sound sound, final SoundOptions options) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");

        executor.execute(() -> play(sound, options));
        return this;
    }

//    @Override
//    public List<Playback> activePlaybacks() {
//        List<Playback> activePlaybacks = new ArrayList<>();
//        for (final var activePlayback : playbackTracker.allActive()) {
//            activePlaybacks.add(activePlayback.playback());
//        }
//        return activePlaybacks;
//    }


    private void play(final Sound sound, final SoundOptions options) {
        int loop = 0;
        final var format = AudioAdapter.getAudioFormat(sound.content());
        var line = dataLinePool.getLine(format);
        ActivePlayback activePlayback = new ActivePlayback(sound, options, line);
        activePlayback.currentOptions = determinActualOptions(options);
        activePlaybacks.put(activePlayback.id, activePlayback);
        audioAdapter.setVolume(line, activePlayback.currentOptions.volume());
        audioAdapter.setBalance(line, activePlayback.currentOptions.balance());
        audioAdapter.setPan(line, activePlayback.currentOptions.pan());

        do {
            try (var stream = AudioAdapter.getAudioInputStream(sound.content())) {
                loop++;
                final byte[] bufferBytes = new byte[4096];
                int readBytes;
                while ((readBytes = stream.read(bufferBytes)) != -1 && activePlaybacks.containsKey(activePlayback.id)) {
                    line.write(bufferBytes, 0, readBytes);
                }
                line.drain();
            } catch (IOException e) {
                throw new IllegalStateException("could not close audio stream", e);
            }
        } while (loop < activePlayback.currentOptions.times() && activePlaybacks.containsKey(activePlayback.id));
        dataLinePool.freeLine(line);
        activePlaybacks.remove(activePlayback.id);
    }

    private SoundOptions determinActualOptions(SoundOptions options) {
        return calculateCurrent(options).volume(calculateVolume(options));
    }

    @Override
    public Audio stopSound(final Sound sound) {
        requireNonNull(sound, "sound must not be null");
        for (final var activePlayback : fetchPlaybacks(sound)) {
            activePlaybacks.remove(activePlayback.id());
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

//    @Override
//    public void configurationChanged(final AudioConfigurationEvent event) {
//        if (MUSIC_VOLUME.equals(event.changedProperty()) || EFFECTS_VOLUME.equals(event.changedProperty())) {
//            for (final var activePlayback : allActivePlaybacks()) {
//                Percent volume = calculateVolume(activePlayback.options());
//                changeVolume(activePlayback, volume);
//            }
//        }
//    }

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