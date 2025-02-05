package io.github.srcimon.screwbox.core.achievements;

import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AchievementDetailsTest {

    @Test
    void newInstance_validValues_containsAllValues() {
        var achievementDetails = AchievementDetails.title("finish level 1")
                .description("run until finished")
                .useAbsoluteProgression()
                .icon(SpriteBundle.ACHIEVEMENT)
                .goal(1);

        assertThat(achievementDetails.goal()).isOne();
        assertThat(achievementDetails.description()).isEqualTo("run until finished");
        assertThat(achievementDetails.title()).isEqualTo("finish level 1");
        assertThat(achievementDetails.progressionIsAbsolute()).isTrue();
        assertThat(achievementDetails.icon()).contains(SpriteBundle.ACHIEVEMENT.get());
    }

    @Test
    void newInstance_titleNull_throwsException() {
        assertThatThrownBy(() -> AchievementDetails.title(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("title must not be null");
    }

    @Test
    void newInstance_titleEmpty_throwsException() {
        assertThatThrownBy(() -> AchievementDetails.title(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title must not be empty");
    }

    @Test
    void goal_goalIsNegative_throwsException() {
        var archivementDetails = AchievementDetails.title("negative goals are not allowed");
        assertThatThrownBy(() -> archivementDetails.goal(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("goal must be positive");
    }
}
