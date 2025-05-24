package dev.screwbox.core.audio.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.audio.Audio;
import dev.screwbox.core.audio.AudioConfiguration;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.loop.internal.Updatable;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, Updatable {

    private final ExecutorService executor;
    private final AudioConfiguration configuration;
    private final MicrophoneMonitor microphoneMonitor;
    private final AudioLinePool audioLinePool;
    private final DynamicSoundSupport dynamicSoundSupport;
    private final Map<UUID, ActivePlayback> activePlaybacks = new ConcurrentHashMap<>();
    private final AtomicInteger soundsPlayedCount = new AtomicInteger();
    private final AtomicInteger completedPlaybackCount = new AtomicInteger();

    public DefaultAudio(final ExecutorService executor,
                        final AudioConfiguration configuration,
                        final DynamicSoundSupport dynamicSoundSupport,
                        final MicrophoneMonitor microphoneMonitor,
                        final AudioLinePool audioLinePool) {
        this.executor = executor;
        this.dynamicSoundSupport = dynamicSoundSupport;
        this.microphoneMonitor = microphoneMonitor;
        this.audioLinePool = audioLinePool;
        this.configuration = configuration;
    }

    @Override
    public Audio stopAllPlaybacks() {
        activePlaybacks.clear();
        for (final var line : audioLinePool.lines()) {
            line.flush();
        }
        return this;
    }

    @Override
    public int completedPlaybackCount() {
        return completedPlaybackCount.get();
    }

    @Override
    public int soundsPlayedCount() {
        return soundsPlayedCount.get();
    }

    @Override
    public Audio stopPlayback(final Playback playback) {
        var activePlayback = fetchActivePlayback(playback);
        if (nonNull(activePlayback)) {
            if (nonNull(activePlayback.line())) {
                activePlayback.line().flush();
            }
            activePlaybacks.remove(playback.id());
        }
        return this;
    }

    @Override
    public int lineCount() {
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
        final List<Playback> playbacks = new ArrayList<>();
        for (var activePlayback : new ArrayList<>(activePlaybacks.values())) {
            playbacks.add(activePlayback.toPlayback());
        }
        return playbacks;
    }

    @Override
    public List<Playback> activePlaybacksMatching(final Predicate<Playback> condition) {
        final List<Playback> playbacks = new ArrayList<>();
        for (var activePlayback : new ArrayList<>(activePlaybacks.values())) {
            final var playback = activePlayback.toPlayback();
            if (condition.test(playback)) {
                playbacks.add(playback);
            }
        }
        return playbacks;
    }

    @Override
    public boolean hasActivePlaybacksMatching(Predicate<Playback> condition) {
        for (var activePlayback : new ArrayList<>(activePlaybacks.values())) {
            final var playback = activePlayback.toPlayback();
            if (condition.test(playback)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Playback playSound(final Sound sound, final SoundOptions options) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");

        ActivePlayback activePlayback = new ActivePlayback(sound, options);
        activePlaybacks.put(activePlayback.id(), activePlayback);
        executor.execute(() -> play(activePlayback));
        return activePlayback.toPlayback();
    }

    @Override
    public boolean isPlaybackActive(final Playback playback) {
        requireNonNull(playback, "playback must not be null");
        return activePlaybacks.containsKey(playback.id());
    }

    @Override
    public boolean updatePlaybackOptions(final Playback playback, final SoundOptions options) {
        requireNonNull(options, "options must not be null");
        final var activePlayback = fetchActivePlayback(playback);
        if (isNull(activePlayback)) {
            return false;
        }
        if (activePlayback.options().speed() != options.speed()) {
            throw new IllegalArgumentException("cannot change speed of playback once it has started");
        }
        activePlayback.setOptions(options);
        return true;
    }

    private ActivePlayback fetchActivePlayback(Playback playback) {
        requireNonNull(playback, "playback must not be null");
        return activePlaybacks.get(playback.id());
    }

    private void play(final ActivePlayback playback) {
        int loop = 1;
        final var speedFormat = getFormatMatching(playback);

        playback.setLine(audioLinePool.acquireLine(speedFormat));
        refreshLineSettingsOfPlayback(playback);

        do {
            writePlaybackDateToAudioLine(playback);
            soundsPlayedCount.incrementAndGet();
        } while (loop++ < playback.options().times() && activePlaybacks.containsKey(playback.id()));
        playback.line().drain();
        audioLinePool.releaseLine(playback.line());
        activePlaybacks.remove(playback.id());
        completedPlaybackCount.incrementAndGet();
    }

    private AudioFormat getFormatMatching(final ActivePlayback playback) {
        final var format = AudioAdapter.getAudioFormat(playback.sound().content());
        return playback.options().speed() == 1
                ? format
                : new AudioFormat(
                format.getEncoding(),
                (float) (format.getSampleRate() * playback.options().speed()),
                format.getSampleSizeInBits(),
                format.getChannels(),
                format.getFrameSize(),
                (float) (format.getFrameRate() * playback.options().speed()),
                format.isBigEndian());
    }

    private void writePlaybackDateToAudioLine(final ActivePlayback playback) {
        try (var stream = AudioAdapter.getAudioInputStream(playback.sound().content())) {
            final byte[] bufferBytes = new byte[4096];
            int readBytes;
            while ((readBytes = stream.read(bufferBytes)) != -1 && activePlaybacks.containsKey(playback.id())) {
                playback.line().write(bufferBytes, 0, readBytes);
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not close audio stream", e);
        }
    }

    @Override
    public Audio stopAllPlaybacks(final Sound sound) {
        requireNonNull(sound, "sound must not be null");
        for (final var activePlayback : fetchPlaybacks(sound)) {
            activePlaybacks.remove(activePlayback.id());
        }
        return this;
    }

    @Override
    public int activePlaybackCount(final Sound sound) {
        int count = 0;
        for (final var activePlayback : allActivePlaybacks()) {
            if (activePlayback.sound().equals(sound)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean hasActivePlaybacks(final Sound sound) {
        return activePlaybackCount(sound) > 0;
    }

    @Override
    public int activePlaybackCount() {
        return activePlaybacks.size();
    }

    @Override
    public AudioConfiguration configuration() {
        return configuration;
    }

    @Override
    public void update() {
        for (var activePlayback : allActivePlaybacks()) {
            if (nonNull(activePlayback.line())) {
                refreshLineSettingsOfPlayback(activePlayback);
            }
        }
    }

    private void refreshLineSettingsOfPlayback(ActivePlayback activePlayback) {
        AudioAdapter.setVolume(activePlayback.line(), dynamicSoundSupport.currentVolume(activePlayback.options()));
        AudioAdapter.setPan(activePlayback.line(), dynamicSoundSupport.currentPan(activePlayback.options()));
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