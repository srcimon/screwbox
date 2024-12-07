package io.github.srcimon.screwbox.core.archivements;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchivementDetailsTest {

    @Test
    void newInstance_validValues_containsAllValues() {
        var archivementDetails = ArchivementDetails.title("finish level 1")
                .description("run until finished")
                .useAbsoluteProgression()
                .goal(1);

        assertThat(archivementDetails.goal()).isOne();
        assertThat(archivementDetails.description()).isEqualTo("run until finished");
        assertThat(archivementDetails.title()).isEqualTo("finish level 1");
        assertThat(archivementDetails.progressionIsAbsolute()).isTrue();
    }
}
