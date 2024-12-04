package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementDetails(String title, String description, int goal,
                                 boolean isProbeMode, boolean isUpdateLazy) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        Validate.isFalse(isUpdateLazy && !isProbeMode, "lazy refresh should only be used when using probe mode");
    }

    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, false, false);
    }

    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, isProbeMode, isUpdateLazy);
    }

    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, isProbeMode, isUpdateLazy);
    }

    public ArchivementDetails probeMode() {
        return new ArchivementDetails(title, description, goal, true, isUpdateLazy);
    }

    public ArchivementDetails updateLazy() {
        return new ArchivementDetails(title, description, goal, isProbeMode, true);
    }

}
