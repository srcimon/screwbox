package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Percent;

public interface ArchivementInfo {

    String title();

    int score();

    int goal();

    boolean isCompleted();

    Percent progress();
}
