package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;

import java.util.Optional;

public interface ArchivementInfo {

    String title();

    Optional<String> description();

    int score();

    int goal();

    boolean isCompleted();

    Percent progress();

    Time startTime();

    Time completionTime();

}
