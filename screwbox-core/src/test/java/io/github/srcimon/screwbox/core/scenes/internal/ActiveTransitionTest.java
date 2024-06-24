package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.scenes.DefaultScene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveTransitionTest {

    ActiveTransition activeTransition;

    @BeforeEach
    void setUp() {
        activeTransition = new ActiveTransition(DefaultScene.class, SceneTransition.custom()
                .introDurationSeconds(3)
                .outroDurationSeconds(4));
    }

    @Test
    void targetScene_returnsTargetScene() {
        assertThat(activeTransition.targetScene()).isEqualTo(DefaultScene.class);
    }

    @Test
    void introProgress_atSwitchTime_isZero() {
        Percent introProgress = activeTransition.introProgress(activeTransition.switchTime());
        assertThat(introProgress).isEqualTo(Percent.zero());
    }

    @Test
    void introProgress_rightAfterStart_isMax() {
        Percent introProgress = activeTransition.introProgress(Time.now());
        assertThat(introProgress).isEqualTo(Percent.max());
    }

    @Test
    void outroProgress_atSwitchTime_isNearToMax() {
        Percent outroProgress = activeTransition.outroProgress(activeTransition.switchTime());
        assertThat(outroProgress.value()).isEqualTo(1, Offset.offset(0.1));
    }
}
