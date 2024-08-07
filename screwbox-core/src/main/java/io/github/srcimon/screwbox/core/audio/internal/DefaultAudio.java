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
import java.util.UUID;
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
    private final DynamicSoundSupport dynamicSoundSupport;
    private final Map<UUID, ActivePlayback> activePlaybacks = new ConcurrentHashMap<>();

    public DefaultAudio(final ExecutorService executor, final AudioConfiguration configuration,
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
    public Audio stopAllSounds() {
        activePlaybacks.clear();
        for (final var line : audioLinePool.lines()) {
            line.flush();
        }
        return this;
    }

    @Override
    public Audio stopPlayback(final Playback playback) {
        var activePlayback = fetchActivePlayback(playback);
        if (nonNull(activePlayback)) {
            activePlayback.line().flush();
            activePlaybacks.remove(playback.id());
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
        final List<Playback> playbacks = new ArrayList<>();
        for (var activePlayback : new ArrayList<>(activePlaybacks.values())) {
            playbacks.add(activePlayback.toPlayback());
        }
        return playbacks;
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
    public boolean playbackIsActive(final Playback playback) {
        requireNonNull(playback, "playback must not be null");
        return activePlaybacks.containsKey(playback.id());
    }

    @Override
    public boolean updatePlaybackOptions(final Playback playback, final SoundOptions options) {
        requireNonNull(options, "options must not be null");

        var activePlayback = fetchActivePlayback(playback);
        if (isNull(activePlayback)) {
            return false;
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
        final var format = AudioAdapter.getAudioFormat(playback.sound().content());
        playback.setLine(audioLinePool.aquireLine(format));
        refreshLineSettingsOfPlayback(playback);

        do {
            writePlaybackDateToAudioLine(playback);
        } while (loop++ < playback.options().times() && activePlaybacks.containsKey(playback.id()));
        audioLinePool.releaseLine(playback.line());
        activePlaybacks.remove(playback.id());
    }

    private void writePlaybackDateToAudioLine(ActivePlayback playback) {
        try (var stream = AudioAdapter.getAudioInputStream(playback.sound().content())) {
            final byte[] bufferBytes = new byte[4096];
            int readBytes;
            while ((readBytes = stream.read(bufferBytes)) != -1 && activePlaybacks.containsKey(playback.id())) {
                playback.line().write(bufferBytes, 0, readBytes);
            }
            playback.line().drain();
        } catch (IOException e) {
            throw new IllegalStateException("could not close audio stream", e);
        }
    }

    @Override
    public Audio stopAllPLaybacks(final Sound sound) {
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