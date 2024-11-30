package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementDetails(String title, String description, int goal, Class<? extends Archivement> family,
                                 boolean isFixedProgressMode, boolean usesLazyRefresh) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        if (usesLazyRefresh && !isFixedProgressMode) {
            throw new IllegalArgumentException("lazy refresh should only be used when using fixed progression");
        }
        //TODO validate family is not self
    }

    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, null, false, false);
    }


    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode, usesLazyRefresh);
    }

    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode, usesLazyRefresh);
    }

    public ArchivementDetails family(Class<? extends Archivement> family) {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode, usesLazyRefresh);
    }

    public ArchivementDetails useFixedProgressMode() {
        return new ArchivementDetails(title, description, goal, family, true, usesLazyRefresh);
    }

    public ArchivementDetails useLazyRefresh() {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode, true);
    }

}
