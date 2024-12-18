package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArchivementDetailsTest {

    @Test
    void newInstance_validValues_containsAllValues() {
        var archivementDetails = ArchivementDetails.title("finish level 1")
                .description("run until finished")
                .useAbsoluteProgression()
                .icon(SpriteBundle.ARCHIVEMENT)
                .goal(1);

        assertThat(archivementDetails.goal()).isOne();
        assertThat(archivementDetails.description()).isEqualTo("run until finished");
        assertThat(archivementDetails.title()).isEqualTo("finish level 1");
        assertThat(archivementDetails.progressionIsAbsolute()).isTrue();
        assertThat(archivementDetails.icon()).contains(SpriteBundle.ARCHIVEMENT.get());
    }

    @Test
    void newInstance_titleNull_throwsException() {
        assertThatThrownBy(() -> ArchivementDetails.title(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("title must not be null");
    }

    @Test
    void newInstance_titleEmpty_throwsException() {
        assertThatThrownBy(() -> ArchivementDetails.title(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title must not be empty");
    }

    @Test
    void goal_goalIsNegative_throwsException() {
        var archivementDetails = ArchivementDetails.title("negative goals are not allowed");
        assertThatThrownBy(() -> archivementDetails.goal(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("goal must be positive");
    }
}
