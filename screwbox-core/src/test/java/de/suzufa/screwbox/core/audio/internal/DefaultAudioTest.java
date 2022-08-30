package de.suzufa.screwbox.core.audio.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.audio.Sound;

@ExtendWith(MockitoExtension.class)
class DefaultAudioTest {

    @InjectMocks
    DefaultAudio audio;

    @Mock
    AudioAdapter audioAdapter;

    @Mock
    Clip clip;

    @Mock
    FloatControl gainControl;

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
    void playEffect_invokesMethodsOnClipAndIncreasesActiveCount() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);
        when(clip.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(gainControl);

        audio.playEffect(sound);

        waitExecutorToFinishTasks();

        verify(gainControl).setValue(0.0f);
        verify(clip).setFramePosition(0);
        verify(clip).addLineListener(audio);
        verify(clip).start();

        assertThat(audio.activeCount(sound)).isEqualTo(1);
    }

    @Test
    void activeCount_oneInstanceStartedAndStopped_isZero() {
        Sound sound = Sound.fromFile("kill.wav");

        when(audioAdapter.createClip(sound)).thenReturn(clip);
        when(clip.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(gainControl);

        audio.playEffect(sound);

        waitExecutorToFinishTasks();

        audio.update(stopEventFor(clip));

        assertThat(audio.activeCount(sound)).isZero();
    }

    @Test
    void stopAllSounds_clipIsActive_clipIsStopped() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);
        when(clip.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(gainControl);
        audio.playMusic(sound);

        audio.stopAllSounds();

        waitExecutorToFinishTasks();

        verify(clip).stop();
        assertThat(audio.activeCount()).isZero();
    }

    @Test
    void setEffectVolume_setsEffectVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        when(audioAdapter.createClip(sound)).thenReturn(clip);
        when(clip.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(gainControl);

        audio.setEffectVolume(Percentage.half());
        audio.playEffect(sound);

        waitExecutorToFinishTasks();

        verify(gainControl).setValue(-6.0206003f);
        assertThat(audio.effectVolume()).isEqualTo(Percentage.half());
    }

    private LineEvent stopEventFor(Clip clipMock) {
        LineEvent stopEventMock = new LineEvent(mock(Line.class), Type.STOP, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getSource() {
                return clipMock;
            };
        };
        return stopEventMock;
    }

    @AfterEach
    void afterEach() {
        waitExecutorToFinishTasks();
    }

    private void waitExecutorToFinishTasks() {
        try {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
