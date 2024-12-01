package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementConfiguration(String title, String description, int goal,
                                       boolean isFixedProgressMode, boolean usesLazyRefresh) {

    public ArchivementConfiguration {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        if (usesLazyRefresh && !isFixedProgressMode) {
            throw new IllegalArgumentException("lazy refresh should only be used when using fixed progression");
        }
    }

    public static ArchivementConfiguration title(final String title) {
        return new ArchivementConfiguration(title, null, 1, false, false);
    }

    public ArchivementConfiguration description(final String description) {
        return new ArchivementConfiguration(title, description, goal, isFixedProgressMode, usesLazyRefresh);
    }

    public ArchivementConfiguration goal(final int goal) {
        return new ArchivementConfiguration(title, description, goal, isFixedProgressMode, usesLazyRefresh);
    }


    public ArchivementConfiguration useFixedProgressMode() {
        return new ArchivementConfiguration(title, description, goal, true, usesLazyRefresh);
    }

    public ArchivementConfiguration useLazyRefresh() {
        return new ArchivementConfiguration(title, description, goal, isFixedProgressMode, true);
    }

}
