package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementDetails(String title, String description, int goal,
                                 boolean isFixedProgressMode, boolean usesLazyRefresh) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        if (usesLazyRefresh && !isFixedProgressMode) {
            throw new IllegalArgumentException("lazy refresh should only be used when using fixed progression");
        }
    }

    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, false, false);
    }

    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, isFixedProgressMode, usesLazyRefresh);
    }

    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, isFixedProgressMode, usesLazyRefresh);
    }

    public ArchivementDetails useFixedProgressMode() {
        return new ArchivementDetails(title, description, goal, true, usesLazyRefresh);
    }

    public ArchivementDetails useLazyRefresh() {
        return new ArchivementDetails(title, description, goal, isFixedProgressMode, true);
    }

}
