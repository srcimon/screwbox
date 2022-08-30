package de.suzufa.screwbox.core.audio.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.audio.Sound;

@ExtendWith(MockitoExtension.class)
class DefaultAudioTest {

    @InjectMocks
    DefaultAudio audio;

    @Mock
    AudioAdapter audioAdapter;

    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newCachedThreadPool();
        audio = new DefaultAudio(executor, audioAdapter);
    }

    @Test
    void activeCount_noneActive_isZero() {
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void activeCount_noInstanceActive_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(audio.activeCount(sound)).isZero();
    }

    @Test
    void activeCount_oneInstanceActive_isOne() {
        Sound sound = Sound.fromFile("kill.wav");

        Clip clipMock = Mockito.mock(Clip.class);
        when(audioAdapter.createClip(sound)).thenReturn(clipMock);
        FloatControl gainControlMock = Mockito.mock(FloatControl.class);
        when(clipMock.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(gainControlMock);

        audio.playEffect(sound);

        awaitShutdown();

        verify(clipMock).setFramePosition(0);
        verify(clipMock).addLineListener(audio);

        verify(gainControlMock).setValue(0.0f);
        verify(clipMock).start();

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void activeCount_oneInstanceStartedAndStopped_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        Clip clipMock = Mockito.mock(Clip.class);
        when(audioAdapter.createClip(sound)).thenReturn(clipMock);
        FloatControl gainControlMock = Mockito.mock(FloatControl.class);
        when(clipMock.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(gainControlMock);

        audio.playEffect(sound);

        awaitShutdown();

        LineEvent stopEventMock = new LineEvent(Mockito.mock(Line.class), Type.STOP, 0) {
            @Override
            public Object getSource() {
                return clipMock;
            };
        };

        audio.update(stopEventMock);

        assertThat(audio.activeCount(sound)).isZero();
    }

    private void awaitShutdown() {
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
